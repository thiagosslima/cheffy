package br.com.fiap.cheffy.service.security;

import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.model.security.AuthenticatedUser;
import br.com.fiap.cheffy.repos.TbUserRepository;
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

    private final TbUserRepository tbUserRepository;

    public CustomUserDetailsService(TbUserRepository tbUserRepository) {
        this.tbUserRepository = tbUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        TbUser user = tbUserRepository.findByLogin(login)
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

        TbUser user = tbUserRepository.findById(id)
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

    private Set<GrantedAuthority> mapProfilesToAuthorities(TbUser user) {
        return user.getProfiles().stream()
                .map(profile ->
                        new SimpleGrantedAuthority("ROLE_" + profile.getType())
                )
                .collect(Collectors.toSet());
    }
}
