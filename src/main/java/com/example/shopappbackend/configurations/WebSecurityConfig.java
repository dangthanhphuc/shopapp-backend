package com.example.shopappbackend.configurations;

import com.example.shopappbackend.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableWebMvc
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    // Dùng để kiểm tra quyền người dùng có được gọi controller không ?
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // có thể không cần tạo requestMatchers
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            ).permitAll()
                            .requestMatchers(
                                    GET, String.format("%s/categories/**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(
                                    GET, String.format("%s/materials/**", apiPrefix)
                            ).permitAll()
                            .requestMatchers(
                                    GET, String.format("%s/get-order-by-keywords", apiPrefix)
                            ).permitAll()
                            .anyRequest()
                            .authenticated();

                })
                .csrf(AbstractHttpConfigurer::disable);
        http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));
        return http.build();
    }
}
