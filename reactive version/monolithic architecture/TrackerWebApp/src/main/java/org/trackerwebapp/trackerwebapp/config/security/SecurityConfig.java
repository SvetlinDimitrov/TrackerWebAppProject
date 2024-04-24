package org.trackerwebapp.trackerwebapp.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final ReactiveUserDetailsServiceImp reactiveUserDetailsServiceImp;

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(ServerHttpSecurity.CorsSpec::disable)
        .httpBasic(Customizer.withDefaults())
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .authorizeExchange(request ->
            request
                .pathMatchers(HttpMethod.POST, "/api/user").permitAll()
                .pathMatchers("/api/meals/**").hasRole("FULLY_REGISTERED")
                .pathMatchers("/api/record/**").hasRole("FULLY_REGISTERED")
                .anyExchange().authenticated()
        )
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ReactiveUserDetailsService reactiveUserDetailsService() {
    return reactiveUserDetailsServiceImp;
  }
}

