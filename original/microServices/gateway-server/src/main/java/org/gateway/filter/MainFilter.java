package org.gateway.filter;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.shared.EndpointPermission;
import org.example.domain.shared.HttpMethod;
import org.example.domain.shared.UserAccess;
import org.example.domain.user.dto.UserView;
import org.example.util.GsonWrapper;
import org.gateway.utils.HttpResponseUtil;
import org.gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public abstract class MainFilter implements GlobalFilter {

  protected JwtUtil jwtUtil;

  protected abstract List<EndpointPermission> getPermissions();

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = getPath(exchange);
    org.springframework.http.HttpMethod requestMethod = exchange.getRequest().getMethod();

    if (!path.startsWith(getBasePath())) {
      return chain.filter(exchange);
    }

    HttpMethod customMethod = HttpMethod.valueOf(requestMethod.name());

    Optional<EndpointPermission> pathPermission = getPermissions().stream()
        .filter(permission -> permissionMatchesPath(path, customMethod, permission))
        .findFirst();

    if (pathPermission.isEmpty()) {
      return notFoundResponse(exchange);
    }

    EndpointPermission endpointPermission = pathPermission.get();

    if (endpointPermission.access() == UserAccess.AUTH) {
      return filterWithUserHeader(exchange, chain);
    }

    return chain.filter(exchange);
  }

  protected abstract String getBasePath();

  private Mono<Void> forbiddenResponse(ServerWebExchange exchange) {
    log.info("Forbidden access attempt: {}", exchange.getRequest().getURI());

    ProblemDetail problemDetail = HttpResponseUtil.createProblemDetail(
        HttpStatus.FORBIDDEN, "Forbidden",
        "You are forbidden to perform this action."
    );

    return HttpResponseUtil.writeJsonResponse(exchange, HttpStatus.FORBIDDEN, problemDetail);
  }

  private Mono<Void> notFoundResponse(ServerWebExchange exchange) {
    log.info("Not Found access attempt: {}", exchange.getRequest().getURI());

    ProblemDetail problemDetail = HttpResponseUtil.createProblemDetail(
        HttpStatus.NOT_FOUND, "Not Found",
        "The resource you are looking for might have been removed, had its name changed, or is temporarily unavailable."
    );

    return HttpResponseUtil.writeJsonResponse(exchange, HttpStatus.NOT_FOUND, problemDetail);
  }

  private Mono<Void> filterWithUserHeader(ServerWebExchange exchange, GatewayFilterChain chain) {
    Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

    if (user.isEmpty()) {
      return forbiddenResponse(exchange);
    }

    log.info("Filtering request with user header: {}", exchange.getRequest().getURI());

    ServerHttpRequest request = exchange
        .getRequest()
        .mutate()
        .header("X-ViewUser", new GsonWrapper().toJson(user.get()))
        .build();

    return chain.filter(exchange.mutate().request(request).build());
  }

  private boolean permissionMatchesPath(String path, HttpMethod method, EndpointPermission permission) {
    String fullPath = getBasePath().concat(permission.path());

    // Define possible UUID patterns
    String hyphenatedUuidPattern = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    String nonHyphenatedUuidPattern = "[a-fA-F0-9]{24}";

    // Combined pattern to match either hyphenated or non-hyphenated UUID
    String combinedUuidPattern = String.format("(%s|%s)", hyphenatedUuidPattern, nonHyphenatedUuidPattern);

    // Replace the path variable placeholder with the UUID pattern
    String pattern = fullPath.replaceAll("\\{[^/]+}", combinedUuidPattern);

    return path.matches(pattern) && method == permission.method();
  }

  private String getPath(ServerWebExchange exchange) {
    return exchange.getRequest().getURI().getPath();
  }

  @Autowired
  public void setJwtUtil(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }
}