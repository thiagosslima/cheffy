package br.com.fiap.cheffy.exceptions;

public class ApiInternalServerErrorException extends ApiException {
    public ApiInternalServerErrorException(String message) {
        super(message);
    }

}
