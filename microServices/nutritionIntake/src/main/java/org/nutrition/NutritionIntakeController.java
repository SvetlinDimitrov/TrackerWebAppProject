package org.nutrition;

import java.util.List;

import org.nutrition.exceptions.IncorrectNutrientChangeException;
import org.nutrition.exceptions.NutrientNameNotFoundException;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.ErrorResponse;
import org.nutrition.model.dtos.NutritionIntakeView;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/nutritionIntake")
public class NutritionIntakeController {

    private final NutrientIntakeService nutrientIntakeService;

    @GetMapping("/{recordId}")
    public ResponseEntity<List<NutritionIntakeView>> getAllNutritionByRecord(@PathVariable Long recordId)
            throws RecordNotFoundException {

        List<NutritionIntakeView> intakeViews = nutrientIntakeService.getAllNutritionIntakeByRecordId(recordId);

        return new ResponseEntity<>(intakeViews, HttpStatusCode.valueOf(200));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchRecordNotFoundException(RecordNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(List.of(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectNutrientChangeException.class)
    public ResponseEntity<ErrorResponse> catchIncorrectNutrientChangeException(IncorrectNutrientChangeException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NutrientNameNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchNutrientNameNotFoundException(NutrientNameNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(List.of(e.getMessage())), HttpStatus.BAD_REQUEST);
    }
}
