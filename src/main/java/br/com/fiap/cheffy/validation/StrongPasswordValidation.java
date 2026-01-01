package br.com.fiap.cheffy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidation implements ConstraintValidator<StrongPassword, String> {

    private int minLength;

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if(password == null) {
            return false;
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSymbol = password.chars().anyMatch(
                ch -> !Character.isLetterOrDigit(ch)
        );

        return password.length() >= minLength
                && hasUppercase
                && hasLowercase
                && hasDigit
                && hasSymbol;
    }
}
