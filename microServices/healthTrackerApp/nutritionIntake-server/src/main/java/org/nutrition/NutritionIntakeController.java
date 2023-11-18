package org.nutrition;

import java.math.BigDecimal;
import java.util.List;

import org.nutrition.exceptions.NutritionCreateException;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.ErrorSingleResponse;
import org.nutrition.model.dtos.NutritionIntakeView;
import org.nutrition.model.enums.Gender;
import org.nutrition.model.enums.WorkoutState;
import org.nutrition.services.NutrientIntakeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/api/nutritionIntake", headers = "X-ViewUser")
public class NutritionIntakeController {

    private final NutrientIntakeService nutrientIntakeService;

    @GetMapping
    public ResponseEntity<List<NutritionIntakeView>> getAllNutritionByRecord(@RequestParam Long recordId)
            throws RecordNotFoundException {

        List<NutritionIntakeView> intakeViews = nutrientIntakeService.getAllNutritionIntakeByRecordId(recordId);

        return new ResponseEntity<>(intakeViews, HttpStatusCode.valueOf(200));
    }

    @PostMapping
    public ResponseEntity<List<NutritionIntakeView>> createNutritionIntake(
            @RequestParam (required = true) Long recordId,
            @RequestParam (required = true) Gender gender,
            @RequestParam (required = true) BigDecimal caloriesPerDay,
            @RequestParam (required = true) WorkoutState workoutState) throws NutritionCreateException {
        List<NutritionIntakeView> intakeView = nutrientIntakeService.createNutritionIntake(recordId, gender,
                caloriesPerDay, workoutState);
        return new ResponseEntity<>(intakeView, HttpStatus.CREATED);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorSingleResponse> catchRecordNotFoundException(RecordNotFoundException e) {
        return new ResponseEntity<>(new ErrorSingleResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NutritionCreateException.class)
    public ResponseEntity<ErrorSingleResponse> catchNutritionCreateException(NutritionCreateException e) {
        return new ResponseEntity<>(new ErrorSingleResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
