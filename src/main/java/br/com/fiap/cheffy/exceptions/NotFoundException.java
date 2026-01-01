package br.com.fiap.cheffy.exceptions;

import br.com.fiap.cheffy.model.enums.ExceptionsKeys;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends BusinessException {

    private final String entityName;
    private final String id;

    public NotFoundException(ExceptionsKeys message, String entityName, String id) {
        super(message);
        this.entityName = entityName;
        this.id = id;
    }
}
