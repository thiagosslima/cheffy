package br.com.fiap.cheffy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TbUserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @TbUserEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String login;

    @NotNull
    @Size(max = 255)
    private String password;

    private List<Long> profiles;

}
