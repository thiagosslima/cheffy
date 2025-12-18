package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.ExceptionsKeys;
import br.com.fiap.cheffy.domain.ProfileType;
import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.mapper.UserMapper;
import br.com.fiap.cheffy.model.TbUserCreateDTO;
import br.com.fiap.cheffy.model.TbUserResponseDTO;
import br.com.fiap.cheffy.model.TbUserUpdateDTO;
import br.com.fiap.cheffy.model.TbUserUpdatePasswordDTO;
import br.com.fiap.cheffy.repos.TbProfileRepository;
import br.com.fiap.cheffy.repos.TbUserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
@Transactional(rollbackFor = Exception.class)
public class TbUserService {

    private final TbUserRepository tbUserRepository;
    private final TbProfileRepository tbProfileRepository;
    private final ApplicationEventPublisher publisher;
    private final UserMapper userMapper;

    private static final String USER_ENTITY_NAME = "User";
    private static final String PROFILE_ENTITY_NAME = "Profile";

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

    public List<TbUserResponseDTO> findAll() {
        final List<TbUser> tbUsers = tbUserRepository.findAll(Sort.by("id"));
        return tbUsers.stream()
                .map(userMapper::mapToDTO)
                .toList();
    }

    public TbUserResponseDTO get(final UUID id) {
        return tbUserRepository.findById(id)
                .map(userMapper::mapToDTO)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionsKeys.USER_NOT_FOUND_EXCEPTION.toString(),
                        USER_ENTITY_NAME,
                        id.toString()));
    }

    public String create(final TbUserCreateDTO tbUserDTO) {
        final TbUser tbUser = userMapper.mapToEntity(tbUserDTO);
        extractedProfiles(tbUserDTO.profileType(), tbUser);
        return tbUserRepository.save(tbUser).getId().toString();
    }

    public TbUserResponseDTO get(final String name) {
        return tbUserRepository.findByName(name)
                .map(userMapper::mapToDTO)
                .orElseThrow();
    }

    public void update(final UUID id, final TbUserUpdateDTO tbUserDTO) {
        final TbUser tbUser = findById(id);
        userMapper.updateUserFromDtoWithoutPassword(tbUserDTO, tbUser);
        extractedProfiles(tbUserDTO.profileType(), tbUser);
        tbUserRepository.save(tbUser);
    }

    private void extractedProfiles(ProfileType tbUserDTO, TbUser tbUser) {
        if (tbUserDTO != null) {
            final TbProfile profile = tbProfileRepository.findByType(tbUserDTO.name())
                    .orElseThrow(() -> new NotFoundException(
                            ExceptionsKeys.PROFILE_NOT_FOUND_EXCEPTION.toString(),
                            PROFILE_ENTITY_NAME,
                            null));
            tbUser.setProfiles(Set.of(profile));
        }
    }

    public void delete(final UUID id) {
        final TbUser tbUser = findById(id);
        publisher.publishEvent(new BeforeDeleteTbUser(id));
        tbUserRepository.delete(tbUser);
    }

    public void updatePassword(final UUID id, final TbUserUpdatePasswordDTO tbUserUpdatePasswordDTO) {
        final TbUser tbUser = findById(id);
        userMapper.updateUserFromDtoOnlyPassword(tbUserUpdatePasswordDTO, tbUser);
        tbUserRepository.save(tbUser);
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

    private TbUser findById(final UUID id){
        return tbUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ExceptionsKeys.USER_NOT_FOUND_EXCEPTION.toString(),
                        USER_ENTITY_NAME,
                        id.toString()));
    }

}
