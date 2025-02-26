package com.zup.secureBasic.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // Habilita o uso de @PreAuthorize para proteger endpoints com roles
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Filtro para validar o JWT em cada requisição

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desabilitei CSRF para simplificar/estudar (NÃO FAÇA ISSO EM PROD!!!!)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Define que o Spring Security não deve criar sessões
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll() // Permitir acesso ao endpoint de login sem autenticação
                .requestMatchers("/api/admin").hasRole("ADMIN") // Apenas usuários com a ROLE_ADMIN podem acessar
                .requestMatchers("/api/user").authenticated() // Qualquer usuário autenticado pode acessar
                .anyRequest().denyAll() // Bloqueia qualquer outra requisição
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT antes do filtro padrão de autenticação

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Gerencia a autenticação com base nas configurações do Spring Security
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define o algoritmo de hash para senhas (BCrypt)
    }
}
