package br.com.fiap.cheffy.model;

import br.com.fiap.cheffy.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TbUserUpdatePasswordDTO(
        @NotBlank
        @StrongPassword(minLength = 12)
        String password
) {
}
