package ru.elsu.oio.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Stavka.Validator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Stavka {

    String message() default "{validation.constraints.Stavka.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public static class Validator implements ConstraintValidator<Stavka, Float> {

        @Override
        public void initialize(Stavka constraintAnnotation) {}

        @Override
        public boolean isValid(Float f, ConstraintValidatorContext context) {
            if (f == 0.25 || f == 0.5 || f == 0.75 || f == 1 || f == 1.25 || f == 1.5) {
                return true;
            }
            return false;
        }
    }

}

