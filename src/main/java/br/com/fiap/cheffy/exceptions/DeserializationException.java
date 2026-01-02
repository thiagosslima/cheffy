package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeserializationException extends ApiException {

    public DeserializationException(ExceptionsKeys message) {
        super(message);
    }

}
