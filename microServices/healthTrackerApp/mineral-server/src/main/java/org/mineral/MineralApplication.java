package org.mineral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MineralApplication {
    public static void main(String[] args) {
        SpringApplication.run(MineralApplication.class,args);

    }
}