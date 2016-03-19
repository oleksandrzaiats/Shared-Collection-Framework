package se.lnu.application.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.lnu.application.model.dto.ErrorDTO;
import se.lnu.application.model.exception.ErrorCode;
import se.lnu.application.model.exception.InvalidBeanException;
import se.lnu.application.model.exception.RecordNotFoundException;

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
        } if (e instanceof RecordNotFoundException) {
            ErrorCode errorCode = ((RecordNotFoundException) e).getErrorCode();
            return new ResponseEntity<>(errorCode.getErrorDTO(), HttpStatus.NOT_FOUND);
        } else {
            errorDTO.setErrorCode(ErrorCode.INTERNAL_ERROR.getErrorCode());
            errorDTO.setMessage(ErrorCode.INTERNAL_ERROR.getMessage() + ": " + e.getMessage());
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
