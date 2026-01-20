package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;

public class InvalidOperationException extends BusinessException {

    public InvalidOperationException() {
        super(ExceptionsKeys.INVALID_OPERATION_EXCEPTION);
    }

}