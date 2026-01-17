package br.com.fiap.cheffy.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressCreateDTO {

    @NotNull
    @Size(max = 255)
    private String streetName;

    @NotNull
    private Integer number;

    @NotNull
    @Size(max = 255)
    private String city;

    @NotNull
    private Integer postalCode;

    @NotNull
    @Size(max = 255)
    private String neighborhood;

    @NotNull
    @Size(max = 255)
    private String stateProvince;

    @Size(max = 255)
    private String addressLine;

    private Boolean main;

}
