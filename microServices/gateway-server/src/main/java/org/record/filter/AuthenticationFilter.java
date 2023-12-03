package org.record.filter;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.record.enums.NotCompletedUserDetailsServices;
import org.record.model.UserView;
import org.record.utils.JwtUtil;
import org.record.utils.PathChecker;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final Gson gson;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (!PathChecker.halfAuthServices(exchange) && PathChecker.nonAuthServices(exchange)) {
            return chain.filter(exchange);
        } else {
            Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

            if (user.isEmpty() || (user.get().getUserDetails().equals("NOT_COMPLETED") && !PathChecker.halfAuthServices(exchange))) {
                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange
                        .getResponse()
                        .writeWith(Mono
                                .just(exchange.getResponse().bufferFactory()
                                        .wrap("Your authorization token is either invalid or expired. Please provide a valid token."
                                                .getBytes())));
            }

            ServerHttpRequest request = exchange
                    .getRequest()
                    .mutate()
                    .header("X-ViewUser", gson.toJson(user.get()))
                    .build();

            return chain.filter(exchange.mutate().request(request).build());
        }

    }
}