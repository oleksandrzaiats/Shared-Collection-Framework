package com.scf.server.application.model.exception;

public class UserExistsException extends RuntimeException {

    private ErrorCode errorCode;

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(ErrorCode errorCode) {
        super(errorCode.getErrorDTO().getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
