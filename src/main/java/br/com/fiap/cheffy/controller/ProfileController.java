package br.com.fiap.cheffy.controller;

import br.com.fiap.cheffy.model.dtos.ProfileDTO;
import br.com.fiap.cheffy.service.ProfileService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(final ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAllTbProfiles() {
        return ResponseEntity.ok(profileService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getTbProfile(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(profileService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTbProfile(
            @RequestBody @Valid final ProfileDTO profileDTO) {
        final Long createdId = profileService.create(profileDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTbProfile(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProfileDTO profileDTO) {
        profileService.update(id, profileDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTbProfile(@PathVariable(name = "id") final Long id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
