package org.record.client;

import java.math.BigDecimal;
import java.util.List;

import org.record.client.dto.NutritionIntakeView;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "nutritionIntake")
public interface NutritionIntakeClient {

    @GetMapping(path = "/api/nutritionIntake")
    List<NutritionIntakeView> getAllNutritionIntakesWithRecordId(@RequestParam("recordId") Long recordId,
            @RequestHeader("X-ViewUser") String userToken);

    @PostMapping(path = "/api/nutritionIntake")
    ResponseEntity<List<NutritionIntakeView>> createNutritionIntake(
            @RequestParam("recordId") Long recordId,
            @RequestParam("gender") Gender gender,
            @RequestParam("caloriesPerDay") BigDecimal caloriesPerDay,
            @RequestParam("workoutState") WorkoutState workoutState, 
            @RequestHeader("X-ViewUser") String userToken);

}
