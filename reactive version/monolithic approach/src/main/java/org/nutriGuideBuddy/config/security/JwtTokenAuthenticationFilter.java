package org.nutriGuideBuddy.config.security;

import lombok.RequiredArgsConstructor;
import org.nutriGuideBuddy.utils.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {

  public static final String HEADER_PREFIX = "Bearer ";

  private final JWTUtil tokenProvider;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String path = exchange.getRequest().getPath().value();
    HttpMethod method = exchange.getRequest().getMethod();

    Map<String, HttpMethod> excludedPaths = new HashMap<>();
    excludedPaths.put("/api/user", HttpMethod.POST);
    excludedPaths.put("/api/user/login", HttpMethod.POST);
    excludedPaths.put("/api/user/reset-password", HttpMethod.PATCH);
    excludedPaths.put("/api/verify", null);

    if (excludedPaths.entrySet().stream().anyMatch(entry ->
        path.startsWith(entry.getKey()) && (entry.getValue() == null || method == entry.getValue()))) {
      return chain.filter(exchange);
    }

    String token = resolveToken(exchange.getRequest());
    if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
      return Mono.fromCallable(() -> this.tokenProvider.getAuthentication(token))
          .subscribeOn(Schedulers.boundedElastic())
          .flatMap(authentication -> chain.filter(exchange)
              .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
          );
    } else {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  private String resolveToken(ServerHttpRequest request) {
    String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

}