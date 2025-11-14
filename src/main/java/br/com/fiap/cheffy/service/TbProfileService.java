package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.model.TbProfileDTO;
import br.com.fiap.cheffy.repos.TbProfileRepository;
import br.com.fiap.cheffy.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class TbProfileService {

    private final TbProfileRepository tbProfileRepository;
    private final ApplicationEventPublisher publisher;

    public TbProfileService(final TbProfileRepository tbProfileRepository,
            final ApplicationEventPublisher publisher) {
        this.tbProfileRepository = tbProfileRepository;
        this.publisher = publisher;
    }

    public List<TbProfileDTO> findAll() {
        final List<TbProfile> tbProfiles = tbProfileRepository.findAll(Sort.by("id"));
        return tbProfiles.stream()
                .map(tbProfile -> mapToDTO(tbProfile, new TbProfileDTO()))
                .toList();
    }

    public TbProfileDTO get(final Long id) {
        return tbProfileRepository.findById(id)
                .map(tbProfile -> mapToDTO(tbProfile, new TbProfileDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TbProfileDTO tbProfileDTO) {
        final TbProfile tbProfile = new TbProfile();
        mapToEntity(tbProfileDTO, tbProfile);
        return tbProfileRepository.save(tbProfile).getId();
    }

    public void update(final Long id, final TbProfileDTO tbProfileDTO) {
        final TbProfile tbProfile = tbProfileRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tbProfileDTO, tbProfile);
        tbProfileRepository.save(tbProfile);
    }

    public void delete(final Long id) {
        final TbProfile tbProfile = tbProfileRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTbProfile(id));
        tbProfileRepository.delete(tbProfile);
    }

    private TbProfileDTO mapToDTO(final TbProfile tbProfile, final TbProfileDTO tbProfileDTO) {
        tbProfileDTO.setId(tbProfile.getId());
        tbProfileDTO.setType(tbProfile.getType());
        return tbProfileDTO;
    }

    private TbProfile mapToEntity(final TbProfileDTO tbProfileDTO, final TbProfile tbProfile) {
        tbProfile.setType(tbProfileDTO.getType());
        return tbProfile;
    }

}
