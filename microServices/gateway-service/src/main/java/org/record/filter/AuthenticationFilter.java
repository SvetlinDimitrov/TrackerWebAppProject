package org.record.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${properties.jwt-secret-keyWord}")
    private String secret;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String errorMessage = isAuthenticated(exchange);

        if (errorMessage == null || isAuthRequested(exchange)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange
                    .getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(errorMessage.getBytes())));
        }
    }

    private boolean isAuthRequested(ServerWebExchange exchange) {
        return Stream.of("/auth/login", "/auth/register")
                .anyMatch(path -> path.equals(exchange.getRequest().getPath().toString())) &&
                exchange.getRequest().getMethod().equals(HttpMethod.POST);
    }

    private String isAuthenticated(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {

            String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            if(token != null && token.startsWith("Bearer ")){

                token = token.substring(7);
                try {
                    verifyToken(token);
                    return null;
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
        }
        return "You are not authenticated .Go to /auth/register and /auth/login and get your token";
    }

    private void verifyToken(String token) {
        JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }
}