package com.zup.secureBasic.infra.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserStore {
    private static final Map<String, UserDetails> users = new HashMap<>();
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static {
        // Adicionando usuários em memória
        // As senhas são criptografadas usando BCrypt para maior segurança
        users.put("admin", new UserDetails("admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN", "Finance"));
        users.put("user", new UserDetails("user", passwordEncoder.encode("user123"), "ROLE_USER", "IT"));
    }

    // Método para buscar um usuário pelo nome de usuário
    public static UserDetails getUser(String username) {
        return users.get(username);
    }

    // Classe interna para representar os detalhes do usuário
    public static class UserDetails {
        private final String username;
        private final String password;
        private final String role;
        private final String department;

        public UserDetails(String username, String password, String role, String department) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.department = department;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }

        public String getDepartment() {
            return department;
        }
    }
}
