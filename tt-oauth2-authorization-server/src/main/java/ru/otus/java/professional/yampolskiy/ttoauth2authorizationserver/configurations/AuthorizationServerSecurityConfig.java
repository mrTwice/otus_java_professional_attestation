package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.configurations;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.repositories.JpaRegisteredClientRepository;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.CustomDaoAuthenticationProvider;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.JpaOAuth2AuthorizationService;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.LoggingClientAuthenticationProvider;
import ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.services.LoggingOidcUserInfoAuthenticationProvider;

import java.io.IOException;
import java.util.stream.Collectors;


@Configuration
public class AuthorizationServerSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServerSecurityConfig.class);


    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            JpaOAuth2AuthorizationService authorizationService,
            AuthenticationProvider loggingOidcUserInfoAuthenticationProvider,
            AuthenticationProvider clientAuthenticationProvider
    ) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .authenticationProvider(clientAuthenticationProvider)
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.authorizationRequestConverter(new LoggingOAuth2AuthorizationCodeRequestConverter())
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
                .addFilterAfter(jwtDebugLogger(), BearerTokenAuthenticationFilter.class)

                .oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(Customizer.withDefaults())
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
        http.addFilterBefore(new CustomOAuth2TokenEndpointFilter(), UsernamePasswordAuthenticationFilter.class);

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
    public Filter jwtDebugLogger() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                if (request.getRequestURI().equals("/userinfo")) {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    LOGGER.info("🛡️ JWT Authentication for /userinfo: {}", auth);
                }
                try {
                    filterChain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        };
    }

    @Bean
    public AuthenticationProvider loggingOidcUserInfoAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            CustomOidcUserInfoMapper userInfoMapper
    ) {
        OidcUserInfoAuthenticationProvider baseProvider = new OidcUserInfoAuthenticationProvider(authorizationService);
        userInfoMapper.configureUserInfoMapper(baseProvider);
        return new LoggingOidcUserInfoAuthenticationProvider(baseProvider);
    }

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


    @Bean
    public AuthenticationProvider userAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new CustomDaoAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public FilterRegistrationBean<Filter> loggingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                String uri = request.getRequestURI();
                String method = request.getMethod();

                LOGGER.info("📥 [LOGGING FILTER] Запрос: {}", uri);
                LOGGER.info("➡️ Метод: {}, Параметры: {}", method, request.getParameterMap().entrySet().stream()
                        .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                        .collect(Collectors.joining(", ")));

                String authHeader = request.getHeader("Authorization");
                if (authHeader != null) {
                    LOGGER.info("🔑 Authorization: {}", authHeader);
                } else {
                    LOGGER.warn("⚠️ Authorization header отсутствует");
                }

                try {
                    filterChain.doFilter(request, response);
                } catch (IOException | ServletException e){
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });

        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
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
