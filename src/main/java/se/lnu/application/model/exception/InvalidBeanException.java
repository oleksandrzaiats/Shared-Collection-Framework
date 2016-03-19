package se.lnu.application.model.exception;

public class InvalidBeanException extends RuntimeException {
    public InvalidBeanException() {
        super();
    }

    public InvalidBeanException(String message) {
        super(message);
    }
}
