package br.com.fiap.cheffy.model;

import br.com.fiap.cheffy.domain.ProfileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class TbUserResponseDTO {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    @TbUserEmailUnique
    private String email;

    @NotBlank
    @Size(max = 255)
    private String login;

    @NotBlank
    private ProfileType profileType;

}
