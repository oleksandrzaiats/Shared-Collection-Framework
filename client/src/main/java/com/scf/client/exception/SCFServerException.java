package com.scf.client.exception;

import com.scf.shared.dto.ErrorDTO;

import javax.validation.constraints.NotNull;

public class SCFServerException extends RuntimeException {
    private ErrorDTO errorDTO;
    private Integer  httpStatus;
    private String  httpMessage;

    public SCFServerException(@NotNull Integer httpStatus, @NotNull ErrorDTO errorDTO) {
        super("HTTP code: " + httpStatus.toString() + ", " + errorDTO.toString());
        this.errorDTO = errorDTO;
        this.httpStatus = httpStatus;
    }

    public SCFServerException(@NotNull Integer httpStatus, @NotNull String message) {
        super("HTTP code: " + httpStatus.toString() + ", " + message);
        this.httpMessage = message;
        this.httpStatus = httpStatus;
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getHttpMessage() {
        return httpMessage;
    }
}
