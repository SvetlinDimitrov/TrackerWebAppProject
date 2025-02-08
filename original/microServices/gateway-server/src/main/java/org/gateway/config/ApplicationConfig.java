package org.gateway.config;

import lombok.RequiredArgsConstructor;
import org.example.util.GsonWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  @Bean
  GsonWrapper gsonWrapper() {
    return new GsonWrapper();
  }
}

