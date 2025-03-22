package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.util.StringUtils;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.CustomDaoAuthenticationProvider;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.JpaOAuth2AuthorizationService;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.LoggingClientAuthenticationProvider;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class AuthorizationServerSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServerSecurityConfig.class);

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            JpaRegisteredClientRepository registeredClientRepository,
            JpaOAuth2AuthorizationService authorizationService,
            PasswordEncoder passwordEncoder,
            JwtGenerator jwtGenerator,
            AuthorizationServerSettings authorizationServerSettings
    ) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http
                //.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                //.securityMatcher("/oauth2/authorize**");
//                .securityMatcher(request -> {
//                    boolean matches = authorizationServerConfigurer.getEndpointsMatcher().matches(request);
//                    LOGGER.info("Запрос {} обрабатывается authorizationServerSecurityFilterChain: {}", request.getRequestURI(), matches);
//                    return matches;
//                })
                .securityMatcher(request -> {
                    LOGGER.info("Запрос: {}", request.getRequestURI());
                    LOGGER.info("Параметры: {}", request.getParameterMap().entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + String.join(", ", entry.getValue()))
                            .collect(Collectors.joining(", ")));
                    return authorizationServerConfigurer.getEndpointsMatcher().matches(request);
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/authorize").authenticated()
                        .anyRequest().permitAll()
                )
                .with(authorizationServerConfigurer, configurer -> {
                    configurer
                            .authorizationServerSettings(authorizationServerSettings)
                            .authorizationService(authorizationService)
                            .registeredClientRepository(registeredClientRepository)
                            .tokenGenerator(jwtGenerator)
                            .oidc(oidc -> oidc
                                    .providerConfigurationEndpoint(config ->
                                            config.providerConfigurationCustomizer(context -> {
                                                context.claim("scopes_supported", List.of(
                                                        "openid", "profile", "email", "read", "write"
                                                ));
                                            })
                                    )
                            )
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
                //.formLogin(Customizer.withDefaults())
                .exceptionHandling(exceptions ->
                        exceptions.accessDeniedHandler((request, response, accessDeniedException) -> {
                            LOGGER.error("Ошибка доступа: {}", accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                        })
                )
                .requestCache(requestCache -> requestCache
                        .requestCache(new HttpSessionRequestCache())
                )
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers("/oauth2/revoke"));

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
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .requestCache(requestCache -> requestCache
                        .requestCache(new HttpSessionRequestCache()) // 🔧 тоже важно!
                )
                .exceptionHandling(exceptions ->
                exceptions.accessDeniedHandler((request, response, accessDeniedException) -> {
                    LOGGER.error("Ошибка доступа: {}", accessDeniedException.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                })
        );
        return http.build();
    }

//    @Bean
//    public AuthenticationProvider clientAuthenticationProvider(
//            JpaRegisteredClientRepository registeredClientRepository,
//            JpaOAuth2AuthorizationService authorizationService,
//            PasswordEncoder passwordEncoder
//    ) {
//        ClientSecretAuthenticationProvider provider =
//                new ClientSecretAuthenticationProvider(registeredClientRepository, authorizationService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }

    @Bean
    public AuthenticationProvider clientAuthenticationProvider(
            JpaRegisteredClientRepository registeredClientRepository,
            JpaOAuth2AuthorizationService authorizationService,
            PasswordEncoder passwordEncoder
    ) {
        ClientSecretAuthenticationProvider baseProvider =
                new ClientSecretAuthenticationProvider(registeredClientRepository, authorizationService);
        baseProvider.setPasswordEncoder(passwordEncoder);

        return new LoggingClientAuthenticationProvider(baseProvider);
    }

//    @Bean
//    public AuthenticationProvider userAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }

    @Bean
    public AuthenticationProvider userAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new CustomDaoAuthenticationProvider(userDetailsService, passwordEncoder);
    }
}
