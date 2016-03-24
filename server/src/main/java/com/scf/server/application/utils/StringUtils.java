package com.scf.server.application.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scf.server.application.model.exception.ErrorCode;

import javax.validation.ConstraintViolation;

public class StringUtils {

    public static String errorCodeToJson(ErrorCode errorCode) {
        ObjectMapper converter = new ObjectMapper();
        try {
            return converter.writeValueAsString(errorCode);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> String formatValidationError(ConstraintViolation<T> error) {
        String fieldWrapper = "Field '".concat(error.getPropertyPath().toString()).concat("': ");
        return fieldWrapper.concat(error.getMessage());
    }
}
