package ru.elsu.oio.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    private ValidDate constraintAnnotation;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat dateParser = new SimpleDateFormat(constraintAnnotation.pattern());

        if ((value == null || value.trim().isEmpty())) return true;

        try {
            final Date d = dateParser.parse(value);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }


}
