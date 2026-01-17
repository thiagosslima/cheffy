package br.com.fiap.cheffy.model.dtos;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressPatchDTO {

    @Size(max = 255)
    private String streetName;

    private Integer number;

    @Size(max = 255)
    private String city;

    private Integer postalCode;

    @Size(max = 255)
    private String neighborhood;

    @Size(max = 255)
    private String stateProvince;

    @Size(max = 255)
    private String addressLine;

    private Boolean main;
}