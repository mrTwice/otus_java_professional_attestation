package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.StringUtils;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.logging.AuthorizationCodeRequestLogger;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.logging.TokenEndpointLoggingFilter;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.auth.core.service.DatabaseAuthorizationService;


@Configuration
public class AuthorizationSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationSecurityConfig.class);


    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            DatabaseAuthorizationService authorizationService,
            AuthenticationProvider loggingOidcUserInfoAuthenticationProvider,
            AuthenticationProvider clientAuthenticationProvider,
            Filter jwtDebugLogger,
            JwtDecoder jwtDecoder,
            AuthenticationProvider authorizationCodeAuthenticationProvider,
            AuthenticationProvider authorizationCodeRequestAuthenticationProvider
    ) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .authenticationProvider(clientAuthenticationProvider)
                .authenticationProvider(authorizationCodeRequestAuthenticationProvider)
                .authenticationProvider(authorizationCodeAuthenticationProvider)
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .authorizationService(authorizationService)
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.authorizationRequestConverter(new AuthorizationCodeRequestLogger())
                                )

                                .oidc(oidc -> oidc
                                .userInfoEndpoint(userInfo -> {
                                    LOGGER.info("✅ Конфигурируем userInfoEndpoint");
                                    userInfo.authenticationProvider(loggingOidcUserInfoAuthenticationProvider);
                                }))
                                .tokenRevocationEndpoint(tokenRevocationEndpointConfigurer ->
                                        tokenRevocationEndpointConfigurer
                                                .revocationResponseHandler((request, response, authentication) -> {
                                                    String token = request.getParameter(OAuth2ParameterNames.TOKEN);
                                                    if (StringUtils.hasText(token)) {
                                                        OAuth2Authorization authorization = authorizationService.findByToken(token, null);
                                                        if (authorization != null) {
                                                            LOGGER.info("Удаляем токен через кастомный revocationResponseHandler: {}", authorization.getId());
                                                            authorizationService.remove(authorization);
                                                        } else {
                                                            LOGGER.warn("Токен не найден для удаления: {}", token);
                                                        }
                                                    }
                                                    response.setStatus(HttpServletResponse.SC_OK);
                                                })
                                )
                )
                .addFilterAfter(jwtDebugLogger, BearerTokenAuthenticationFilter.class)

                .oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(jwt -> jwt.decoder(jwtDecoder))
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );
        http.addFilterBefore(new TokenEndpointLoggingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            AuthenticationProvider userAuthenticationProvider
    ) throws Exception {
        http
                .authenticationProvider(userAuthenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/auth/register")
                )
                .formLogin(Customizer.withDefaults())
                .requestCache(requestCache -> requestCache
                        .requestCache(new HttpSessionRequestCache())
                )
                .exceptionHandling(exceptions ->
                        exceptions.accessDeniedHandler((request, response, accessDeniedException) -> {
                            LOGGER.error("Ошибка доступа: {}", accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                        })
                );
        return http.build();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher() {
        return new AuthenticationEventPublisher() {
            private final Logger logger = LoggerFactory.getLogger("AuthenticationEventPublisher");

            @Override
            public void publishAuthenticationSuccess(Authentication authentication) {
                logger.info("✅ Успешная аутентификация: {}", authentication.getClass().getSimpleName());
            }

            @Override
            public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
                logger.warn("❌ Ошибка аутентификации: {}, тип токена: {}", exception.getMessage(),
                        authentication != null ? authentication.getClass().getSimpleName() : "null", exception);
            }
        };
    }
}
