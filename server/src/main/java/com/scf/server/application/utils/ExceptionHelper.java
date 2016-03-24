package com.scf.server.application.utils;

import com.scf.shared.dto.ErrorDTO;
import com.scf.server.application.model.exception.ErrorCode;
import com.scf.server.application.model.exception.InvalidBeanException;
import com.scf.server.application.model.exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scf.server.application.model.exception.NoPermissionException;

/**
 * Creating response based on caught exception.
 */
public class ExceptionHelper {
    public static ResponseEntity<ErrorDTO> processException(RuntimeException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        if (e instanceof InvalidBeanException) {
            errorDTO.setErrorCode(ErrorCode.VALIDATION_ERROR.getErrorCode());
            errorDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
        if (e instanceof RecordNotFoundException) {
            ErrorCode errorCode = ((RecordNotFoundException) e).getErrorCode();
            return new ResponseEntity<>(errorCode.getErrorDTO(), HttpStatus.NOT_FOUND);
        }
        if (e instanceof NoPermissionException) {
            return new ResponseEntity<>(ErrorCode.NO_PERMISSION.getErrorDTO(), HttpStatus.FORBIDDEN);
        } else {
            errorDTO.setErrorCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            errorDTO.setMessage(ErrorCode.INTERNAL_ERROR.getMessage() + ": " + e.getMessage());
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
