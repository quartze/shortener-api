package com.quartze.shortenerurl.config;

import com.quartze.shortenerurl.filters.JWTValidationTokenFilter;
import com.quartze.shortenerurl.helpers.CustomAccessDeniedHandler;
import com.quartze.shortenerurl.helpers.CustomUnauthorizedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecuritySpringConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(req -> {
            req.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll();
            req.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll();
            req.requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll();
            req.requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated();

            req.requestMatchers(HttpMethod.POST, "/api/shorts").permitAll();
            req.requestMatchers(HttpMethod.GET, "/api/shorts/**").permitAll();
            req.requestMatchers(HttpMethod.GET, "/api/shorts").authenticated();
            req.requestMatchers(HttpMethod.DELETE, "/api/shorts/**").authenticated();

            req.requestMatchers(HttpMethod.GET, "/api/csrf").permitAll();

            req.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll();

            req.anyRequest().denyAll();
        });

        http.httpBasic(HttpBasicConfigurer::disable);
        http.formLogin(FormLoginConfigurer::disable);

        http.cors(cors -> cors.configurationSource(new CorsSettings()));
        http.csrf(CsrfConfigurer::disable);

        http.exceptionHandling(ex -> {
           ex.authenticationEntryPoint(new CustomUnauthorizedHandler());
           ex.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        http.addFilterBefore(new JWTValidationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
