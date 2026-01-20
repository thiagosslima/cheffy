package br.com.fiap.cheffy.model.dtos;

import br.com.fiap.cheffy.validation.PostalCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressCreateDTO {

    @NotBlank
    @Size(max = 255)
    private String streetName;

    @NotNull
    private Integer number;

    @NotBlank
    @Size(max = 255)
    private String city;

    @PostalCode
    private String postalCode;

    @NotBlank
    @Size(max = 255)
    private String neighborhood;

    @NotBlank
    @Size(max = 2)
    private String stateProvince;

    @Size(max = 255)
    private String addressLine;

    private Boolean main;

}
