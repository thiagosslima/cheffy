package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.model.dtos.UserCreateDTO;
import br.com.fiap.cheffy.model.dtos.UserResponseDTO;
import br.com.fiap.cheffy.model.entities.Profile;
import br.com.fiap.cheffy.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = AddressMapper.class
)
public interface UserMapper {

    @Mapping(target = "profiles", ignore = true)
    User mapToEntity(UserCreateDTO tbUserDTO);

    @Mapping(target = "addresses", source = "addresses")
    @Mapping(target = "profiles", expression = "java(mapProfiles(user))")
    UserResponseDTO mapToDTO(User user);

    default List<String> mapProfiles(User user) {
        if (user.getProfiles() == null || user.getProfiles().isEmpty()) {
            return Collections.emptyList();
        }
        return user.getProfiles().stream()
                .map(Profile::getType)
                .toList();
    }

}