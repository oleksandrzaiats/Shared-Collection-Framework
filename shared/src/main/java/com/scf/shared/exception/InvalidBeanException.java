package com.scf.shared.exception;

public class InvalidBeanException extends RuntimeException {
    public InvalidBeanException() {
        super();
    }

    public InvalidBeanException(String message) {
        super(message);
    }
}
