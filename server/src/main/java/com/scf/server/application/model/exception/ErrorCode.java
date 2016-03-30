package com.scf.server.application.model.exception;

import com.scf.shared.dto.ErrorDTO;

public enum ErrorCode {
    VALIDATION_ERROR(1, ""),
    INTERNAL_ERROR(2, "Internal server error"),
    ARTIFACT_NOT_FOUND(3, "Artifact does not exist"),
    COLLECTION_NOT_FOUND(4, "Collection does not exist"),
    USER_NOT_FOUND(5, "User does not exist"),
    USER_EXISTS(6, "User with this login already exists"),
    NO_PERMISSION(7, "You don't have permission for editing or deleting this object"),
    USER_CANNOT_BE_DELETED(8, "User cannot be deleted, because he has connected artifacts")
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
