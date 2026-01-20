package br.com.fiap.cheffy.model.dtos;

import br.com.fiap.cheffy.validation.NotBlankIfPresent;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressPatchDTO {

    @NotBlankIfPresent
    @Size(max = 255, min = 3)
    private String streetName;

    @Positive
    @Min(1)
    private Integer number;

    @NotBlankIfPresent
    @Size(max = 255, min = 3)
    private String city;

    @Positive
    @Min(8)
    @Max(8)
    private Integer postalCode;

    @NotBlankIfPresent
    @Size(max = 255, min = 3)
    private String neighborhood;

    @NotBlankIfPresent
    @Size(max = 255, min = 2)
    private String stateProvince;

    @NotBlankIfPresent
    @Size(max = 255, min = 3)
    private String addressLine;

    private Boolean main;
}