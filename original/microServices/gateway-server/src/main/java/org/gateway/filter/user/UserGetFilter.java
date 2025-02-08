package org.gateway.filter.user;

import org.example.util.GsonWrapper;
import org.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserGetFilter extends MainUserFilter implements GlobalFilter {

  public UserGetFilter(GsonWrapper gson, JwtUtil jwtUtil) {
    super(gson, jwtUtil);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();

    if (path.matches(USER_PATHS.GET.path.replace("{id}", "[^/]+")) &&
        exchange.getRequest().getMethod().name().equals("GET")) {
      return checkIfUserIsTheOwnerOrAdmin(exchange, chain, path);
    }

    return chain.filter(exchange);
  }
}