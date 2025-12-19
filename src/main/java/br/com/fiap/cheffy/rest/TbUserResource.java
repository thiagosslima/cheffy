package br.com.fiap.cheffy.rest;

import br.com.fiap.cheffy.model.TbUserCreateDTO;
import br.com.fiap.cheffy.model.TbUserResponseDTO;
import br.com.fiap.cheffy.service.TbUserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
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

    @GetMapping("/name/{name}")
    public ResponseEntity<TbUserResponseDTO> getTbUserByName(@PathVariable(name = "name") final String name) {
        addLogTradeId();
        log.info("TbUserResource.getTbUserByName - START - Find user by name [{}]", name);
        var response = ResponseEntity.ok(tbUserService.get(name));
        log.info("TbUserResource.getTbUserByName - END - Users found [{}]", name);
        MDC.clear();
        return response;
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
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final String id) {
        log.info("TbUserResource.deleteUser - START - Delete user: [{}]", id);
        tbUserService.deleteUser(UUID.fromString(id));
        log.info("TbUserResource.deleteUser - END - User deleted: [{}]", id);
        return ResponseEntity.noContent().build();
    }

    private static void addLogTradeId() {
        MDC.put("traceId", UUID.randomUUID().toString());
    }
}
