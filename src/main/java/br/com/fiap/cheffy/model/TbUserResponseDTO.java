package br.com.fiap.cheffy.model;

import br.com.fiap.cheffy.domain.ProfileType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TbUserResponseDTO {

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @TbUserEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String login;

    private ProfileType profileType;

}
