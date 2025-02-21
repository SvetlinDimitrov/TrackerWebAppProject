package org.gateway.filter;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.user.dto.UserView;
import org.example.util.GsonWrapper;
import org.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class MainFilter {

  protected final GsonWrapper gson;
  protected final JwtUtil jwtUtil;

  protected Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
    log.info("Unauthorized access attempt: {}", exchange.getRequest().getURI());

    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
        "Your authorization token is either invalid or expired. Please provide a valid token.");
    problemDetail.setTitle("Unauthorized");

    byte[] bytes = gson.toJson(problemDetail).getBytes();
    return exchange.getResponse()
        .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
  }

  protected Mono<Void> forbiddenResponse(ServerWebExchange exchange) {
    log.info("Forbidden access attempt: {}", exchange.getRequest().getURI());

    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
        "You are forbidden to perform this action.");
    problemDetail.setTitle("Forbidden");

    byte[] bytes = gson.toJson(problemDetail).getBytes();
    return exchange.getResponse()
        .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
  }

  protected Mono<Void> filterWithUserHeader(ServerWebExchange exchange, GatewayFilterChain chain) {
    Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

    if (user.isEmpty()) {
      return unauthorizedResponse(exchange);
    }

    log.info("Filtering request with user header: {}", exchange.getRequest().getURI());

    ServerHttpRequest request = exchange
        .getRequest()
        .mutate()
        .header("X-ViewUser", gson.toJson(user))
        .build();

    return chain.filter(exchange.mutate().request(request).build());
  }


  protected String getPath(ServerWebExchange exchange) {
    return exchange.getRequest().getURI().getPath();
  }
}