package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;

public class ApiException extends RuntimeException{
    public ApiException(ExceptionsKeys message) {
        super(String.valueOf(message));
    }
}
