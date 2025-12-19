package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.domain.ExceptionsKeys;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeserializationException extends ApiException {

    public DeserializationException(ExceptionsKeys message) {
        super(message);
    }

}
