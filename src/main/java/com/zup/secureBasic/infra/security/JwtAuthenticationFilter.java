package com.zup.secureBasic.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extrair o token do cabeçalho Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7); // Remover "Bearer " do início
        try {
            // Validar o token e extrair as claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(JwtUtil.getKey()) // Usar a chave secreta para validar o token
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Configurar o contexto de segurança com as informações do token
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            // Aqui você pode criar um objeto de autenticação e configurá-lo no contexto de segurança
            // Exemplo: UsernamePasswordAuthenticationToken authentication = ...

            // SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            // Token inválido
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
