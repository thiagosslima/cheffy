package br.com.fiap.cheffy.model;

import br.com.fiap.cheffy.domain.ProfileType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TbUserDTO {

    private UUID id;

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

    @NotNull
    @Size(max = 255)
    private String password;

    private ProfileType profileType;

}
