package br.com.fiap.cheffy.model;

import br.com.fiap.cheffy.domain.ProfileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TbUserCreateDTO(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        @TbUserEmailUnique
        String email,

        @NotBlank
        @Size(max = 255)
        String login,

        @NotBlank
        @Size(min = 8, max = 50, message = "The password needs to be between 8 and 50 chars")
        String password,

        @NotBlank
        ProfileType profileType
) {
}
