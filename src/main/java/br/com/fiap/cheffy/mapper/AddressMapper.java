package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.model.dtos.AddressCreateDTO;
import br.com.fiap.cheffy.model.dtos.AddressPatchDTO;
import br.com.fiap.cheffy.model.entities.Address;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(AddressPatchDTO dto, @MappingTarget Address entity);

    Address mapToEntity(AddressCreateDTO address);

}
