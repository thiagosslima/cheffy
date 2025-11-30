package br.com.fiap.cheffy.domain.stub;

import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.model.TbUserDTO;

import java.time.OffsetDateTime;
import java.util.List;

public class TbUserStub {

    public static TbUser buildDomain(){
        TbUser tbUser = new TbUser();
        tbUser.setId(1L);
        tbUser.setName("John Doe");
        tbUser.setEmail("john@test.com");
        tbUser.setPassword("password123");
        tbUser.setProfiles(null);
        tbUser.setAddresses(null);
        tbUser.setDateCreated(OffsetDateTime.now());
        tbUser.setLastUpdated(null);
        return tbUser;
    }

    public static TbUserDTO buildDTO(){
        TbUserDTO tbUserDTO = new TbUserDTO();
        tbUserDTO.setId(1L);
        tbUserDTO.setName("John Doe");
        tbUserDTO.setEmail("john@test.com");
        tbUserDTO.setPassword("password123");
        tbUserDTO.setProfiles(List.of(1L, 2L, 3L));
        return tbUserDTO;
    }
}
