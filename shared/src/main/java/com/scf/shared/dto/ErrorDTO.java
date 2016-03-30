package com.scf.shared.dto;

public class ErrorDTO {
    private Integer errorCode;
    private String message;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "errorCode=" + errorCode +
                ", message='" + message + '\'';
    }
}
