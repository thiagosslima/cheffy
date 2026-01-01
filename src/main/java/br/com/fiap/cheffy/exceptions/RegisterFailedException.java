package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;

public class RegisterFailedException extends BusinessException{
    public RegisterFailedException(ExceptionsKeys message) {
        super(message);
    }
}
