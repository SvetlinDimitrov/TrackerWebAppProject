package org.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.storage.utils.GsonWrapper;

@Configuration
public class BeansConfig {

    @Bean
    GsonWrapper gsonWrapper() {
        return new GsonWrapper();
    }
}
