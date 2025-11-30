package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.model.TbUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    TbUser mapToEntity(TbUserDTO tbUserDTO);

    TbUserDTO mapToDTO(TbUser tbUser);

}