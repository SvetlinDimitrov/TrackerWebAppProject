package org.storage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.storage.config.FoodFeignConfiguration;

@Component
@FeignClient(name = "food" , configuration = FoodFeignConfiguration.class)
public interface FoodClient {

    @GetMapping(path = "/api/food/{name}")
    FoodView getFoodByName(@PathVariable("name") String name);

}
