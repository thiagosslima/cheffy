package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.ExceptionsKeys;
import br.com.fiap.cheffy.domain.TbAddress;
import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.model.TbAddressDTO;
import br.com.fiap.cheffy.repos.TbAddressRepository;
import br.com.fiap.cheffy.repos.TbUserRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static br.com.fiap.cheffy.domain.ExceptionsKeys.ADRESS_NOT_FOUND_EXCEPTION;


@Slf4j
@Service
public class TbAddressService {

    private final TbAddressRepository tbAddressRepository;
    private final TbUserRepository tbUserRepository;

    private static final String ENTITY_NAME = "Address";

    public TbAddressService(final TbAddressRepository tbAddressRepository,
            final TbUserRepository tbUserRepository) {
        this.tbAddressRepository = tbAddressRepository;
        this.tbUserRepository = tbUserRepository;
    }

    public List<TbAddressDTO> findAll() {
        final List<TbAddress> tbAddresses = tbAddressRepository.findAll(Sort.by("id"));
        return tbAddresses.stream()
                .map(tbAddress -> mapToDTO(tbAddress, new TbAddressDTO()))
                .toList();
    }

    public TbAddressDTO get(final Long id) {
        return tbAddressRepository.findById(id)
                .map(tbAddress -> mapToDTO(tbAddress, new TbAddressDTO()))
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
    }

    public Long create(final TbAddressDTO tbAddressDTO) {
        final TbAddress tbAddress = new TbAddress();
        mapToEntity(tbAddressDTO, tbAddress);
        return tbAddressRepository.save(tbAddress).getId();
    }

    public void update(final Long id, final TbAddressDTO tbAddressDTO) {
        final TbAddress tbAddress = tbAddressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        mapToEntity(tbAddressDTO, tbAddress);
        tbAddressRepository.save(tbAddress);
    }

    public void delete(final Long id) {
        final TbAddress tbAddress = tbAddressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        id.toString()));
        tbAddressRepository.delete(tbAddress);
    }

    private TbAddressDTO mapToDTO(final TbAddress tbAddress, final TbAddressDTO tbAddressDTO) {
        tbAddressDTO.setId(tbAddress.getId());
        tbAddressDTO.setStreetName(tbAddress.getStreetName());
        tbAddressDTO.setNumber(tbAddress.getNumber());
        tbAddressDTO.setCity(tbAddress.getCity());
        tbAddressDTO.setPostalCode(tbAddress.getPostalCode());
        tbAddressDTO.setNeighborhood(tbAddress.getNeighborhood());
        tbAddressDTO.setStateProvince(tbAddress.getStateProvince());
        tbAddressDTO.setAddressLine(tbAddress.getAddressLine());
        tbAddressDTO.setMain(tbAddress.getMain());
        tbAddressDTO.setUser(tbAddress.getUser() == null ? null : tbAddress.getUser().getId());
        return tbAddressDTO;
    }

    private TbAddress mapToEntity(final TbAddressDTO tbAddressDTO, final TbAddress tbAddress) {
        tbAddress.setStreetName(tbAddressDTO.getStreetName());
        tbAddress.setNumber(tbAddressDTO.getNumber());
        tbAddress.setCity(tbAddressDTO.getCity());
        tbAddress.setPostalCode(tbAddressDTO.getPostalCode());
        tbAddress.setNeighborhood(tbAddressDTO.getNeighborhood());
        tbAddress.setStateProvince(tbAddressDTO.getStateProvince());
        tbAddress.setAddressLine(tbAddressDTO.getAddressLine());
        tbAddress.setMain(tbAddressDTO.getMain());
        final TbUser user = tbAddressDTO.getUser() == null ? null : tbUserRepository.findById(tbAddressDTO.getUser())
                .orElseThrow(() -> new NotFoundException(
                        ADRESS_NOT_FOUND_EXCEPTION,
                        ENTITY_NAME,
                        tbAddressDTO.getUser().toString()));
        tbAddress.setUser(user);
        return tbAddress;
    }

    @EventListener(BeforeDeleteTbUser.class)
    public void onBeforeDeleteTbUser(final BeforeDeleteTbUser event) {
        log.info("TbAddressService.onBeforeDeleteTbUser - START - Deleting all addresses for user: [{}]", event.getId());
        tbAddressRepository.deleteAllByUserId(event.getId());
        log.info("TbAddressService.onBeforeDeleteTbUser - END - Deleted all address related to user: [{}]", event.getId());
    }

}
