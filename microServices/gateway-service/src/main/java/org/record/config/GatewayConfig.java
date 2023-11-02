package org.record.config;

import org.record.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.List;

@Configuration
public class GatewayConfig {

    @Value("${client.host}")
    private String clientHost;

    @Bean
    public GlobalFilter customGlobalFilter() {
        return new AuthenticationFilter();
    }
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        String hosts = String.format("http://%s:[*]", clientHost);
        corsConfig.setAllowedOriginPatterns(List.of(hosts)); // Specify the specific origin you want to allow
        corsConfig.addAllowedMethod("*"); // You can specify specific HTTP methods
        corsConfig.addAllowedHeader("*"); // You can specify specific HTTP headers

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
