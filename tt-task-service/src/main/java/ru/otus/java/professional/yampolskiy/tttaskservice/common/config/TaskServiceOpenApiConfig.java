package ru.otus.java.professional.yampolskiy.tttaskservice.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.professional.yampolskiy.tttaskservice.common.dto.ErrorDTO;


@OpenAPIDefinition(
        info = @Info(
                title = "TT Task Service",
                version = "1.0",
                description = "Task API"
        ),
        security = @SecurityRequirement(name = "oidc")
)
@SecurityScheme(
        name = "oidc",
        type = SecuritySchemeType.OAUTH2,
        scheme = "bearer",
        bearerFormat = "JWT",
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:8080/oauth2/authorize",
                        tokenUrl = "http://localhost:8080/oauth2/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "Basic identity scope"),
                                @OAuthScope(name = "profile", description = "User profile"),
                                @OAuthScope(name = "offline_access", description = "Offline access (refresh tokens)")

                        }
                )
        )
)
@Configuration
public class TaskServiceOpenApiConfig {

    @Bean
    public OpenApiCustomizer globalResponseCustomiser() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) -> {
                    pathItem.readOperations().forEach(this::addGlobalResponses);
                });
            }
        };
    }

    private void addGlobalResponses(Operation operation) {
        ApiResponses responses = operation.getResponses();

        responses.addApiResponse("400", createResponse("Bad Request (ошибка запроса)", "#/components/schemas/ErrorDTO"));
        responses.addApiResponse("401", createResponse("Unauthorized (неавторизован)", "#/components/schemas/ErrorDTO"));
        responses.addApiResponse("403", createResponse("Forbidden (нет доступа)", "#/components/schemas/ErrorDTO"));
        responses.addApiResponse("404", createResponse("Not Found", "#/components/schemas/ErrorDTO"));
        //responses.addApiResponse("422", createResponse("Unprocessable Entity (валидация email)", "#/components/schemas/ValidationEmailErrorDTO"));
        responses.addApiResponse("500", createResponse("Internal Server Error", "#/components/schemas/ErrorDTO"));
    }

    private ApiResponse createResponse(String description, String schemaRef) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().$ref(schemaRef))));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("ErrorDTO", new Schema<ErrorDTO>().$ref("#/components/schemas/ErrorDTO"))

                )
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("TT Task Service API")
                        .version("1.0")
                        .description("Документация сервиса задач")
                );
    }
}
