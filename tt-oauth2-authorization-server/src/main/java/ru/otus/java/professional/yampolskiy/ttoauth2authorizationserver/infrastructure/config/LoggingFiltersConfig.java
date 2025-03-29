package ru.otus.java.professional.yampolskiy.ttoauth2authorizationserver.infrastructure.config;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
public class LoggingFiltersConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFiltersConfig.class);

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
}
