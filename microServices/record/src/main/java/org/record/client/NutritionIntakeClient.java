package org.record.client;

import java.util.List;

import org.record.client.dto.NutritionIntakeView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "${clients.nutritionIntake.name}")
public interface NutritionIntakeClient {

    @GetMapping(path = "${clients.nutritionIntake.retrieve-data.url}")
    List<NutritionIntakeView> getAllNutritionIntakesWithRecordId(@RequestParam("recordId") Long recordId,
            @RequestHeader("X-ViewUser") String userToken);

}
