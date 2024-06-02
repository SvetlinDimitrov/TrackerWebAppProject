package org.nutriGuideBuddy.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

  private final JwtTokenAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration corsConfiguration = new CorsConfiguration();
          corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
          corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
          corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "DNT", "X-CustomHeader", "Keep-Alive", "User-Agent", "X-Requested-With", "If-Modified-Since", "Cache-Control", "Content-Range", "Range"));
          corsConfiguration.setAllowCredentials(true);
          corsConfiguration.setMaxAge(1728000L);
          return corsConfiguration;
        }))
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(request ->
            request
                .pathMatchers(HttpMethod.POST, "/api/user" , "/api/user/login").permitAll()
                .pathMatchers("/api/user/reset-password").permitAll()
                .pathMatchers("/api/verify/**").permitAll()
                .pathMatchers("/api/meals/**").hasRole("FULLY_REGISTERED")
                .pathMatchers("/api/record/**").hasRole("FULLY_REGISTERED")
                .anyExchange().authenticated()
        )
        .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

