package org.gateway.filter;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.user.UserView;
import org.example.util.GsonWrapper;
import org.gateway.GateWayBlockedUsers;
import org.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockUserFilter implements GlobalFilter, Ordered {

  private final JwtUtil jwtUtil;
  private final GateWayBlockedUsers blockedUsers;
  private final GsonWrapper gson;

  @Override
  public int getOrder() {
    return -1;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

    if (user.isEmpty()) {
      return chain.filter(exchange);
    }

    if (blockedUsers.isUserBlocked(user.get())) {
      return blockedUserResponse(exchange);
    }

    return chain.filter(exchange);
  }

  private Mono<Void> blockedUserResponse(ServerWebExchange exchange) {
    log.info("Blocked user access attempt: {}", exchange.getRequest().getURI());

    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
        "Your account is blocked. Please contact support.");
    problemDetail.setTitle("Blocked");

    byte[] bytes = gson.toJson(problemDetail).getBytes();
    return exchange.getResponse()
        .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
  }
}
