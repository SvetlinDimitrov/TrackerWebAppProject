package org.storage.exception;

import org.storage.model.errorResponses.ErrorResponse;

import lombok.Getter;

@Getter
public class StorageNotFoundException extends Exception{
    private final ErrorResponse errorResponse;

    public StorageNotFoundException(String errorResponse) {
        this.errorResponse = new ErrorResponse(errorResponse);
    }
}
