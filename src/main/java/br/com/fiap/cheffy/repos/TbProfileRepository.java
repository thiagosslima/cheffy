package br.com.fiap.cheffy.repos;

import br.com.fiap.cheffy.domain.TbProfile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TbProfileRepository extends JpaRepository<TbProfile, Long> {
}
