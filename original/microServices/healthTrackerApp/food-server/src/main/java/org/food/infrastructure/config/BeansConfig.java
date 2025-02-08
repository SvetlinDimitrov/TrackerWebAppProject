package org.food.infrastructure.config;

import org.example.util.GsonWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    GsonWrapper gsonWrapper() {
        return new GsonWrapper();
    }
}
