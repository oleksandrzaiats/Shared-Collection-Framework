package se.lnu.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import se.lnu.application.model.dto.ErrorDTO;
import se.lnu.application.model.exception.InvalidBeanException;
import se.lnu.application.security.AuthUser;
import se.lnu.application.utils.ExceptionHelper;
import se.lnu.application.utils.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

/**
 * Abstract class for controllers.
 * Handler for all runtime exceptions.
 * Contains bean validation methods.
 */
@ControllerAdvice
public abstract class AbstractController {

    private final Validator validator;

    public AbstractController() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    /**
     * Handle all exceptions and create response based
     * on exception type.
     *
     * @param e handled exception
     * @return response with error
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRuntime(RuntimeException e) {
        return ExceptionHelper.processException(e);
    }

    /**
     * Generic method for bean validation.
     *
     * @param bean bean to validate
     * @param <T>  type of bean
     * @throws InvalidBeanException if bean is not valid
     */
    protected <T> void validateBean(T bean) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(bean);
        Iterator<ConstraintViolation<T>> iterator = violationSet.iterator();
        if (iterator.hasNext()) {
            ConstraintViolation<T> next = iterator.next();
            throw new InvalidBeanException(StringUtils.formatValidationError(next));
        }

    }

    protected AuthUser getCurrentUser() {
        return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
