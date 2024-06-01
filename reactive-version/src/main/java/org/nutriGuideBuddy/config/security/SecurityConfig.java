package org.nutriGuideBuddy.config.security;

import lombok.RequiredArgsConstructor;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("https://nutriguidebuddy.netlify.app"));
    corsConfig.addAllowedMethod("*");
    corsConfig.addAllowedHeader("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);
    return source;
  }
}

