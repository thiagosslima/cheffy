package br.com.fiap.cheffy.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDTO {

    private Long id;

    private String streetName;

    private Integer number;

    private String city;

    private String postalCode;

    private String neighborhood;

    private String stateProvince;

    private String addressLine;

    private Boolean main;
}
