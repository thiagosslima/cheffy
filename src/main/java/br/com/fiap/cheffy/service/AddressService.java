package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.model.entities.Address;
import br.com.fiap.cheffy.model.entities.User;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.model.dtos.AddressDTO;
import br.com.fiap.cheffy.repository.AddressRepository;
import br.com.fiap.cheffy.repository.UserRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static br.com.fiap.cheffy.model.enums.ExceptionsKeys.ADRESS_NOT_FOUND_EXCEPTION;


@Slf4j
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    private static final String ENTITY_NAME = "Address";

    public AddressService(final AddressRepository addressRepository,
                          final UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressDTO> findAll() {
        final List<Address> addresses = addressRepository.findAll(Sort.by("id"));
        return addresses.stream()
                .map(address -> mapToDTO(address, new AddressDTO()))
                .toList();
    }

    public AddressDTO get(final Long id) {
        return addressRepository.findById(id)
                .map(address -> mapToDTO(address, new AddressDTO()))
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
    }

    public Long create(final AddressDTO addressDTO) {
        final Address address = new Address();
        mapToEntity(addressDTO, address);
        return addressRepository.save(address).getId();
    }

    public void update(final Long id, final AddressDTO addressDTO) {
        final Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        mapToEntity(addressDTO, address);
        addressRepository.save(address);
    }

    public void delete(final Long id) {
        final Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        addressRepository.delete(address);
    }

    private AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setStreetName(address.getStreetName());
        addressDTO.setNumber(address.getNumber());
        addressDTO.setCity(address.getCity());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setNeighborhood(address.getNeighborhood());
        addressDTO.setStateProvince(address.getStateProvince());
        addressDTO.setAddressLine(address.getAddressLine());
        addressDTO.setMain(address.getMain());
        addressDTO.setUser(address.getUser() == null ? null : address.getUser().getId());
        return addressDTO;
    }

    private Address mapToEntity(final AddressDTO addressDTO, final Address address) {
        address.setStreetName(addressDTO.getStreetName());
        address.setNumber(addressDTO.getNumber());
        address.setCity(addressDTO.getCity());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setNeighborhood(addressDTO.getNeighborhood());
        address.setStateProvince(addressDTO.getStateProvince());
        address.setAddressLine(addressDTO.getAddressLine());
        address.setMain(addressDTO.getMain());
        final User user = addressDTO.getUser() == null ? null : userRepository.findById(addressDTO.getUser())
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        addressDTO.getUser().toString()));
        address.setUser(user);
        return address;
    }

    @EventListener(BeforeDeleteTbUser.class)
    public void onBeforeDeleteTbUser(final BeforeDeleteTbUser event) {
        log.info("AddressService.onBeforeDeleteTbUser - START - Deleting all addresses for user: [{}]", event.getId());
        addressRepository.deleteAllByUserId(event.getId());
        log.info("AddressService.onBeforeDeleteTbUser - END - Deleted all address related to user: [{}]", event.getId());
    }

}
