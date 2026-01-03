package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;

public class UserEmailAlreadyExistsException extends BusinessException {

    public UserEmailAlreadyExistsException() {
        super(ExceptionsKeys.USER_EMAIL_ALREADY_EXISTS_EXCEPTION);
    }

}