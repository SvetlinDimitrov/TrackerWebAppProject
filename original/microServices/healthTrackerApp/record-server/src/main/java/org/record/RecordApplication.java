package org.record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class RecordApplication {
    //TODO:: More things to do
    public static void main(String[] args) {
        SpringApplication.run(RecordApplication.class,args);
    }
}