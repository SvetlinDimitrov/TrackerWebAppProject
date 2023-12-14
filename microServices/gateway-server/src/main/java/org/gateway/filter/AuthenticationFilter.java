package org.gateway.filter;

import java.util.Optional;

import org.gateway.GateWayBlockedUsers;
import org.gateway.model.UserView;
import org.gateway.utils.GsonWrapper;
import org.gateway.utils.JwtUtil;
import org.gateway.utils.PathChecker;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final GsonWrapper gson;
    private final JwtUtil jwtUtil;
    private final GateWayBlockedUsers blockedUsers;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (PathChecker.nonAuthServices(exchange)) {
            return chain.filter(exchange);
        } else {

            Optional<UserView> user = jwtUtil.verifyAndExtractUser(exchange.getRequest().getHeaders());

            if (user.isEmpty() || blockedUsers.isUserBlocked(user.get())
                    || (user.get().getUserDetails().equals("NOT_COMPLETED")
                            && !PathChecker.halfAuthServices(exchange))) {
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