package br.com.fiap.cheffy.model.dtos;

import br.com.fiap.cheffy.model.enums.ProfileType;
import br.com.fiap.cheffy.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String email,

        @NotBlank
        @Size(max = 255)
        String login,

        @NotBlank
        @StrongPassword(minLength = 12)
        String password,

        @NotNull
        ProfileType profileType
) {
}
