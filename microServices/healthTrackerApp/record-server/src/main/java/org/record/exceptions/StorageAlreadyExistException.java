package org.record.exceptions;

public class StorageAlreadyExistException extends Exception {

    public StorageAlreadyExistException(String errorResponse) {
        super(errorResponse);
    }
}
