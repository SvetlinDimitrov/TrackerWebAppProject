package org.electrolyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ElectrolyteApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElectrolyteApplication.class,args);
    }
}