package ru.elsu.oio.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullNotBlankValidator implements ConstraintValidator<NotNullNotBlank, String> {

    @Override
    public void initialize(NotNullNotBlank constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value != null) && !value.trim().isEmpty();
    }

}
