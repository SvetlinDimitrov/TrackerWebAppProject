package org.gateway.filter.user;

import java.util.Optional;
import org.example.domain.user.dto.UserView;
import org.example.util.GsonWrapper;
import org.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserMeFilter extends MainUserFilter implements GlobalFilter {

  public UserMeFilter(GsonWrapper gson, JwtUtil jwtUtil) {
    super(gson, jwtUtil);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    String path = exchange.getRequest().getURI().getPath();

    if (path.equals(USER_PATHS.ME.path) && exchange.getRequest().getMethod().name().equals("GET")) {
      return filterWithUserHeader(exchange, chain);
    }

    return chain.filter(exchange);
  }
}
