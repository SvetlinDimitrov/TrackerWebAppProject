package org.record.config;

import java.util.List;

import org.record.filter.AuthenticationFilter;
import org.record.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import com.google.gson.Gson;

@Configuration
public class GatewayConfig {

    @Value("${client.host}")
    private String clientHost;

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    GlobalFilter customGlobalFilter() {
        return new AuthenticationFilter(jwtUtil, gson());
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        String hosts = String.format("http://%s:[*]", clientHost);
        corsConfig.setAllowedOriginPatterns(List.of(hosts));
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

    @Bean
    Gson gson() {
        return new Gson();
    }
}
