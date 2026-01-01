package br.com.fiap.cheffy.service.security;

import br.com.fiap.cheffy.model.entities.User;
import br.com.fiap.cheffy.model.security.AuthenticatedUser;
import br.com.fiap.cheffy.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials")
                );

        return new AuthenticatedUser(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                mapProfilesToAuthorities(user)
        );
    }

    public AuthenticatedUser loadUserById(UUID id)
            throws UsernameNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid credentials")
                );

        return new AuthenticatedUser(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                mapProfilesToAuthorities(user)
        );
    }

    private Set<GrantedAuthority> mapProfilesToAuthorities(User user) {
        return user.getProfiles().stream()
                .map(profile ->
                        new SimpleGrantedAuthority("ROLE_" + profile.getType())
                )
                .collect(Collectors.toSet());
    }
}
