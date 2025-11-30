package br.com.fiap.cheffy.repos;

import br.com.fiap.cheffy.domain.TbProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TbProfileRepository extends JpaRepository<TbProfile, Long> {

    Optional<TbProfile> findByType(String type);
    Optional<TbProfile> findById(Long id);

}