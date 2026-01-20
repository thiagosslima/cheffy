package br.com.fiap.cheffy.model.dtos;

import br.com.fiap.cheffy.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;

public record UserUpdatePasswordDTO(
        @NotBlank
        @StrongPassword(minLength = 12)
        String password
) {
}
