package org.nutrivoltamin.controller;

import lombok.RequiredArgsConstructor;
import org.nutrivoltamin.exceptions.VitaminNotFoundException;
import org.nutrivoltamin.model.dtos.ErrorResponse;
import org.nutrivoltamin.model.dtos.VitaminView;
import org.nutrivoltamin.service.VitaminServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vitamin")
@RequiredArgsConstructor
public class VitaminController {

    private final VitaminServiceImp vitaminService;

    @GetMapping
    public ResponseEntity<List<VitaminView>> getAllVitamins(){
        return new ResponseEntity<>(vitaminService.getVitamins() , HttpStatus.OK);
    }
    @GetMapping("/{name}")
    public ResponseEntity<VitaminView> getVitaminByName(@PathVariable String name) throws VitaminNotFoundException {
        VitaminView vitaminView = vitaminService.getVitaminViewByName(name);
        return new ResponseEntity<>(vitaminView, HttpStatus.OK);
    }
    @ExceptionHandler(VitaminNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundVitamin(VitaminNotFoundException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Vitamin with name " + exception.getMessage() + " does not existed in the data.");
        errorResponse.setAvailableElectrolyteNames(vitaminService.getAllVitaminsNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
