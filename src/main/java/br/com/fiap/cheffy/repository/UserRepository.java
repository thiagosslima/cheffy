package br.com.fiap.cheffy.repository;

import br.com.fiap.cheffy.model.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findAllByProfilesId(Long id);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    List<User> findAll();

    @Query("""
            SELECT distinct u FROM User u 
                    JOIN FETCH u.profiles
                    LEFT JOIN FETCH u.addresses
                WHERE u.name = :name 
            """)
    Optional<User> findByName(@Param("name") String name);

    Optional<User> findByEmail(String email);

    boolean existsByEmailOrLogin(String email, String login);

    @Query("""
            SELECT distinct u FROM User u 
                    JOIN FETCH u.profiles
                    LEFT JOIN FETCH u.addresses
                WHERE u.id = :id 
            """)
    Optional<User> findById(@Param("id") UUID id);

    @Query("""
            SELECT distinct u FROM User u 
                    JOIN FETCH u.profiles
                WHERE u.login = :login 
            """)
    Optional<User> findByLogin(@Param("login") String login);


}
