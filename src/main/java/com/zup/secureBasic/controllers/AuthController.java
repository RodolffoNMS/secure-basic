package com.zup.secureBasic.controllers;

import com.zup.secureBasic.infra.security.InMemoryUserStore;
import com.zup.secureBasic.infra.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        // Busca o usuário na "base de dados" em memória
        InMemoryUserStore.UserDetails user = InMemoryUserStore.getUser(username);

        // Verifica se o usuário existe e se a senha está correta
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas!");
        }

        // Gera o token JWT com as roles e claims do usuário
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole(), user.getDepartment());

        // Retorna o token em um mapa (JSON)
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
