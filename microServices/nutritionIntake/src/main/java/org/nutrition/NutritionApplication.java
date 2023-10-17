package org.nutrition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NutritionApplication {
    public static void main(String[] args) {
        SpringApplication.run(NutritionApplication.class , args);
    }
}