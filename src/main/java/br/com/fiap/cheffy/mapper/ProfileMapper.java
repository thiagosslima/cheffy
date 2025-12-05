package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.model.TbProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    TbProfileDTO mapToDTO(final TbProfile tbProfile);

    TbProfile mapToEntity(final TbProfileDTO tbProfileDTO);
}
