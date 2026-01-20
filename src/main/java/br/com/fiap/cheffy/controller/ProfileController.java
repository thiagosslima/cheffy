package br.com.fiap.cheffy.controller;

import br.com.fiap.cheffy.model.dtos.ProfileDTO;
import br.com.fiap.cheffy.service.ProfileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Profile",
        description = "Endpoints para gerenciamento de perfis de usuário"
)
@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os perfis")
    @ApiResponse(responseCode = "200", description = "Lista de perfis retornada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<List<ProfileDTO>> getAllTbProfiles() {
        return ResponseEntity.ok(profileService.findAll());
    }

    @GetMapping("/{id}")
    @Hidden
    @Operation(summary = "Buscar perfil por ID")
    public ResponseEntity<ProfileDTO> getTbProfile(
            @PathVariable(name = "id") final Long id) {

        return ResponseEntity.ok(profileService.get(id));
    }

    @PostMapping
    @Hidden
    @Operation(summary = "Criar um novo perfil")
    public ResponseEntity<Long> createTbProfile(
            @RequestBody @Valid final ProfileDTO profileDTO) {

        final Long createdId = profileService.create(profileDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Hidden
    @Operation(summary = "Atualizar um perfil existente")
    public ResponseEntity<Long> updateTbProfile(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProfileDTO profileDTO) {

        profileService.update(id, profileDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um perfil")
    @Hidden
    public ResponseEntity<Void> deleteTbProfile(
            @PathVariable(name = "id") final Long id) {

        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
