package com.scf.client.exception;

import com.scf.shared.dto.ErrorDTO;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

public class SCFServerException extends RuntimeException {
    ErrorDTO errorDTO;
    HttpStatus httpStatus;

    public SCFServerException(@NotNull HttpStatus httpStatus, @NotNull ErrorDTO errorDTO) {
        super("HTTP code: " + httpStatus.toString() + ", " + errorDTO.toString());
        this.errorDTO = errorDTO;
        this.httpStatus = httpStatus;
    }

    public SCFServerException(@NotNull HttpStatus httpStatus, @NotNull String message) {
        super("HTTP code: " + httpStatus.toString() + ", " + message);
        this.httpStatus = httpStatus;
    }
}
