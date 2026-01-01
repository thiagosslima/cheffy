package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.domain.ExceptionsKeys;

public class ApiException extends RuntimeException{
    public ApiException(ExceptionsKeys message) {
        super(String.valueOf(message));
    }
}
