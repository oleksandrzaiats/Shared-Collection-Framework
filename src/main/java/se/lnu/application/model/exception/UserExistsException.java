package se.lnu.application.model.exception;

public class UserExistsException extends RuntimeException {
    ErrorCode errorCode;

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
