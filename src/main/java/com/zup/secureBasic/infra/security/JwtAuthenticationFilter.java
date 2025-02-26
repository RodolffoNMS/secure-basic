package com.zup.secureBasic.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;// Serviço para manipular tokens JWT
    private final UserDetailsService userDetailsService;// Serviço para carregar os detalhes do usuário

    // Construtor para injetar dependências
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Ignorar o endpoint /login
        String path = request.getRequestURI();
        if ("/login".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Extrair o token JWT do cabeçalho da requisição
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7); // Remove o prefixo "Bearer " para obter o token
                username = jwtService.extractUsername(jwt); // Extrai o username do token
            }

            // 2. Verificar se o usuário está autenticado no contexto do Spring Security
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 3. Carregar os detalhes do usuário a partir do banco de dados ou outra fonte
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 4. Validar o token JWT
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // 5. Criar um objeto de autenticação para o usuário
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // 6. Adicionar detalhes da requisição ao token de autenticação
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 7. Configurar o contexto de segurança do Spring com o token de autenticação
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        // 8. Continuar com o próximo filtro na cadeia
        filterChain.doFilter(request, response);
    }
}
