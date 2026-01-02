package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.model.entities.User;
import br.com.fiap.cheffy.model.dtos.UserUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "login", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "profiles", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    void updateEntityFromDto(UserUpdateDTO dto, @MappingTarget User user);
}
