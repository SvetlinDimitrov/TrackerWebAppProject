package org.record.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.record.client.dto.User;
import org.record.exceptions.RecordCreationException;

public class RecordValidator {

    public static void validateRecord(User dto) throws RecordCreationException {
        List<String> errors = new ArrayList<>();

        if (dto.getAge() == null || dto.getAge() < 0) {
            errors.add("age must not be null");
        }
        if (dto.getGender() == null) {
            errors.add("gender must be selected");
        }
        if (dto.getHeight() == null ||
                dto.getHeight().compareTo(new BigDecimal(0)) < 0) {
            errors.add("height must not be null or negative");
        }
        if (dto.getKilograms() == null ||
                dto.getKilograms().compareTo(new BigDecimal(0)) < 0) {
            errors.add("kilograms must not be null or negative");
        }
        if (dto.getWorkoutState() == null) {
            errors.add("workOut state must not be null");
        }

        if (dto.getEmail() == null ||
                dto.getEmail().isBlank() ||
                !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add("email must not be null or invalid");
        }

        if (dto.getUsername() == null ||
                dto.getUsername().isBlank()) {
            errors.add("username must not be null");
        }

        if (!errors.isEmpty()) {
            throw new RecordCreationException(errors);
        }
    }
}
