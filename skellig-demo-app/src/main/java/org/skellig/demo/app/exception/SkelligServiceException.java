package org.skellig.demo.app.exception;

public class SkelligServiceException extends RuntimeException {

    public SkelligServiceException(String message) {
        super(message);
    }

    public SkelligServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
