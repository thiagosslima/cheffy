package br.com.fiap.cheffy.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TbUserUpdatePasswordDTO(
        @NotBlank
        @Size(min = 8, max = 50, message = "The password needs to be between 8 and 50 chars")
        String password
) {
}
