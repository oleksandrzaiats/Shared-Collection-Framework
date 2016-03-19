package se.lnu.application.model.exception;

import se.lnu.application.model.dto.ErrorDTO;

public enum ErrorCode {
    VALIDATION_ERROR(1, ""),
    INTERNAL_ERROR(2, "Internal server error"),
    ARTIFACT_NOT_FOUND(3, "Artifact does not exists"),
    COLLECTION_NOT_FOUND(4, "Collection does not exists"),
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
