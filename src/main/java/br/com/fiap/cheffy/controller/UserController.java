package br.com.fiap.cheffy.controller;

import br.com.fiap.cheffy.model.dtos.*;
import br.com.fiap.cheffy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "User",
        description = "Endpoints para gerenciamento do ciclo de vida de usuários: criação, consulta, atualização e exclusão"
)
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    @Operation(summary = "Remover endereço do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou endereço não encontrado"),
            @ApiResponse(responseCode = "400", description = "Operação não permitida"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> removeAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId) {

        addLogTradeId();
        log.info("UserController.removeAddress - START - User [{}] Address [{}]", userId, addressId);

        userService.removeAddress(userId, addressId);

        log.info("UserController.removeAddress - END");
        MDC.clear();

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/addresses/{addressId}")
    @Operation(summary = "Atualizar parcialmente um endereço do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário ou endereço não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> updateAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId,
            @RequestBody AddressPatchDTO dto) {

        addLogTradeId();
        log.info("UserController.updateAddress - START - User [{}] Address [{}]", userId, addressId);

        userService.updateAddress(userId, addressId, dto);

        log.info("UserController.updateAddress - END");
        MDC.clear();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/addresses")
    @Operation(summary = "Adicionar novo endereço ao usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Long> addAddress(
            @PathVariable UUID userId,
            @RequestBody @Valid AddressCreateDTO dto) {

        addLogTradeId();
        log.info("UserController.addAddress - START - User [{}]", userId);

        Long id = userService.addAddress(userId, dto);

        log.info("UserController.addAddress - END");
        MDC.clear();

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content =
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<List<UserResponseDTO>> getAllTbUsers() {
        addLogTradeId();
        log.info("UserController.getAllTbUsers - START - Find all users");
        var response = ResponseEntity.ok(userService.findAll());
        log.info("UserController.getAllTbUsers - END - Users found");
        MDC.clear();
        return response;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna dados detalhados de um usuário específico através do UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o ID fornecido"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<UserResponseDTO> getTbUser(@PathVariable(name = "id") final String id) {
        addLogTradeId();
        log.info("UserController.getTbUser - START - Find user by id [{}]", id);
        var response = ResponseEntity.ok(userService.get(UUID.fromString(id)));
        log.info("UserController.getTbUser - END - User found [{}]", id);
        MDC.clear();
        return response;
    }

    @GetMapping("/name/{name}")
    @Operation(
            summary = "Buscar usuário por name",
            description = "Retorna dados de um usuário através do nome de usuário"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o name fornecido"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<UserResponseDTO> getTbUserByName(@PathVariable(name = "name") final String name) {
        addLogTradeId();
        log.info("UserController.getTbUserByName - START - Find user by name [{}]", name);
        var response = ResponseEntity.ok(userService.get(name));
        log.info("UserController.getTbUserByName - END - Users found [{}]", name);
        MDC.clear();
        return response;
    }

    @PostMapping
    @Operation(
            summary = "Criar novo usuário",
            description = "Cadastra novo usuário"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso - Retorna UUID do novo usuário",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "string", format = "uuid")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou malformados"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "409", description = "Conflito - Email ou Login já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<String> createTbUser(@RequestBody @Valid final UserCreateDTO tbUserDTO) {
        addLogTradeId();
        log.info("UserController.createTbUser - START - Create user");
        var createdId = userService.create(tbUserDTO);
        log.info("UserController.createTbUser - END - User created with id [{}]", createdId);
        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualização parcial - apenas campos enviados são modificados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de atualização inválidos"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito - Email já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<Void> updateTbUser(@PathVariable(name = "id") final UUID id,
                                             @RequestBody @Valid final UserUpdateDTO userUpdateDTO) {
        addLogTradeId();
        log.info("UserController.updateTbUser - START - Update user");
        userService.update(id, userUpdateDTO);
        log.info("UserController.updateTbUser - END - User updated [{}]", id);
        MDC.clear();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Atualizar senha do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nova senha inválida"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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
    @Operation(summary = "Deletar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "ID inválido - formato UUID incorreto"),
            @ApiResponse(responseCode = "401", description = "Token expirado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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
