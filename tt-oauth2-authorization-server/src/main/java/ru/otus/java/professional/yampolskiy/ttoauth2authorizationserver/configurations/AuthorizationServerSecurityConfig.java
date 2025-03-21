package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.StringUtils;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.JpaOAuth2AuthorizationService;

@Configuration
public class AuthorizationServerSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServerSecurityConfig.class);

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            JpaRegisteredClientRepository registeredClientRepository,
            JpaOAuth2AuthorizationService authorizationService,
            JwtEncoder jwtEncoder,
            PasswordEncoder passwordEncoder,
            JwtGenerator jwtGenerator
    ) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http
                .with(authorizationServerConfigurer, configurer -> {
                    configurer
                            .authorizationService(authorizationService)
                            .registeredClientRepository(registeredClientRepository)
                            .tokenGenerator(jwtGenerator)
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
                            );
                })
                .authenticationProvider(clientAuthenticationProvider(
                        registeredClientRepository, authorizationService, passwordEncoder))
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/revoke"));

        return http.build();
    }

    @Bean
    public AuthenticationProvider clientAuthenticationProvider(
            JpaRegisteredClientRepository registeredClientRepository,
            JpaOAuth2AuthorizationService authorizationService,
            PasswordEncoder passwordEncoder
    ) {
        ClientSecretAuthenticationProvider provider =
                new ClientSecretAuthenticationProvider(registeredClientRepository, authorizationService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
