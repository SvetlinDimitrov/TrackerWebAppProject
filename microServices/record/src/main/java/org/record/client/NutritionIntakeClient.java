package org.record.client;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(name = "${clients.nutritionIntake.name}")
public interface NutritionIntakeClient {

    @GetMapping(path = "${clients.nutritionIntake.retrieve-data.url}")
    List<NutritionIntakeView> getAllNutritionIntakesWithRecordId(@PathVariable("recordId") Long recordId);

    @PostMapping(path = "${clients.nutritionIntake.retrieve-data.url}")
    List<NutritionIntakeView> createNutritionIntakesWithRecordId(@PathVariable("recordId") Long recordId ,
                                                                 @RequestBody NutritionIntakeCreateDto nutritionIntakeCreateDto);

    @DeleteMapping(path = "${clients.nutritionIntake.retrieve-data.url}")
    List<NutritionIntakeView> deleteNutritionIntakesWithRecordId(@PathVariable("recordId") Long recordId);

}
