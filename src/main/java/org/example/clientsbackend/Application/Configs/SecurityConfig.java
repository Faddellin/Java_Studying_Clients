package org.example.clientsbackend.Application.Configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.clientsbackend.Application.Filters.JwtSecurityFilter;
import org.example.clientsbackend.Application.Filters.RequestLoggingFilter;
import org.example.clientsbackend.Application.Models.Common.ResponseModel;
import org.example.clientsbackend.Domain.Enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Dictionary;
import java.util.Hashtable;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtSecurityFilter jwtSecurityFilter;
    private final RequestLoggingFilter requestLoggingFilter;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            Dictionary<String, String> errors = new Hashtable<>();
            errors.put("Authentication", authException.getMessage());
            String responseBody = new ObjectMapper().writeValueAsString(
                    ResponseModel
                            .builder()
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .errors(errors)
                            .build()
            );

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(responseBody);
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            Dictionary<String, String> errors = new Hashtable<>();
            errors.put("Authorization", accessDeniedException.getMessage());
            String responseBody = new ObjectMapper().writeValueAsString(
                    ResponseModel
                            .builder()
                            .statusCode(HttpStatus.FORBIDDEN.value())
                            .errors(errors)
                            .build()
            );

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(responseBody);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/clients/**")
                        .hasAnyRole(UserRole.ADMIN.toString(), UserRole.MANAGER.toString())
                        .requestMatchers(
                                "/managers/**",
                                "/reports/**")
                        .hasRole(UserRole.ADMIN.toString())
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandlingCustomizer ->
                    {
                        exceptionHandlingCustomizer.authenticationEntryPoint(authenticationEntryPoint());
                        exceptionHandlingCustomizer.accessDeniedHandler(accessDeniedHandler());
                    }
                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(requestLoggingFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
