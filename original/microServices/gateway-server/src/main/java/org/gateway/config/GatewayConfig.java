package org.gateway.config;

import java.util.List;

import org.gateway.GateWayBlockedUsers;
import org.gateway.filter.AuthenticationFilter;
import org.gateway.utils.GsonWrapper;
import org.gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;


@Configuration
public class GatewayConfig {

    private String clientHost = "localhost";

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private GsonWrapper gson;
    @Autowired
    private GateWayBlockedUsers blockedUsers;

    @Bean
    GlobalFilter customGlobalFilter() {
        return new AuthenticationFilter(gson , jwtUtil , blockedUsers);
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        String hosts = String.format("http://%s:[*]" , clientHost);
        corsConfig.setAllowedOriginPatterns(List.of(hosts));
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
