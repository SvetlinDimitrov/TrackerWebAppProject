package org.storage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.storage.model.entity.Food;

@Component
@FeignClient(name = "food")
public interface FoodClient {

    @GetMapping(path = "/api/food/{name}")
    Food getFoodByName(@PathVariable("name") String name);

    @GetMapping(path = "/api/food/customFood")
    Food getCustomFoodByNameAndSize(
            @RequestParam(name = "foodName") String name,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader(name = "X-ViewUser") String userToken);

}
