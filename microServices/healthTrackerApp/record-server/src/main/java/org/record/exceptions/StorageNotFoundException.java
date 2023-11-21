package org.record.exceptions;

public class StorageNotFoundException extends Exception {

    public StorageNotFoundException(String errorResponse) {
        super(errorResponse);
    }
}
