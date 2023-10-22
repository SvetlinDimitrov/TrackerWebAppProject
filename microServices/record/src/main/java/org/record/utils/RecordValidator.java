package org.record.utils;


import org.record.exeptions.RecordCreationException;
import org.record.model.dtos.RecordCreateDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RecordValidator {

    public static void validateRecord(RecordCreateDto dto) throws RecordCreationException {
        List<String> errors = new ArrayList<>();

        if(dto.getAge() == null || dto.getAge() < 0){
            errors.add("age must not be null");
        }
        if(dto.getGender() == null) {
            errors.add("gender must be selected");
        }
        if(dto.getHeight() == null || dto.getHeight().compareTo(new BigDecimal(0)) < 0){
            errors.add("height must not be null or negative");
        }
        if(dto.getKilograms() == null || dto.getKilograms().compareTo(new BigDecimal(0)) < 0){
            errors.add("kilograms must not be null or negative");
        }
        if(dto.getWorkoutState() == null){
            errors.add("workOut state must not be null");
        }
        if(!errors.isEmpty()){
            throw new RecordCreationException(errors);
        }
    }
}
