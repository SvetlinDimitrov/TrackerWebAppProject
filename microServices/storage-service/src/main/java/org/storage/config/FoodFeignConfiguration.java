package org.storage.config;

import org.springframework.context.annotation.Bean;
import org.storage.client.FoodErrorDecoder;

import feign.codec.ErrorDecoder;

public class FoodFeignConfiguration {

    @Bean
    ErrorDecoder errorDecoder() {
        return new FoodErrorDecoder();
    }
}