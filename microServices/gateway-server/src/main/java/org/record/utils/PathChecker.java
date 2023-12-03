package org.record.utils;

import java.util.Arrays;

import org.record.enums.NonAuthServices;
import org.record.enums.NotCompletedUserDetailsServices;
import org.springframework.web.server.ServerWebExchange;

public class PathChecker {

    public static boolean nonAuthServices(ServerWebExchange exchange) {
        return Arrays.stream(NonAuthServices.values())
                .map(NonAuthServices::name)
                .anyMatch(paths -> getExchangePath(exchange).contains(paths));
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
