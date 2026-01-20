package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;

public class ApiInternalServerErrorException extends ApiException {
    public ApiInternalServerErrorException(ExceptionsKeys message) {
        super(message);
    }

}
