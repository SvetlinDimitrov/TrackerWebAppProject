package org.record.exeptions;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RecordCreationException extends Exception{
    List<String> errorMessages;

    public RecordCreationException(List<String> errorMessages) {
        super();
        this.errorMessages = errorMessages;
    }
}
