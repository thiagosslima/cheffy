package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.model.entities.User;
import br.com.fiap.cheffy.model.dtos.UserCreateDTO;
import br.com.fiap.cheffy.model.dtos.UserResponseDTO;
import br.com.fiap.cheffy.model.dtos.UserUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "profiles", ignore = true)
    User mapToEntity(UserCreateDTO tbUserDTO);

    UserResponseDTO mapToDTO(User user);

    void updateUserFromDto(UserUpdateDTO tbUserDTO, @MappingTarget User user);

}