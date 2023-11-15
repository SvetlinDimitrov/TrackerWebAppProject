package org.vitamin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vitamin.model.dtos.ErrorResponse;
import org.vitamin.model.dtos.VitaminView;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vitamin")
@RequiredArgsConstructor
public class VitaminController {

    private final VitaminServiceImp vitaminService;

    @GetMapping
    public ResponseEntity<List<VitaminView>> getAllVitamins() {
        return new ResponseEntity<>(vitaminService.getVitamins(), HttpStatus.OK);
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
