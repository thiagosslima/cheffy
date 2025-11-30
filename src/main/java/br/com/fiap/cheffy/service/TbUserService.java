package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.model.TbUserDTO;
import br.com.fiap.cheffy.repos.TbProfileRepository;
import br.com.fiap.cheffy.repos.TbUserRepository;
import br.com.fiap.cheffy.util.NotFoundException;
import java.util.HashSet;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class TbUserService {

    private final TbUserRepository tbUserRepository;
    private final TbProfileRepository tbProfileRepository;
    private final ApplicationEventPublisher publisher;

    public TbUserService(final TbUserRepository tbUserRepository,
            final TbProfileRepository tbProfileRepository,
            final ApplicationEventPublisher publisher) {
        this.tbUserRepository = tbUserRepository;
        this.tbProfileRepository = tbProfileRepository;
        this.publisher = publisher;
    }

    public List<TbUserDTO> findAll() {
        final List<TbUser> tbUsers = tbUserRepository.findAll(Sort.by("id"));
        return tbUsers.stream()
                .map(tbUser -> mapToDTO(tbUser, new TbUserDTO()))
                .toList();
    }

    public TbUserDTO get(final Long id) {
        return tbUserRepository.findById(id)
                .map(tbUser -> mapToDTO(tbUser, new TbUserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public TbUserDTO get(final String name) {
        return tbUserRepository.findByName(name)
                .map(tbUser -> mapToDTO(tbUser, new TbUserDTO()))
                .orElseThrow(NotFoundException::new);
    }



    public Long create(final TbUserDTO tbUserDTO) {
        final TbUser tbUser = new TbUser();
        mapToEntity(tbUserDTO, tbUser);
        return tbUserRepository.save(tbUser).getId();
    }

    public void update(final Long id, final TbUserDTO tbUserDTO) {
        final TbUser tbUser = tbUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tbUserDTO, tbUser);
        tbUserRepository.save(tbUser);
    }

    public void delete(final Long id) {
        final TbUser tbUser = tbUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTbUser(id));
        tbUserRepository.delete(tbUser);
    }

    private TbUserDTO mapToDTO(final TbUser tbUser, final TbUserDTO tbUserDTO) {
        tbUserDTO.setId(tbUser.getId());
        tbUserDTO.setName(tbUser.getName());
        tbUserDTO.setEmail(tbUser.getEmail());
        tbUserDTO.setLogin(tbUser.getLogin());
        tbUserDTO.setPassword(tbUser.getPassword());
        tbUserDTO.setProfiles(tbUser.getProfiles().stream()
                .map(tbProfile -> tbProfile.getId())
                .toList());
        return tbUserDTO;
    }

    private TbUser mapToEntity(final TbUserDTO tbUserDTO, final TbUser tbUser) {
        tbUser.setName(tbUserDTO.getName());
        tbUser.setEmail(tbUserDTO.getEmail());
        tbUser.setLogin(tbUserDTO.getLogin());
        tbUser.setPassword(tbUserDTO.getPassword());
        final List<TbProfile> profiles = tbProfileRepository.findAllById(
                tbUserDTO.getProfiles() == null ? List.of() : tbUserDTO.getProfiles());
        if (profiles.size() != (tbUserDTO.getProfiles() == null ? 0 : tbUserDTO.getProfiles().size())) {
            throw new NotFoundException("one of profiles not found");
        }
        tbUser.setProfiles(new HashSet<>(profiles));
        return tbUser;
    }

    public boolean emailExists(final String email) {
        return tbUserRepository.existsByEmailIgnoreCase(email);
    }

    @EventListener(BeforeDeleteTbProfile.class)
    public void on(final BeforeDeleteTbProfile event) {
        // remove many-to-many relations at owning side
        tbUserRepository.findAllByProfilesId(event.getId()).forEach(tbUser ->
                tbUser.getProfiles().removeIf(tbProfile -> tbProfile.getId().equals(event.getId())));
    }

}
