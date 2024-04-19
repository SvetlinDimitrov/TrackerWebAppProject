package org.auth.config;

import org.auth.util.GsonWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    GsonWrapper gsonWrapper() {
        return new GsonWrapper();
    }
}
