package br.com.fiap.cheffy.rest;

import br.com.fiap.cheffy.model.LoginRequestDTO;
import br.com.fiap.cheffy.model.TokenResponseDTO;
import br.com.fiap.cheffy.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return authService.doLogin(loginRequestDTO);
    }
}
