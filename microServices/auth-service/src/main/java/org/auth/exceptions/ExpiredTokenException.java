package org.auth.exceptions;

public class ExpiredTokenException extends Exception{
    
        public ExpiredTokenException(String message) {
            super(message);
        }
}
