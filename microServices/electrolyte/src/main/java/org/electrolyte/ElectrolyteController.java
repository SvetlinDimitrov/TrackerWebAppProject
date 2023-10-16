package org.electrolyte;

import lombok.RequiredArgsConstructor;
import org.electrolyte.model.dtos.ElectrolyteView;
import org.electrolyte.model.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/electrolyte")
@RequiredArgsConstructor
public class ElectrolyteController {

    private final ElectrolyteServiceImp electrolyteService;

    @GetMapping
    public ResponseEntity<List<ElectrolyteView>> getAllElectrolytes(){
        return new ResponseEntity<>(electrolyteService.getAllViewElectrolytes() , HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ElectrolyteView> getElectrolyteByName(@PathVariable String name) throws ElectrolyteNotFoundException {
        return new ResponseEntity<>(electrolyteService.getElectrolyteViewByName(name) , HttpStatus.OK);
    }

    @ExceptionHandler(ElectrolyteNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchElectrolyteNotFound(ElectrolyteNotFoundException e) {

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage("Electrolyte with name '" + e.getMessage() + "' does not exist in the data.");
        errorResponse.setAvailableElectrolyteNames(electrolyteService.getAllElectrolytesNames());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
