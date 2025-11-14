package br.com.fiap.cheffy.rest;

import br.com.fiap.cheffy.model.TbProfileDTO;
import br.com.fiap.cheffy.service.TbProfileService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


@RestController
@RequestMapping(value = "/api/tbProfiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class TbProfileResource {

    private final TbProfileService tbProfileService;

    public TbProfileResource(final TbProfileService tbProfileService) {
        this.tbProfileService = tbProfileService;
    }

    @GetMapping
    public ResponseEntity<List<TbProfileDTO>> getAllTbProfiles() {
        return ResponseEntity.ok(tbProfileService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TbProfileDTO> getTbProfile(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tbProfileService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTbProfile(
            @RequestBody @Valid final TbProfileDTO tbProfileDTO) {
        final Long createdId = tbProfileService.create(tbProfileDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTbProfile(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TbProfileDTO tbProfileDTO) {
        tbProfileService.update(id, tbProfileDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTbProfile(@PathVariable(name = "id") final Long id) {
        tbProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
