package com.zup.secureBasic.config;

import com.zup.secureBasic.models.User;
import com.zup.secureBasic.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Criptografa a senha
                admin.setRole("ROLE_ADMIN");
                admin.setDepartment("IT");
                userRepository.save(admin);
                System.out.println("Usuário admin criado com sucesso!");
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123")); // Criptografa a senha
                user.setRole("ROLE_USER");
                user.setDepartment("Finance");
                userRepository.save(user);
                System.out.println("Usuário user criado com sucesso!");
            }
        };
    }
}
