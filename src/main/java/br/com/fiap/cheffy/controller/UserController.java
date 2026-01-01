package br.com.fiap.cheffy.controller;

import br.com.fiap.cheffy.model.dtos.UserCreateDTO;
import br.com.fiap.cheffy.model.dtos.UserResponseDTO;
import br.com.fiap.cheffy.model.dtos.UserUpdateDTO;
import br.com.fiap.cheffy.model.dtos.UserUpdatePasswordDTO;
import br.com.fiap.cheffy.service.UserService;
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
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllTbUsers() {
        addLogTradeId();
        log.info("UserController.getAllTbUsers - START - Find all users");
        var response = ResponseEntity.ok(userService.findAll());
        log.info("UserController.getAllTbUsers - END - Users found");
        MDC.clear();
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getTbUser(@PathVariable(name = "id") final String id) {
        addLogTradeId();
        log.info("UserController.getTbUser - START - Find user by id [{}]", id);
        var response = ResponseEntity.ok(userService.get(UUID.fromString(id)));
        log.info("UserController.getTbUser - END - User found [{}]", id);
        MDC.clear();
        return response;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserResponseDTO> getTbUserByName(@PathVariable(name = "name") final String name) {
        addLogTradeId();
        log.info("UserController.getTbUserByName - START - Find user by name [{}]", name);
        var response = ResponseEntity.ok(userService.get(name));
        log.info("UserController.getTbUserByName - END - Users found [{}]", name);
        MDC.clear();
        return response;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createTbUser(@RequestBody @Valid final UserCreateDTO tbUserDTO) {
        addLogTradeId();
        log.info("UserController.createTbUser - START - Create user");
        var createdId = userService.create(tbUserDTO);
        log.info("UserController.createTbUser - END - User created with id [{}]", createdId);
        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UUID> updateTbUser(@PathVariable(name = "id") final UUID id,
                                             @RequestBody @Valid final UserUpdateDTO userUpdateDTO) {
        addLogTradeId();
        log.info("UserController.updateTbUser - START - Update user");
        userService.update(id, userUpdateDTO);
        log.info("UserController.updateTbUser - END - User updated [{}]", id);
        MDC.clear();
        return ResponseEntity.ok(id);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable(name = "id") final String id,
                                                 @RequestBody @Valid final UserUpdatePasswordDTO userUpdatePasswordDTO) {
        addLogTradeId();
        log.info("UserController.updatePassword - START - Update password for user [{}]", id);
        userService.updatePassword(UUID.fromString(id), userUpdatePasswordDTO);
        var response = ResponseEntity.ok(id);
        log.info("UserController.updatePassword - END - Password updated for user [{}]", id);
        MDC.clear();
        return response;
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final String id) {
        addLogTradeId();
        log.info("UserController.deleteUser - START - Delete user: [{}]", id);
        userService.deleteUser(UUID.fromString(id));
        log.info("UserController.deleteUser - END - User deleted: [{}]", id);
        return ResponseEntity.noContent().build();
    }

    private static void addLogTradeId() {
        MDC.put("traceId", UUID.randomUUID().toString());
    }
}
