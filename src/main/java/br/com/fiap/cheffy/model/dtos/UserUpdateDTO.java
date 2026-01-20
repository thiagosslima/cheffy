package br.com.fiap.cheffy.model.dtos;

import br.com.fiap.cheffy.validation.NotBlankIfPresent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @NotBlankIfPresent
        @Size(min = 3, max = 80)
        String name,

        @NotBlankIfPresent
        @Email(message = "O email deve ser válido")
        String email
) {
}