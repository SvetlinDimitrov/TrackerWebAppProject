package org.storage.exception;

import lombok.Getter;

@Getter
public class StorageException extends Exception {

    public StorageException(String errorResponse) {
        super(errorResponse);

    }
}
