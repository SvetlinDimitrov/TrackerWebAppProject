package org.trackerwebapp.user_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.trackerwebapp.user_server.config.jwt.JwtTokenAuthenticationFilter;
import org.trackerwebapp.user_server.config.jwt.JwtTokenProvider;

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
        .authorizeExchange(request ->
            request.pathMatchers("/api/user/login").permitAll()
                .anyExchange().authenticated()
        )
        .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider),
            SecurityWebFiltersOrder.HTTP_BASIC)
        .build();
  }
}
