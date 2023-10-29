package org.macronutrient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MacronutrientApplication {
    public static void main(String[] args) {

        SpringApplication.run(MacronutrientApplication.class , args);
    }
}