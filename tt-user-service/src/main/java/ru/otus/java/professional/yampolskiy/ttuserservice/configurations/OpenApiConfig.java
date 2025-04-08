package ru.otus.java.professional.yampolskiy.ttuserservice.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.error.ErrorDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.error.ValidationEmailErrorDTO;
import ru.otus.java.professional.yampolskiy.ttuserservice.dtos.error.ValidationEmailPartErrorDTO;

@OpenAPIDefinition(
        info = @Info(
                title = "TT User Service",
                version = "1.0",
                description = "User API"
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
                                @OAuthScope(name = "user:view", description = "View users"),
                                @OAuthScope(name = "user:update", description = "Update users"),
                                @OAuthScope(name = "user:delete", description = "Delete users"),
                                @OAuthScope(name = "user:assign-roles", description = "Assign roles"),
                                @OAuthScope(name = "user:manage", description = "Manage users"),
                                @OAuthScope(name = "role:create", description = "Create roles"),
                                @OAuthScope(name = "role:view", description = "View roles"),
                                @OAuthScope(name = "role:update", description = "Update roles"),
                                @OAuthScope(name = "role:delete", description = "Delete roles"),
                                @OAuthScope(name = "permission:create", description = "Create permissions"),
                                @OAuthScope(name = "permission:view", description = "View permissions"),
                                @OAuthScope(name = "permission:update", description = "Update permissions"),
                                @OAuthScope(name = "permission:delete", description = "Delete permissions")
                        }
                )
        )
)
@Configuration
public class OpenApiConfig {

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
        responses.addApiResponse("422", createResponse("Unprocessable Entity (валидация email)", "#/components/schemas/ValidationEmailErrorDTO"));
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
                        .addSchemas("ValidationEmailErrorDTO", new Schema<ValidationEmailErrorDTO>().$ref("#/components/schemas/ValidationEmailErrorDTO"))
                        .addSchemas("ValidationEmailPartErrorDTO", new Schema<ValidationEmailPartErrorDTO>().$ref("#/components/schemas/ValidationEmailPartErrorDTO"))
                )
                        .info(new io.swagger.v3.oas.models.info.Info()
                        .title("TT User Service API")
                        .version("1.0")
                        .description("Документация сервиса пользователей")
                );
    }
}
