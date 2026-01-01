package br.com.fiap.cheffy.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProfileDTO {

    @NotNull
    @Size(max = 255)
    private String type;

}
