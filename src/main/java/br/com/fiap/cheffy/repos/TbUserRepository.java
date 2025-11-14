package br.com.fiap.cheffy.repos;

import br.com.fiap.cheffy.domain.TbUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TbUserRepository extends JpaRepository<TbUser, Long> {

    List<TbUser> findAllByProfilesId(Long id);

    boolean existsByEmailIgnoreCase(String email);

}
