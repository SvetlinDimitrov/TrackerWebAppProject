package org.record.filter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (isAuthenticated(exchange)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isAuthenticated(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        if(headers.containsKey(HttpHeaders.AUTHORIZATION)){

            String token = headers.get(HttpHeaders.AUTHORIZATION).get(0);

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try{
                    JWT.require(Algorithm.HMAC256("${properties.jwt-secret-keyWord}"))
                            .build()
                            .verify(token);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }
}