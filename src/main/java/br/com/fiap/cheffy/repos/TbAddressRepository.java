package br.com.fiap.cheffy.repos;

import br.com.fiap.cheffy.domain.TbAddress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TbAddressRepository extends JpaRepository<TbAddress, Long> {

    TbAddress findFirstByUserId(Long id);

}
