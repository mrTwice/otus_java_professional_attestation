package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.external.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.external.exceptions.IntegrationException;

import java.time.Instant;


@Slf4j
@RequiredArgsConstructor
public class OAuth2AccessTokenManager {
    private final RestClient tokenRestClient;
    private final String clientId;
    private final String clientSecret;
    private final String scope;

    private String accessToken;
    private Instant expiresAt = Instant.EPOCH;

    public synchronized String getAccessToken() {
        if (accessToken == null || Instant.now().isAfter(expiresAt.minusSeconds(10))) {
            fetchNewToken();
        }
        return accessToken;
    }

    private void fetchNewToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("scope", scope);

        try {
            AccessTokenResponse response = tokenRestClient.post()
                    .uri("/oauth2/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .onStatus(HttpStatus.BAD_REQUEST::equals, (req, res) -> {
                        log.warn("Не удалось получить access token для клиента {}: неверные параметры", clientId);
                        throw new IntegrationException("INVALID_CLIENT_CREDENTIALS", "Некорректные параметры клиента");
                    })
                    .onStatus(HttpStatus.UNAUTHORIZED::equals, (req, res) -> {
                        log.warn("Неверный client_id или client_secret для клиента {}", clientId);
                        throw new IntegrationException("UNAUTHORIZED", "Ошибка авторизации при получении токена");
                    })
                    .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, (req, res) -> {
                        log.error("Ошибка на стороне SAS при получении токена для клиента {}", clientId);
                        throw new IntegrationException("TOKEN_ENDPOINT_UNAVAILABLE", "Ошибка получения токена");
                    })
                    .body(AccessTokenResponse.class);

            if (response != null) {
                this.accessToken = response.accessToken();
                this.expiresAt = Instant.now().plusSeconds(response.expiresIn());
                log.info("🔑 Получили access token: {}", accessToken);
            } else {
                throw new IntegrationException("TOKEN_RESPONSE_EMPTY", "Пустой ответ при получении токена");
            }
        } catch (IntegrationException ex) {
            throw ex; // пробрасываем свои исключения
        } catch (Exception ex) {
            log.error("Неизвестная ошибка при получении токена для клиента {}: {}", clientId, ex.getMessage(), ex);
            throw new IntegrationException("TOKEN_REQUEST_FAILED", "Не удалось получить токен из-за ошибки");
        }
    }

    public record AccessTokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in") long expiresIn,
            @JsonProperty("scope") String scope
    ) {}

}
