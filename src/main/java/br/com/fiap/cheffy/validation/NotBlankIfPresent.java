package br.com.fiap.cheffy.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {NotBlankIfPresentValidation.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfPresent {

    String message() default "O campo não pode estar em branco se presente";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
