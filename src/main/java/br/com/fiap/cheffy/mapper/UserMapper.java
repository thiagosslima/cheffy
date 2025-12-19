package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.model.TbUserCreateDTO;
import br.com.fiap.cheffy.model.TbUserResponseDTO;
import br.com.fiap.cheffy.model.TbUserUpdateDTO;
import br.com.fiap.cheffy.model.TbUserUpdatePasswordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "profiles", ignore = true)
    TbUser mapToEntity(TbUserCreateDTO tbUserDTO);

    TbUserResponseDTO mapToDTO(TbUser tbUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profiles", ignore = true)
    void updateUserFromDto(TbUserUpdateDTO tbUserDTO, @MappingTarget TbUser tbUser);

}