package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.ProfileType;
import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.exceptions.RegisterFailedException;
import br.com.fiap.cheffy.mapper.UserMapper;
import br.com.fiap.cheffy.model.TbUserCreateDTO;
import br.com.fiap.cheffy.model.TbUserResponseDTO;
import br.com.fiap.cheffy.model.TbUserUpdateDTO;
import br.com.fiap.cheffy.model.TbUserUpdatePasswordDTO;
import br.com.fiap.cheffy.mapper.TbUserUpdateMapper;
import br.com.fiap.cheffy.repos.TbProfileRepository;
import br.com.fiap.cheffy.repos.TbUserRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.fiap.cheffy.domain.ExceptionsKeys.*;
import java.util.Objects;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TbUserService {

    private final TbUserRepository tbUserRepository;
    private final TbProfileRepository tbProfileRepository;
    private final ApplicationEventPublisher publisher;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TbUserUpdateMapper userUpdateMapper;

    private static final String USER_ENTITY_NAME = "User";
    private static final String PROFILE_ENTITY_NAME = "Profile";

    public TbUserService(
            final TbUserRepository tbUserRepository,
            final TbProfileRepository tbProfileRepository,
            final ApplicationEventPublisher publisher,
            final UserMapper userMapper,
            final PasswordEncoder passwordEncoder,
            final TbUserUpdateMapper userUpdateMapper) {

        this.tbUserRepository = tbUserRepository;
        this.tbProfileRepository = tbProfileRepository;
        this.publisher = publisher;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userUpdateMapper = userUpdateMapper;
    }

    public List<TbUserResponseDTO> findAll() {
        log.info("TbUserService.findAll - START");

        final List<TbUser> tbUsers = tbUserRepository.findAll(Sort.by("id"));

        var response = tbUsers.stream()
                .map(userMapper::mapToDTO)
                .toList();

        log.info("TbUserService.findAll - END - Retrieved {} users", response.size());
        return response;
    }

    public TbUserResponseDTO get(final UUID id) {
        log.info("TbUserService.get - START");
        var response = userMapper.mapToDTO(findById(id));

        log.info("TbUserService.get - END - Retrieved user: [{}]", id);
        return response;
    }

    public String create(final TbUserCreateDTO tbUserDTO) {
        log.info("TbUserService.create - START");
        final TbUser tbUser = userMapper.mapToEntity(tbUserDTO);

        throwExceptionCaseLoginOrEmailAlreadyExists(tbUser);

        tbUser.setPassword(passwordEncoder.encode(tbUserDTO.password()));

        extractedProfiles(tbUserDTO.profileType(), tbUser);

        log.info("TbUserService.create - CONTINUE - Creating user: [{}]", tbUser);
        var response = tbUserRepository.save(tbUser).getId().toString();

        log.info("TbUserService.create - END - Created user: [{}]", response);
        return response;
    }

    private void throwExceptionCaseLoginOrEmailAlreadyExists(TbUser user) {
        if(tbUserRepository.existsByEmailOrLogin(user.getEmail(), user.getLogin())) {
            throw new RegisterFailedException(REGISTER_FAILED_EXCEPTION);
        }
    }

    public TbUserResponseDTO get(final String name) {
        log.info("TbUserService.get - START");

        var response = tbUserRepository.findByName(name)
                .map(userMapper::mapToDTO)
                .orElseThrow();

        log.info("TbUserService.get - END - Retrieved user: [{}]", name);
        return response;
    }

    public void update(final UUID id, final TbUserUpdateDTO userUpdateDTO) {
        log.info("TbUserService.update - START - Updating user: [{}]", id);
        final TbUser tbUser = findById(id);

        log.info("TbUserService.update - CONTINUE - Found user: [{}]", id);
        userMapper.updateUserFromDto(userUpdateDTO, tbUser);

        log.info("TbUserService.update - CONTINUE - Updated user: [{}]", id);

        if(existsUserWithEmail(userUpdateDTO.email(), id)){
            throw new RuntimeException("A user with the e-mail already exists");
        }

        tbUserRepository.save(tbUser);

        log.info("TbUserService.update - END - Updated user: [{}]", id);
    }

    private void extractedProfiles(ProfileType profileType, TbUser tbUser) {
        log.info("TbUserService.extractedProfiles - START");
        if (profileType != null) {
            final TbProfile profile = tbProfileRepository.findByType(profileType.name())
                    .orElseThrow(() -> new NotFoundException(
                            PROFILE_NOT_FOUND_EXCEPTION,
                            PROFILE_ENTITY_NAME,
                            null));
            tbUser.setProfiles(Set.of(profile));
        }
        log.info("TbUserService.extractedProfiles - END");
    }

    public void deleteUser(final UUID id) {
        log.info("TbUserService.deleteUser - START - Starting user deletion process for user: [{}]", id);
        final TbUser user = findById(id);

        log.info("TbUserService.deleteUser - CONTINUE - User found. Starting cleanup process. - userName: [{}], userEmail: [{}]", user.getName(), user.getEmail());
        publisher.publishEvent(new BeforeDeleteTbUser(id));

        log.info("TbUserService.deleteUser - CONTINUE - Clearing {} profile associations for user id: [{}]", user.getProfiles().size(), id);
        user.getProfiles().clear();

        tbUserRepository.save(user);
        log.info("TbUserService.deleteUser - CONTINUE - Deleting user with id: [{}]", id);

        tbUserRepository.delete(user);
        log.info("TbUserService.deleteUser - END - Deletion completed successfully for user: [{}]", id);
    }

    public void updatePassword(final UUID id, final TbUserUpdatePasswordDTO dto) {
        log.info("TbUserService.updatePassword - START - Starting password update process for user: [{}]", id);
        final TbUser tbUser = findById(id);

        log.info("TbUserService.updatePassword - CONTINUE - User found. Updating password for user: [{}]", id);
        tbUser.setPassword(passwordEncoder.encode(dto.password()));

        tbUserRepository.save(tbUser);
        log.info("TbUserService.updatePassword - END - Password updated successfully for user: [{}]", id);
    }


    private boolean existsUserWithEmail(String email, UUID idToExclude) {
        return tbUserRepository.findByEmail(email)
                .filter(tbUser -> !Objects.equals(tbUser.getId(), idToExclude))
                .isPresent();
    }

    @EventListener(BeforeDeleteTbProfile.class)
    public void on(final BeforeDeleteTbProfile event) {
        // remove many-to-many relations at owning side
        tbUserRepository.findAllByProfilesId(event.getId()).forEach(tbUser ->
                tbUser.getProfiles().removeIf(tbProfile -> tbProfile.getId().equals(event.getId())));
    }

    private TbUser findById(final UUID id) {
        return tbUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        USER_NOT_FOUND_EXCEPTION,
                        USER_ENTITY_NAME,
                        id.toString()));
    }

}
