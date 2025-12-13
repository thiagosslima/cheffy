package br.com.fiap.cheffy.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends BusinessException {

    private final String entityName;
    private final String id;

    public NotFoundException(String message, String entityName, String id) {
        super(message);
        this.entityName = entityName;
        this.id = id;
    }
}
