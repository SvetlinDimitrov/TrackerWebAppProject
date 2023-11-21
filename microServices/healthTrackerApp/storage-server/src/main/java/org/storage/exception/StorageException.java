package org.storage.exception;

import org.storage.model.errorResponses.ErrorResponse;

import lombok.Getter;

@Getter
public class StorageException extends Exception{
    private final ErrorResponse errorResponse;

    public StorageException(String errorResponse) {
        this.errorResponse = new ErrorResponse(errorResponse);
    }
}
