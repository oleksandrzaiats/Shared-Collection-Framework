package se.lnu.application.exception;

import se.lnu.application.dto.ErrorDTO;

public enum ErrorCode {
    VALIDATION_ERROR(1, ""),
    INTERNAL_ERROR(2, "Internal server error"),
    ARTIFACT_NOT_FOUND(3, "Artifact does not exist"),
    COLLECTION_NOT_FOUND(4, "Collection does not exist"),
    USER_NOT_FOUND(5, "User does not exist"),
    USER_EXISTS(6, "User exists"),
    ;

    ErrorDTO apiError;

    ErrorCode(Integer code, String message) {
        apiError = new ErrorDTO();
        apiError.setErrorCode(code);
        apiError.setMessage(message);
    }

    public Integer getErrorCode() {
        return apiError.getErrorCode();
    }

    public String getMessage() {
        return apiError.getMessage();
    }

    public ErrorDTO getErrorDTO() {
        return apiError;
    }
}
