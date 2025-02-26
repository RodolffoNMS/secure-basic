package com.zup.secureBasic.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;


public class JwtUtil {

    // Gerar uma chave secreta para assinar o JWT
    private static final SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    // Método para gerar um JWT
    public static String generateToken(String subject, String role, String departament) {
        return Jwts.builder()
                .setSubject(subject) // Define o "sub" (subject) do JWT
                .claim("role", role) // Adiciona uma claim chamada "role" com o valor passado (ex.: ROLE_ADMIN ou ROLE_USER).
                .claim("departament", departament) // Adiciona uma claim chamada "departament" com o valor passado (ex.: Peão ou Oreia Seca).
                .signWith(key)       // Assina o JWT com a chave secreta
                .compact();          // Compacta o token em uma String no formato JWT (Header.Payload.Signature).
    }

    // Metodo para verificar e validar um JWT
    public static boolean validateToken(String token, String expectedSubject, String expectedRole, String expectedDepartment) {
        try {
            // Analisa o token JWT e extrai as claims
              var claims = Jwts.parserBuilder()
                    .setSigningKey(key) // Define a chave secreta usada para verificar a assinatura do token
                    .build()
                    .parseClaimsJws(token) // Analisa o JWT e extrai as claims
                    .getBody();

              String subject = claims.getSubject();
              String role = claims.get("role", String.class);
              String departament = claims.get("departament", String.class);

            return subject.equals(expectedSubject)
                    && role.equals(expectedRole) // Comparar com o valor extraído do token
                    && departament.equals(expectedDepartment); // Comparar com o valor extraído do token
        } catch (Exception e) {
            // Se houver qualquer problema (ex.: assinatura inválida), retorna falso
            return false;
        }
    }
}
