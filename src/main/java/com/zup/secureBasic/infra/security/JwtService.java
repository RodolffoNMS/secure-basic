package com.zup.secureBasic.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Chave secreta usada para assinar os tokens JWT
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tempo de expiração do token (em milissegundos)
    private final long jwtExpirationMs = 1000 * 60 * 60; // 1 hora

    // Gera um token JWT com base no username, roles e claims adicionais.
    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims) // Adiciona as claims ao token
                .setSubject(username) // Define o "subject" (geralmente o username)
                .setIssuedAt(new Date()) // Define a data de emissão do token
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Define a data de expiração
                .signWith(secretKey) // Assina o token com a chave secreta
                .compact(); // Compacta o token em uma string no formato JWT
    }

    // Extrai o username (subject) de um token JWT.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Valida se o token JWT é válido para o usuário fornecido.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extrai o username do token
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); // Verifica se o token não expirou
    }

    // Verifica se o token JWT está expirado.
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Extrai a data de expiração de um token JWT.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Extrai uma claim específica de um token JWT.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Extrai todas as claims do token
        return claimsResolver.apply(claims); // Aplica a função para resolver a claim desejada
    }

    //Extrai todas as claims de um token JWT.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Define a chave secreta usada para assinar o token
                .build()
                .parseClaimsJws(token) // Analisa o token e extrai as claims
                .getBody();
    }
}
