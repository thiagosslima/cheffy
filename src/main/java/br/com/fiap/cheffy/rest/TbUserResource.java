package br.com.fiap.cheffy.rest;

import br.com.fiap.cheffy.model.TbUserDTO;
import br.com.fiap.cheffy.model.TbUserUpdateDTO;
import br.com.fiap.cheffy.service.TbUserService;
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

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTbUser(@RequestBody @Valid final TbUserDTO tbUserDTO) {
        final Long createdId = tbUserService.create(tbUserDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTbUser(@PathVariable(name = "id") final Long id,
                                             @RequestBody @Valid final TbUserUpdateDTO userUpdateDTO) {
        tbUserService.update(id, userUpdateDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTbUser(@PathVariable(name = "id") final Long id) {
        tbUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
