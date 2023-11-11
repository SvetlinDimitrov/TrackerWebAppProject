package org.record.client;

import java.util.List;

import org.record.client.dto.NutritionIntakeView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "${clients.nutritionIntake.name}")
public interface NutritionIntakeClient {

    @GetMapping(path = "${clients.nutritionIntake.retrieve-data.url}")
    List<NutritionIntakeView> getAllNutritionIntakesWithRecordId(@PathVariable("recordId") Long recordId);

}
