package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.mapper.UserMapper;
import br.com.fiap.cheffy.model.TbUserDTO;
import br.com.fiap.cheffy.repos.TbProfileRepository;
import br.com.fiap.cheffy.repos.TbUserRepository;
import br.com.fiap.cheffy.util.NotFoundException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    private final UserMapper userMapper;

    public TbUserService(
            final TbUserRepository tbUserRepository,
            final TbProfileRepository tbProfileRepository,
            final ApplicationEventPublisher publisher,
            final UserMapper userMapper) {

        this.tbUserRepository = tbUserRepository;
        this.tbProfileRepository = tbProfileRepository;
        this.publisher = publisher;
        this.userMapper = userMapper;
    }

    public List<TbUserDTO> findAll() {
        final List<TbUser> tbUsers = tbUserRepository.findAll(Sort.by("id"));
        return tbUsers.stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    public TbUserDTO get(final UUID id) {
        return tbUserRepository.findById(id)
                .map(userMapper::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public String create(final TbUserDTO tbUserDTO) {
        final TbUser tbUser = userMapper.mapToEntity(tbUserDTO);
        if (tbUserDTO.getProfileType() != null) {
            final TbProfile profile = tbProfileRepository.findByType(tbUserDTO.getProfileType().name())
                    .orElseThrow(() -> new NotFoundException("Profile not found"));
            tbUser.setProfiles(Set.of(profile));
        }
        return tbUserRepository.save(tbUser).getId().toString();
    }

    public void update(final UUID id, final TbUserDTO tbUserDTO) {
        final TbUser tbUser = tbUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        tbUserRepository.save(tbUser);
    }

    public void delete(final UUID id) {
        final TbUser tbUser = tbUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTbUser(id));
        tbUserRepository.delete(tbUser);
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
