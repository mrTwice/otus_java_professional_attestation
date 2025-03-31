package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.config.OAuth2AccessTokenManager;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.exceptions.IntegrationException;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserCreateDTO;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.infrastructure.dto.ErrorDTO;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.infrastructure.dto.ValidationEmailErrorDTO;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationClient {

    private static final String BASE_PATH = "/api/v1/internal/users";
    private final RestClient userServiceClient;
    private final OAuth2AccessTokenManager userServiceAccessTokenManager;

    public void createUser(UserCreateDTO request) {
        userServiceClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .build()
                )
                .headers(headers -> headers.setBearerAuth(userServiceAccessTokenManager.getAccessToken()))
                .body(request)
                .exchange((clientRequest, clientResponse) -> {
                    HttpStatusCode status = clientResponse.getStatusCode();

                    if (status.is2xxSuccessful()) {
                        return null;
                    }


                    if (status == HttpStatus.CONFLICT) {
                        ErrorDTO error = clientResponse.bodyTo(ErrorDTO.class);
                        log.warn("User already exists: [{}] {}", error.getCode(), error.getMessage());
                        throw new IntegrationException(error.getMessage(), error.getCode());
                    }

                    if (status == HttpStatus.UNPROCESSABLE_ENTITY) {
                        // Пробуем сначала ValidationEmailErrorDTO
                        try {
                            ValidationEmailErrorDTO error = clientResponse.bodyTo(ValidationEmailErrorDTO.class);
                            String message = error.getErrors().stream()
                                    .map(err -> err.getField() + ": " + err.getMessage())
                                    .collect(Collectors.joining("; "));
                            log.warn("Validation error: {}", message);
                            throw new IntegrationException(message, error.getCode());
                        } catch (RestClientException ex) {
                            // fallback на простой ErrorDTO
                            ErrorDTO error = clientResponse.bodyTo(ErrorDTO.class);
                            log.warn("Validation error (simple): {}", error.getMessage());
                            throw new IntegrationException(error.getMessage(), error.getCode());
                        }
                    }

                    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                        log.error("Internal error from user-service for user {}", request.getUsername());
                        throw new IntegrationException("Ошибка сервиса пользователей", "USER_SERVICE_UNAVAILABLE");
                    }

                    // Любая другая ошибка
                    String rawBody = new String(clientResponse.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("Unexpected error from user-service: {} - {}", status, rawBody);
                    throw new IntegrationException("UNKNOWN_ERROR", "Неизвестная ошибка");
                });
    }
}
