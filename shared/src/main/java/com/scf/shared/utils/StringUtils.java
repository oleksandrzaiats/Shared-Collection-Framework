package com.scf.shared.utils;

import javax.validation.ConstraintViolation;

public class StringUtils {

    public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    public static <T> String formatValidationError(ConstraintViolation<T> error) {
        String fieldWrapper = "Field '".concat(error.getPropertyPath().toString()).concat("': ");
        return fieldWrapper.concat(error.getMessage());
    }
}
