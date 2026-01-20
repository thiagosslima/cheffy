package br.com.fiap.cheffy.model.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @Size(min = 3, max = 80, message = "Invalid name")
        String name,

        @Email(message = "e-mail is not valid")
        String email
) {
}