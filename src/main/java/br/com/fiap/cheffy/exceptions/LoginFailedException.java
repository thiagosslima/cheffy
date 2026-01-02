package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginFailedException extends ApiException {

    private final String originalMessage;

    public LoginFailedException(ExceptionsKeys message, String originalMessage) {
        super(message);
        this.originalMessage = originalMessage;
    }
}
