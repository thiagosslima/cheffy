package br.com.fiap.cheffy.model;

import br.com.fiap.cheffy.domain.ProfileType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TbUserCreateDTO(
        @NotNull
        @Size(max = 255)
        String name,

        @NotNull
        @Size(max = 255)
        @TbUserEmailUnique
        String email,

        @NotNull
        @Size(max = 255)
        String login,

        @NotNull
        @Size(min = 8, max = 50, message = "The password needs to be between 8 and 50 chars")
        String password,

        @NotNull
        ProfileType profileType
) {
}
