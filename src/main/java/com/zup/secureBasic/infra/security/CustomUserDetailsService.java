package com.zup.secureBasic.infra.security;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    //implementa a interface UserDetailsService e use o InMemoryUserStore para carregar os detalhes do usuário.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no InMemoryUserStore
        InMemoryUserStore.UserDetails user = InMemoryUserStore.getUser(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        // Converte o usuário para um UserDetails do Spring Security
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", "")) // Remove o prefixo "ROLE_" para compatibilidade
                .build();
    }
}

