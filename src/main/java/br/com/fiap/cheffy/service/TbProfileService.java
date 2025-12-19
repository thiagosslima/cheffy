package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.mapper.ProfileMapper;
import br.com.fiap.cheffy.model.TbProfileDTO;
import br.com.fiap.cheffy.repos.TbProfileRepository;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.fiap.cheffy.domain.ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION;


@Service
@Transactional(rollbackFor = Exception.class)
public class TbProfileService {

    private final TbProfileRepository tbProfileRepository;
    private final ApplicationEventPublisher publisher;
    private final ProfileMapper mapper;
    private static final String ENTITY_NAME = "Profile";

    public TbProfileService(final TbProfileRepository tbProfileRepository,
            final ApplicationEventPublisher publisher, final ProfileMapper mapper) {
        this.tbProfileRepository = tbProfileRepository;
        this.publisher = publisher;
        this.mapper = mapper;
    }

    public List<TbProfileDTO> findAll() {
        final List<TbProfile> tbProfiles = tbProfileRepository.findAll(Sort.by("id"));
        return tbProfiles.stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    public TbProfileDTO get(final Long id) {
        return tbProfileRepository.findById(id)
                .map(mapper::mapToDTO)
                .orElseThrow( () -> new NotFoundException(
                        PROFILE_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()
                ));
    }

    public Long create(final TbProfileDTO tbProfileDTO) {
        TbProfile entity = mapper.mapToEntity(tbProfileDTO);
        return tbProfileRepository.save(entity).getId();
    }

    public void update(final Long id, final TbProfileDTO tbProfileDTO) {
        final TbProfile tbProfile = tbProfileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        PROFILE_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        mapper.mapToEntity(tbProfileDTO);
        tbProfileRepository.save(tbProfile);
    }

    public void delete(final Long id) {
        final TbProfile tbProfile = tbProfileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        PROFILE_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        publisher.publishEvent(new BeforeDeleteTbProfile(id));
        tbProfileRepository.delete(tbProfile);
    }

}
