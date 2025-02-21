package org.gateway.filter.record;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.util.Optional;
import org.example.domain.record.paths.RecordPaths;
import org.example.domain.user.dto.UserView;
import org.example.util.GsonWrapper;
import org.gateway.filter.MainFilter;
import org.gateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RecordControllerFilter extends MainFilter implements GlobalFilter {

  public RecordControllerFilter(GsonWrapper gson, JwtUtil jwtUtil) {
    super(gson, jwtUtil);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = getPath(exchange);
    HttpMethod method = exchange.getRequest().getMethod();

    if (path.contains(RecordPaths.BASE)) {
      if (GET.equals(method)) {
        return handleGet(path, exchange, chain);
      } else if (POST.equals(method)) {
        return handlePost(path, exchange, chain);
      } else if (DELETE.equals(method)) {
        return handleDelete(path, exchange, chain);
      }
    }

    return chain.filter(exchange);
  }

  private Mono<Void> handleGet(String path, ServerWebExchange exchange, GatewayFilterChain chain) {
    if (path.equals(RecordPaths.GET_ALL) || path.matches(RecordPaths.GET_BY_ID)) {
      return filterWithUserHeader(exchange, chain);
    }
    return chain.filter(exchange);
  }

  private Mono<Void> handlePost(String path, ServerWebExchange exchange, GatewayFilterChain chain) {
    if (path.equals(RecordPaths.CREATE)) {
      Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

      if (user.isPresent() && hasAllFields(user.get())) {
        return filterWithUserHeader(exchange, chain);
      }
      return forbiddenResponse(exchange);
    }
    return chain.filter(exchange);
  }

  private Mono<Void> handleDelete(String path, ServerWebExchange exchange,
      GatewayFilterChain chain) {
    if (path.matches(RecordPaths.DELETE)) {
      return filterWithUserHeader(exchange, chain);
    }
    return chain.filter(exchange);
  }
}