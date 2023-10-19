package org.nutrition;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nutrition.exceptions.IncorrectNutrientChangeException;
import org.nutrition.exceptions.NutrientNameNotFoundException;
import org.nutrition.exceptions.NutritionCreateException;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.ErrorResponse;
import org.nutrition.model.dtos.NutritionIntakeChangeDto;
import org.nutrition.model.dtos.NutritionIntakeCreateDto;
import org.nutrition.model.dtos.NutritionIntakeView;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/nutritionIntake")
public class NutritionIntakeController {

    private final NutrientIntakeService nutrientIntakeService;

    @GetMapping("/record/{recordId}")
    public ResponseEntity<List<NutritionIntakeView>> getAllNutritionByRecord(@PathVariable Long recordId) throws RecordNotFoundException {

        List<NutritionIntakeView> intakeViews = nutrientIntakeService.getAllNutritionIntakeByRecordId(recordId);

        return new ResponseEntity<>(intakeViews, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/record/{recordId}")
    public ResponseEntity<List<NutritionIntakeView>> createNutritionIntakes(@PathVariable Long recordId,
                                                             @Valid @RequestBody NutritionIntakeCreateDto nutritionIntakeCreateDto,
                                                             BindingResult result) throws NutritionCreateException {
        if (result.hasErrors()) {
            throw new NutritionCreateException(result.getFieldErrors());
        }

        List<NutritionIntakeView> views = nutrientIntakeService.create(nutritionIntakeCreateDto, recordId);

        return new ResponseEntity<>(views,HttpStatusCode.valueOf(201));
    }

    @DeleteMapping("/record/{recordId}")
    public ResponseEntity<HttpStatus> deleteNutritionIntakes(@PathVariable Long recordId) throws RecordNotFoundException {

        nutrientIntakeService.deleteNutritionIntakesByRecordId(recordId);

        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }

    @PatchMapping("/record/{recordId}")
    public ResponseEntity<NutritionIntakeView> changeNutritionIntake(@PathVariable Long recordId,
                                                                     @Valid @RequestBody NutritionIntakeChangeDto changeDto,
                                                                     BindingResult result) throws IncorrectNutrientChangeException, NutrientNameNotFoundException {
        if(result.hasErrors()){
            throw new IncorrectNutrientChangeException(result.getFieldErrors());
        }

        NutritionIntakeView nutritionIntakeView = nutrientIntakeService.changeNutritionIntake(recordId , changeDto);

        return new ResponseEntity<>(nutritionIntakeView , HttpStatusCode.valueOf(200));

    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchRecordNotFoundException(RecordNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(List.of(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NutritionCreateException.class)
    public ResponseEntity<ErrorResponse> catchNutritionCreateException(NutritionCreateException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getErrors()), HttpStatus.BAD_REQUEST);
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
