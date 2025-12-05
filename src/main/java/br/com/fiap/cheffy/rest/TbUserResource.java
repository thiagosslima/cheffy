package br.com.fiap.cheffy.rest;

import br.com.fiap.cheffy.model.TbUserDTO;
import br.com.fiap.cheffy.service.TbUserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/tbUsers", produces = MediaType.APPLICATION_JSON_VALUE)
public class TbUserResource {

    private final TbUserService tbUserService;

    public TbUserResource(final TbUserService tbUserService) {
        this.tbUserService = tbUserService;
    }

    @GetMapping
    public ResponseEntity<List<TbUserDTO>> getAllTbUsers() {
        return ResponseEntity.ok(tbUserService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TbUserDTO> getTbUser(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(tbUserService.get(id));
    }

    @GetMapping("/{name}")
    public ResponseEntity<TbUserDTO> getTbUser(@PathVariable(name = "name") final String name) {
        addLogTradeId();
        var response = ResponseEntity.ok(tbUserService.get(name));
        MDC.clear();
        return response;
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTbUser(@RequestBody @Valid final TbUserDTO tbUserDTO) {
        final Long createdId = tbUserService.create(tbUserDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTbUser(@PathVariable(name = "id") final Long id,
                                             @RequestBody @Valid final TbUserDTO tbUserDTO) {
        tbUserService.update(id, tbUserDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTbUser(@PathVariable(name = "id") final Long id) {
        tbUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private static void addLogTradeId() {
        MDC.put("traceId", UUID.randomUUID().toString());
    }
}
