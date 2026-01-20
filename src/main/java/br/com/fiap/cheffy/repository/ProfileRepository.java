package br.com.fiap.cheffy.repository;

import br.com.fiap.cheffy.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByType(String type);
    Optional<Profile> findById(Long id);

}