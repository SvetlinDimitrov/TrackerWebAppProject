package org.record.filter;

import com.auth0.jwt.interfaces.Claim;
import com.google.gson.Gson;
import org.record.config.JwtUtil;
import org.record.model.UserView;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (isAuthRequested(exchange) || nonAuthServices(exchange)) {
            return chain.filter(exchange);
        } else {
            try {
                Map<String, Claim> claimMap = new JwtUtil().extractAndVerifyToken(exchange.getRequest().getHeaders());

                UserView user = getUserViewFromJwtToken(claimMap);

                if ((claimMap.get("userDetails").asString().equals("NOT_COMPLETED") &&
                        (exchange.getRequest().getURI().getPath().contains("nutrition") || exchange.getRequest().getURI().getPath().contains("record")))) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap("You must be fully registered".getBytes())));
                }

                Gson gson = new Gson();

                ServerHttpRequest request = exchange.getRequest().mutate().header("X-ViewUser", gson.toJson(user)).build();

                return chain.filter(exchange.mutate().request(request).build());
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(e.getMessage() != null ? e.getMessage().getBytes() : "Something went wrong".getBytes())));
            }

        }
    }

    private UserView getUserViewFromJwtToken(Map<String, Claim> claimMap) {
        return UserView.builder().id(claimMap.get("id").asLong()).username(claimMap.get("username").asString()).email(claimMap.get("email").asString()).kilograms(claimMap.get("kilograms").asString()).height(claimMap.get("height").asString()).workoutState(claimMap.get("workoutState").asString()).gender(claimMap.get("gender").asString()).userDetails(claimMap.get("userDetails").asString()).age(claimMap.get("age").asInt()).build();
    }

    private boolean isAuthRequested(ServerWebExchange exchange) {
        return Stream.of("/auth/login", "/auth/register").anyMatch(path -> path.equals(exchange.getRequest().getPath().toString())) && exchange.getRequest().getMethod().equals(HttpMethod.POST);
    }
    private boolean nonAuthServices(ServerWebExchange exchange) {
        return Stream.of("vitamin" , "macronutrient" , "electrolyte")
                .anyMatch(paths -> (exchange.getRequest().getPath().toString()).contains(paths));
    }
}