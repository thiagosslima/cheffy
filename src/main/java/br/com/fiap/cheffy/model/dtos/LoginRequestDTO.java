package br.com.fiap.cheffy.model.dtos;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(

        @NotEmpty
        String login,

        @NotEmpty
        String password
) {}
