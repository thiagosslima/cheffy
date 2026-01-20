package br.com.fiap.cheffy.model.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank
        String login,

        @NotBlank
        String password
) {
}
