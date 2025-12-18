package br.com.fiap.cheffy.repos;

import br.com.fiap.cheffy.domain.TbUser;
import java.util.List;
import java.util.UUID;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TbUserRepository extends JpaRepository<TbUser, UUID> {

    List<TbUser> findAllByProfilesId(Long id);

    boolean existsByEmailIgnoreCase(String email);

    Optional<TbUser> findByName(String name);

    boolean existsByLogin(String login);


}
