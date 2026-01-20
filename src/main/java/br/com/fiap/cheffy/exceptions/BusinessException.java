package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;

public class BusinessException extends RuntimeException{
    public BusinessException(ExceptionsKeys message){
        super(String.valueOf(message));
    }
}
