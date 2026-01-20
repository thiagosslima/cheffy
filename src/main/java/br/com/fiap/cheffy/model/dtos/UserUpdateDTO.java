package br.com.fiap.cheffy.model.dtos;

import br.com.fiap.cheffy.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @NotBlankIfPresent
        @Size(min = 3, max = 80, message = "Invalid name")
        String name,

        @NotBlankIfPresent
        @Email(message = "e-mail is not valid")
        String email
) {
}