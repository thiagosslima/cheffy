package br.com.fiap.cheffy.mapper;

import br.com.fiap.cheffy.model.entities.Profile;
import br.com.fiap.cheffy.model.dtos.ProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDTO mapToDTO(final Profile profile);

    Profile mapToEntity(final ProfileDTO profileDTO);
}
