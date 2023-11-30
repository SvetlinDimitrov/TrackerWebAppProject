package org.record.utils;

import java.util.Arrays;

import org.record.enums.NonAuthServices;
import org.springframework.web.server.ServerWebExchange;

public class PathChecker {

    public static boolean nonAuthServices(ServerWebExchange exchange) {
        if (getExchangePath(exchange).contains("food/customFood")) {
            return false;
        }

        return Arrays.stream(NonAuthServices.values())
                .map(NonAuthServices::name)
                .anyMatch(paths -> getExchangePath(exchange).contains(paths));
    }

    public static String getExchangePath(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().toString();
    }

}
