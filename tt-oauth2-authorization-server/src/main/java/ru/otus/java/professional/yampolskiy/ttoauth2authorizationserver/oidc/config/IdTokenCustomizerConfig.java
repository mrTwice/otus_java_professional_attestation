package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.oidc.service.OidcUserInfoService;

@Configuration
public class IdTokenCustomizerConfig {
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer(
            OidcUserInfoService userInfoService) {
        return (context) -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                // TODO: Добавить условную логику — кастомизировать клеймы в зависимости от клиента, grant_type, scope и т.д.
                // Пример: пропустить ID токен, если он не из authorization_code flow
                // TODO: Подгрузка дополнительных данных из внешних микросервисов (например, профиля, ролей, статуса)
                // TODO: Вставка стандартных и кастомных OIDC-клеймов
                // TODO: Добавить поддержку клеймов: auth_time, acr, amr и т.п. — для соответствия OIDC спецификации
                // TODO: Добавление кастомных бизнес-клеймов, если нужно передавать роли, идентификаторы и пр.
                // claims.put("user_role", "admin");
                // TODO: Логгирование содержимого ID токена для отладки
                // log.debug("Issuing ID token for user={}, claims={}", context.getPrincipal().getName(), claims);
                OidcUserInfo userInfo = userInfoService.loadUser(
                        context.getPrincipal().getName());
                context.getClaims().claims(claims ->
                        claims.putAll(userInfo.getClaims()));
            }
            // TODO: Делегируемая архитектура — реализовать интерфейс IdTokenClaimContributor и подключать несколько бинов
            // Пример: создать список бинов и вызывать contributors.forEach(c -> c.contribute(context, claims));
            // Разбить кастомизацию на модули (например, StandardClaimsCustomizer, ProfileClaimsCustomizer)
            // Или внедрить механизм регистрации TokenClaimContributor и вызывать их по очереди.
            /*
                interface IdTokenClaimContributor {
                    void contribute(JwtEncodingContext context, Map<String, Object> claims);
                }
             */
        };
    }
}
