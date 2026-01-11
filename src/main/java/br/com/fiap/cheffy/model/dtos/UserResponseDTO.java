package br.com.fiap.cheffy.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class UserResponseDTO {

    private UUID id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String login;

    @NotNull
    private List<String> profiles;

}
