package org.gateway.utils;

import java.util.Arrays;

import org.gateway.enums.NotCompletedUserDetailsServices;
import org.springframework.web.server.ServerWebExchange;

public class PathChecker {

    public static boolean authPath(ServerWebExchange exchange) {
        return getExchangePath(exchange).contains("auth");
    }

    public static boolean halfAuthServices(ServerWebExchange exchange) {
        return Arrays.stream(NotCompletedUserDetailsServices.values())
                .map(NotCompletedUserDetailsServices::name)
                .anyMatch(paths -> getExchangePath(exchange).contains(paths));
    }

    public static String getExchangePath(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().toString();
    }

}
