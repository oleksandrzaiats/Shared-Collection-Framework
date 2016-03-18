package se.lnu.application.exception;

public class InvalidBeanException extends RuntimeException {
    public InvalidBeanException() {
        super();
    }

    public InvalidBeanException(String message) {
        super(message);
    }
}
