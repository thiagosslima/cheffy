package br.com.fiap.cheffy.repository;

import br.com.fiap.cheffy.model.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AddressRepository extends JpaRepository<Address, Long> {

    void deleteAllByUserId(UUID userId);

}
