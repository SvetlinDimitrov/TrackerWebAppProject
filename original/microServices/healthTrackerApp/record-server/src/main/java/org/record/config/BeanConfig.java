package org.record.config;

import org.record.utils.GsonWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    GsonWrapper gsonWrapper() {
        return new GsonWrapper();
    }

}
