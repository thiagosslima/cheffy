package br.com.fiap.cheffy.model.dtos;

import br.com.fiap.cheffy.validation.NotBlankIfPresent;
import br.com.fiap.cheffy.validation.PostalCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @PostalCode
    private String postalCode;

    @NotBlankIfPresent
    @Size(max = 255, min = 3)
    private String neighborhood;

    @NotBlankIfPresent
    @Size(max = 2)
    private String stateProvince;

    @NotBlankIfPresent
    @Size(max = 255, min = 3)
    private String addressLine;

    private Boolean main;
}