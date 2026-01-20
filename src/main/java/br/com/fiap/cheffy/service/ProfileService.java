package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.mapper.ProfileMapper;
import br.com.fiap.cheffy.model.dtos.ProfileDTO;
import br.com.fiap.cheffy.model.entities.Profile;
import br.com.fiap.cheffy.repository.ProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static br.com.fiap.cheffy.model.enums.ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION;


@Service
@Transactional(rollbackFor = Exception.class)
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher publisher;
    private final ProfileMapper mapper;
    private static final String ENTITY_NAME = "Profile";

    public ProfileService(final ProfileRepository profileRepository,
                          final ApplicationEventPublisher publisher, final ProfileMapper mapper) {
        this.profileRepository = profileRepository;
        this.publisher = publisher;
        this.mapper = mapper;
    }

    public List<ProfileDTO> findAll() {
        final List<Profile> profiles = profileRepository.findAll(Sort.by("id"));
        return profiles.stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    public ProfileDTO get(final Long id) {
        return profileRepository.findById(id)
                .map(mapper::mapToDTO)
                .orElseThrow(() -> new NotFoundException(
                        PROFILE_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()
                ));
    }

    public Long create(final ProfileDTO profileDTO) {
        Profile entity = mapper.mapToEntity(profileDTO);
        return profileRepository.save(entity).getId();
    }

    public void update(final Long id, final ProfileDTO profileDTO) {
        final Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        PROFILE_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        mapper.mapToEntity(profileDTO);
        profileRepository.save(profile);
    }

    public void delete(final Long id) {
        final Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        PROFILE_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        publisher.publishEvent(new BeforeDeleteTbProfile(id));
        profileRepository.delete(profile);
    }

}
