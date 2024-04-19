package org.record.exceptions;

import java.util.List;

import lombok.Getter;

@Getter
public class RecordCreationException extends Exception{
    List<String> errorMessages;

    public RecordCreationException(List<String> errorMessages) {
        super();
        this.errorMessages = errorMessages;
    }
}
