package br.com.fiap.cheffy.repos;

import br.com.fiap.cheffy.domain.TbUser;
import java.util.List;
import java.util.UUID;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TbUserRepository extends JpaRepository<TbUser, UUID> {

    List<TbUser> findAllByProfilesId(Long id);

    boolean existsByEmailIgnoreCase(String email);

    Optional<TbUser> findByName(String name);

    Optional<TbUser> findByEmail(String email);

    boolean existsByEmailOrLogin(String email,  String login);

    @Query("""
            SELECT distinct u FROM TbUser u 
                    JOIN FETCH u.profiles
                WHERE u.id = :id 
            """)
    Optional<TbUser> findById(@Param("id") UUID id);

    @Query("""
            SELECT distinct u FROM TbUser u 
                    JOIN FETCH u.profiles
                WHERE u.login = :login 
            """)
    Optional<TbUser> findByLogin(@Param("login") String login);


}
