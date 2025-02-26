package com.zup.secureBasic.infra.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable() // Desabilitei CSRF para simplificar/estudar (NÃO FAÇA ISSO EM PROD!!!!)
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll() // Permitir acesso ao endpoint de login sem autenticação
                .requestMatchers("/api/admin").hasRole("ADMIN") // Apenas usuários com a ROLE_ADMIN podem acessar
                .requestMatchers("/api/user").authenticated() //// Qualquer usuário autenticado pode acessar
                .anyRequest().authenticated()// Todas as outras requisições precisam de autenticação
                .and()
                .httpBasic();
    return http.build();
    }





    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){ //encripta password
        return new BCryptPasswordEncoder();
    }
}
