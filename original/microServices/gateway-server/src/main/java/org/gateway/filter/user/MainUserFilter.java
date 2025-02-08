package org.gateway.filter.user;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.user.UserView;
import org.example.domain.user.enums.UserRole;
import org.example.util.GsonWrapper;
import org.gateway.filter.MainFilter;
import org.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public abstract class MainUserFilter extends MainFilter {

  public MainUserFilter(GsonWrapper gson, JwtUtil jwtUtil) {
    super(gson, jwtUtil);
  }

  protected Mono<Void> checkIfUserIsTheOwnerOrAdmin(ServerWebExchange exchange,
      GatewayFilterChain chain,
      String path) {
    Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

    if (user.isEmpty()) {
      return unauthorizedResponse(exchange);
    }

    var currentUser = user.get();
    String userId = path.substring(path.lastIndexOf("/") + 1);

    if (!currentUser.id().equals(userId) && UserRole.ADMIN != currentUser.role()) {
      return forbiddenResponse(exchange);
    }

    return filterWithUserHeader(exchange, chain, currentUser);
  }
}
