package br.com.fiap.cheffy.rest;

import br.com.fiap.cheffy.model.TbUserCreateDTO;
import br.com.fiap.cheffy.model.TbUserResponseDTO;
import br.com.fiap.cheffy.service.TbUserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

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
@RequestMapping(value = "/api/tbUsers", produces = MediaType.APPLICATION_JSON_VALUE)
public class TbUserResource {

    private final TbUserService tbUserService;

    public TbUserResource(final TbUserService tbUserService) {
        this.tbUserService = tbUserService;
    }

    @GetMapping
    public ResponseEntity<List<TbUserResponseDTO>> getAllTbUsers() {
        return ResponseEntity.ok(tbUserService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TbUserResponseDTO> getTbUser(@PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(tbUserService.get(UUID.fromString(id)));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createTbUser(@RequestBody @Valid final TbUserCreateDTO tbUserDTO) {
        final String createdId = tbUserService.create(tbUserDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTbUser(@PathVariable(name = "id") final String id,
            @RequestBody @Valid final TbUserCreateDTO tbUserDTO) {
        tbUserService.update(UUID.fromString(id), tbUserDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTbUser(@PathVariable(name = "id") final String id) {
        tbUserService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
