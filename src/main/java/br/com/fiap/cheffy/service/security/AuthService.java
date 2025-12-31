package br.com.fiap.cheffy.service.security;

import br.com.fiap.cheffy.exceptions.LoginFailedException;
import br.com.fiap.cheffy.model.LoginRequestDTO;
import br.com.fiap.cheffy.model.TokenResponseDTO;
import br.com.fiap.cheffy.model.security.AuthenticatedUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import static br.com.fiap.cheffy.domain.ExceptionsKeys.LOGIN_FAILED_EXCEPTION;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO doLogin(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.login(),
                            loginRequestDTO.password()
                    )
            );
        String token = jwtService.generateToken((AuthenticatedUser) authentication.getPrincipal());
            return new TokenResponseDTO(token);
        } catch (AuthenticationException e) {
            throw new LoginFailedException(LOGIN_FAILED_EXCEPTION,e.getMessage());
        }
    }

}
