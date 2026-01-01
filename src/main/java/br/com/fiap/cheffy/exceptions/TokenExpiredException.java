package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenExpiredException extends ApiException {

    public TokenExpiredException(ExceptionsKeys message) {
        super(message);
    }
}
