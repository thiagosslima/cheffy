package br.com.fiap.cheffy.rest;

import br.com.fiap.cheffy.model.TbUserCreateDTO;
import br.com.fiap.cheffy.model.TbUserResponseDTO;
import br.com.fiap.cheffy.model.TbUserUpdateDTO;
import br.com.fiap.cheffy.model.TbUserUpdatePasswordDTO;
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
        addLogTradeId();
        log.info("TbUserResource.getAllTbUsers - START - Find all users");
        var response = ResponseEntity.ok(tbUserService.findAll());
        log.info("TbUserResource.getAllTbUsers - END - Users found");
        MDC.clear();
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TbUserResponseDTO> getTbUser(@PathVariable(name = "id") final String id) {
        addLogTradeId();
        log.info("TbUserResource.getTbUser - START - Find user by id [{}]", id);
        var response = ResponseEntity.ok(tbUserService.get(UUID.fromString(id)));
        log.info("TbUserResource.getTbUser - END - User found [{}]", id);
        MDC.clear();
        return response;
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
        addLogTradeId();
        log.info("TbUserResource.createTbUser - START - Create user");
        var createdId = tbUserService.create(tbUserDTO);
        log.info("TbUserResource.createTbUser - END - User created with id [{}]", createdId);
        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UUID> updateTbUser(@PathVariable(name = "id") final UUID id,
                                             @RequestBody @Valid final TbUserUpdateDTO userUpdateDTO) {
        addLogTradeId();
        log.info("TbUserResource.updateTbUser - START - Update user");
        tbUserService.update(id, userUpdateDTO);
        log.info("TbUserResource.updateTbUser - END - User updated [{}]", id);
        MDC.clear();
        return ResponseEntity.ok(id);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable(name = "id") final String id,
                                                 @RequestBody @Valid final TbUserUpdatePasswordDTO tbUserUpdatePasswordDTO) {
        addLogTradeId();
        log.info("TbUserResource.updatePassword - START - Update password for user [{}]", id);
        tbUserService.updatePassword(UUID.fromString(id), tbUserUpdatePasswordDTO);
        var response = ResponseEntity.ok(id);
        log.info("TbUserResource.updatePassword - END - Password updated for user [{}]", id);
        MDC.clear();
        return response;
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final String id) {
        addLogTradeId();
        log.info("TbUserResource.deleteUser - START - Delete user: [{}]", id);
        tbUserService.deleteUser(UUID.fromString(id));
        log.info("TbUserResource.deleteUser - END - User deleted: [{}]", id);
        return ResponseEntity.noContent().build();
    }

    private static void addLogTradeId() {
        MDC.put("traceId", UUID.randomUUID().toString());
    }
}
