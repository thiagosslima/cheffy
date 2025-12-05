package br.com.fiap.cheffy.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeserializationException extends ApiException {

    public DeserializationException(String message) {
        super(message);
    }

}
