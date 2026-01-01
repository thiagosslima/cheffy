package br.com.fiap.cheffy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {StrongPasswordValidation.class})
@Target({ ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    int minLength() default 8;

    String message() default "{password.weak}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
