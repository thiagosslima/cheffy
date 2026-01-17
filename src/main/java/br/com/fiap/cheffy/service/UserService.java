package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.exceptions.InvalidOperationException;
import br.com.fiap.cheffy.mapper.AddressMapper;
import br.com.fiap.cheffy.model.dtos.*;
import br.com.fiap.cheffy.model.entities.Address;
import br.com.fiap.cheffy.model.enums.ProfileType;
import br.com.fiap.cheffy.model.entities.Profile;
import br.com.fiap.cheffy.model.entities.User;
import br.com.fiap.cheffy.events.BeforeDeleteTbProfile;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.exceptions.RegisterFailedException;
import br.com.fiap.cheffy.mapper.UserMapper;
import br.com.fiap.cheffy.mapper.UserUpdateMapper;
import br.com.fiap.cheffy.repository.ProfileRepository;
import br.com.fiap.cheffy.repository.UserRepository;

import java.util.*;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.fiap.cheffy.model.enums.ExceptionsKeys.*;


@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher publisher;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUpdateMapper userUpdateMapper;
    private final AddressMapper addressMapper;
    private final EntityManager entityManager;


    private static final String USER_ENTITY_NAME = "User";
    private static final String PROFILE_ENTITY_NAME = "Profile";

    public UserService(
            final UserRepository userRepository,
            final ProfileRepository profileRepository,
            final ApplicationEventPublisher publisher,
            final UserMapper userMapper,
            final PasswordEncoder passwordEncoder,
            final UserUpdateMapper userUpdateMapper,
            final AddressMapper addressMapper,
            final EntityManager entityManager) {

        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.publisher = publisher;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userUpdateMapper = userUpdateMapper;
        this.addressMapper = addressMapper;
        this.entityManager = entityManager;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(UUID userId, Long addressId, AddressPatchDTO dto) {

        log.info("UserService.updateAddress - START");

        User user = findByIdOrFail(userId);

        Address address = user.findAddressByIdOrFail(addressId);

        addressMapper.updateAddressFromDto(dto, address);

        if (Boolean.TRUE.equals(dto.getMain())) {
            user.setMainAddress(address);
        }

        log.info("UserService.updateAddress - END");
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAddress(final UUID userId, final Long addressId) {

        log.info("UserService.removeAddress - START");

        final User user = findByIdOrFail(userId);
        final Address address = user.findAddressByIdOrFail(addressId);

        user.removeAddress(address);

        log.info("UserService.removeAddress - END - ");

    }

    @Transactional(rollbackFor = Exception.class)
    public Long addAddress(final UUID userId, final AddressCreateDTO dto) {
        log.info("UserService.addAddress - START");
        final User user = findByIdOrFail(userId);

        final Address address = addressMapper.mapToEntity(dto);

        user.addAddress(address);

        entityManager.flush();

        log.info("UserService.addAddress - END - Created address: [{}]", address.getId());

        return address.getId();

    }


    public List<UserResponseDTO> findAll() {
        log.info("UserService.findAll - START");

        final List<User> users = userRepository.findAll(Sort.by("id"));

        var response = users.stream()
                .map(userMapper::mapToDTO)
                .toList();

        log.info("UserService.findAll - END - Retrieved {} users", response.size());
        return response;
    }

    public UserResponseDTO get(final UUID id) {
        log.info("UserService.get - START");
        var response = userMapper.mapToDTO(findByIdOrFail(id));

        log.info("UserService.get - END - Retrieved user: [{}]", id);
        return response;
    }

    public String create(final UserCreateDTO dto) {
        log.info("UserService.create - START");

        final User user = userMapper.mapToEntity(dto);

        throwExceptionCaseLoginOrEmailAlreadyExists(user);

        user.setPassword(passwordEncoder.encode(dto.password()));

        extractedProfiles(dto.profileType(), user);

        final Address address = addressMapper.mapToEntity(dto.address());
        user.addUserMainAddress(address);

        log.info("UserService.create - CONTINUE - Creating user: [{}]", user);

        var response = userRepository.save(user).getId().toString();

        log.info("UserService.create - END - Created user [{}] with main address [{}]", response, address.getId());

        return response;
    }

    private void throwExceptionCaseLoginOrEmailAlreadyExists(User user) {
        if(userRepository.existsByEmailOrLogin(user.getEmail(), user.getLogin())) {
            throw new RegisterFailedException(REGISTER_FAILED_EXCEPTION);
        }
    }

    public UserResponseDTO get(final String name) {
        log.info("UserService.get - START");

        var response = userRepository.findByName(name)
                .map(userMapper::mapToDTO)
                .orElseThrow(() -> new NotFoundException(
                        USER_NOT_FOUND_EXCEPTION,
                        USER_ENTITY_NAME));

        log.info("UserService.get - END - Retrieved user: [{}]", name);
        return response;
    }

    public void update(final UUID id, final UserUpdateDTO userUpdateDTO) {
        log.info("UserService.update - START - Updating user: [{}]", id);
        final User user = findByIdOrFail(id);

        log.info("UserService.update - CONTINUE - Found user: [{}]", id);

        if(userUpdateDTO.email() != null && existsUserWithEmail(userUpdateDTO.email(), id)){
            log.warn("UserService.update - Attempt to use existing email. User: [{}], Email: [{}]", id, userUpdateDTO.email());
            throw new InvalidOperationException();
        }

        userUpdateMapper.updateEntityFromDto(userUpdateDTO, user);

        userRepository.save(user);

        log.info("UserService.update - END - Updated user: [{}]", id);
    }

    private void extractedProfiles(ProfileType profileType, User user) {
        log.info("UserService.extractedProfiles - START");
        if (profileType != null) {
            final Profile profile = profileRepository.findByType(profileType.name())
                    .orElseThrow(() -> new NotFoundException(
                            PROFILE_NOT_FOUND_EXCEPTION,
                            PROFILE_ENTITY_NAME,
                            null));
            user.setProfiles(Set.of(profile));
        }
        log.info("UserService.extractedProfiles - END");
    }

    public void deleteUser(final UUID id) {
        log.info("UserService.deleteUser - START - Starting user deletion process for user: [{}]", id);
        final User user = findByIdOrFail(id);

        log.info("UserService.deleteUser - CONTINUE - User found. Starting cleanup process. - userName: [{}], userEmail: [{}]", user.getName(), user.getEmail());
        publisher.publishEvent(new BeforeDeleteTbUser(id));

        log.info("UserService.deleteUser - CONTINUE - Clearing {} profile associations for user id: [{}]", user.getProfiles().size(), id);
        user.getProfiles().clear();

        userRepository.save(user);
        log.info("UserService.deleteUser - CONTINUE - Deleting user with id: [{}]", id);

        userRepository.delete(user);
        log.info("UserService.deleteUser - END - Deletion completed successfully for user: [{}]", id);
    }

    public void updatePassword(final UUID id, final UserUpdatePasswordDTO dto) {
        log.info("UserService.updatePassword - START - Starting password update process for user: [{}]", id);
        final User user = findByIdOrFail(id);

        log.info("UserService.updatePassword - CONTINUE - User found. Updating password for user: [{}]", id);
        user.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(user);
        log.info("UserService.updatePassword - END - Password updated successfully for user: [{}]", id);
    }


    private boolean existsUserWithEmail(String email, UUID idToExclude) {
        return userRepository.findByEmail(email)
                .filter(tbUser -> !Objects.equals(tbUser.getId(), idToExclude))
                .isPresent();
    }

    @EventListener(BeforeDeleteTbProfile.class)
    public void on(final BeforeDeleteTbProfile event) {
        // remove many-to-many relations at owning side
        userRepository.findAllByProfilesId(event.getId()).forEach(tbUser ->
                tbUser.getProfiles().removeIf(tbProfile -> tbProfile.getId().equals(event.getId())));
    }

    private User findByIdOrFail(final UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        USER_NOT_FOUND_EXCEPTION,
                        USER_ENTITY_NAME,
                        id.toString()));
    }

}
