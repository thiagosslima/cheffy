package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.TbAddress;
import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.events.BeforeDeleteTbUser;
import br.com.fiap.cheffy.model.TbAddressDTO;
import br.com.fiap.cheffy.repos.TbAddressRepository;
import br.com.fiap.cheffy.repos.TbUserRepository;
import br.com.fiap.cheffy.util.NotFoundException;
import br.com.fiap.cheffy.util.ReferencedException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TbAddressService {

    private final TbAddressRepository tbAddressRepository;
    private final TbUserRepository tbUserRepository;

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
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TbAddressDTO tbAddressDTO) {
        final TbAddress tbAddress = new TbAddress();
        mapToEntity(tbAddressDTO, tbAddress);
        return tbAddressRepository.save(tbAddress).getId();
    }

    public void update(final Long id, final TbAddressDTO tbAddressDTO) {
        final TbAddress tbAddress = tbAddressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(tbAddressDTO, tbAddress);
        tbAddressRepository.save(tbAddress);
    }

    public void delete(final Long id) {
        final TbAddress tbAddress = tbAddressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
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
                .orElseThrow(() -> new NotFoundException("user not found"));
        tbAddress.setUser(user);
        return tbAddress;
    }

    @EventListener(BeforeDeleteTbUser.class)
    public void on(final BeforeDeleteTbUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final TbAddress userTbAddress = tbAddressRepository.findFirstByUserId(event.getId());
        if (userTbAddress != null) {
            referencedException.setKey("tbUser.tbAddress.user.referenced");
            referencedException.addParam(userTbAddress.getId());
            throw referencedException;
        }
    }

}
