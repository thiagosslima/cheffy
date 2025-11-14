package br.com.fiap.cheffy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TbProfileDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String type;

}
