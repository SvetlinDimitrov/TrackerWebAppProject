package org.record.exeptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RecordCreationException extends Exception{
    List<String> errorMessages = new ArrayList<>();

    public RecordCreationException(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
