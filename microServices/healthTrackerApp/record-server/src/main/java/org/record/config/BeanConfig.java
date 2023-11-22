package org.record.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

@Configuration
public class BeanConfig {

    @Bean
    Gson getGson() {
        return new Gson();
    }

}
