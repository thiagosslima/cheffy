package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.domain.ExceptionsKeys;

public class RegisterFailedException extends BusinessException{
    public RegisterFailedException(ExceptionsKeys message) {
        super(message);
    }
}
