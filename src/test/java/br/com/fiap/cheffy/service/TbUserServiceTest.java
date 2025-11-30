package br.com.fiap.cheffy.service;

import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.domain.stub.TbUserStub;
import br.com.fiap.cheffy.repos.TbUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TbUserServiceTest {

    @Mock
    private TbUserRepository repository;

    @InjectMocks
    private TbUserService service;

    private TbUser tbUser;

    @BeforeEach
    void setUp() {
        tbUser = TbUserStub.buildDomain();
    }

    @Test
    void getByName_found(){
        when(repository.findByName("Joao")).thenReturn(Optional.of(tbUser));
        var dto = service.get("Joao");
        assertEquals("Joao", dto.getName());
    }
}