package com.scf.client;

import com.scf.client.config.Configuration;
import com.scf.client.resource.SCFRestClient;
import com.scf.shared.exception.InvalidBeanException;
import com.scf.shared.utils.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractClient {
    protected Configuration configuration;
    protected SCFRestClient restClient;

    public AbstractClient(Configuration configuration) {
        this.configuration = configuration;
        restClient = new SCFRestClient(configuration);
    }

    /**
     * Generic method for bean validation.
     *
     * @param bean bean to validate
     * @param <T>  type of bean
     * @throws InvalidBeanException if bean is not valid
     */
    protected <T> void validateBean(T bean) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<T>> violationSet = validator.validate(bean);
        Iterator<ConstraintViolation<T>> iterator = violationSet.iterator();
        if (iterator.hasNext()) {
            ConstraintViolation<T> next = iterator.next();
            throw new InvalidBeanException(StringUtils.formatValidationError(next));
        }
    }

    protected void validateId(Long id) {
        if (id == null) {
            throw new InvalidBeanException("Id parameter is null.");
        }
    }
}
