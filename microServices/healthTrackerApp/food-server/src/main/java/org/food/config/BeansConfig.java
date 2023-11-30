package org.food.config;

import org.food.utils.GsonWrapper;
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
