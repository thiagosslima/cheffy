package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;

public class OperationNotAllowedException extends BusinessException{
    public OperationNotAllowedException(ExceptionsKeys message) {
        super(message);
    }
}
