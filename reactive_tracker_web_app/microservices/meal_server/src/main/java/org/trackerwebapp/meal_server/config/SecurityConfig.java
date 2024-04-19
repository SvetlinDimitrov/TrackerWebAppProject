package org.trackerwebapp.meal_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.trackerwebapp.meal_server.config.jwt.JwtTokenProvider;
import org.trackerwebapp.meal_server.config.jwt.JwtTokenAuthenticationFilter;


@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http,
      JwtTokenProvider tokenProvider) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .cors(ServerHttpSecurity.CorsSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(request -> request.anyExchange().authenticated())
        .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider),
            SecurityWebFiltersOrder.HTTP_BASIC)
        .build();
  }
}
