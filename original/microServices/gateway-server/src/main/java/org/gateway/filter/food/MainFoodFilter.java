package org.gateway.filter.food;

import static org.gateway.filter.food.FOOD_PATHS.CUSTOM_FOODS;

import java.util.Optional;
import org.example.domain.user.UserView;
import org.example.util.GsonWrapper;
import org.gateway.filter.MainFilter;
import org.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MainFoodFilter extends MainFilter implements GlobalFilter {

  public MainFoodFilter(GsonWrapper gson, JwtUtil jwtUtil) {
    super(gson, jwtUtil);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();

    if (path.contains(CUSTOM_FOODS.path)) {
      Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

      if (user.isEmpty()) {
        return unauthorizedResponse(exchange);
      }

      return filterWithUserHeader(exchange, chain, user.get());
    }

    return chain.filter(exchange);
  }
}
