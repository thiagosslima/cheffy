package br.com.fiap.cheffy.util;

import br.com.fiap.cheffy.domain.TbProfile;
import br.com.fiap.cheffy.domain.TbUser;
import br.com.fiap.cheffy.repos.TbProfileRepository;
import br.com.fiap.cheffy.repos.TbUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
   //TODO: Galera, essa classe é somente para teste, vou retirar depois
    @Bean
    CommandLineRunner initDatabase(
            TbUserRepository userRepository,
            TbProfileRepository profileRepository) {

        return args -> {
            if (userRepository.count() > 0) {
                return;
            };

            TbProfile adminProfile = new TbProfile();
            adminProfile.setType("ADMIN");
            adminProfile = profileRepository.save(adminProfile);

            TbProfile userProfile = new TbProfile();
            userProfile.setType("USER");
            userProfile = profileRepository.save(userProfile);

            TbUser admin = new TbUser();
            admin.setName("Admin User");
            admin.setEmail("admin@cheffy.com");
            admin.setLogin("admin");
            admin.setPassword("admin123");
            admin.getProfiles().add(adminProfile);
            userRepository.save(admin);

            TbUser user = new TbUser();
            user.setName("User");
            user.setEmail("user@cheffy.com");
            user.setLogin("user");
            user.setPassword("user123");
            user.getProfiles().add(userProfile);
            userRepository.save(user);
        };
    }
}