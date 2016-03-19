package se.lnu.application.model.exception;

public class RecordNotFoundException extends RuntimeException {

    ErrorCode errorCode;

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(ErrorCode errorCode) {
        super(errorCode.getErrorDTO().getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
