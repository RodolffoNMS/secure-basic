package com.zup.secureBasic.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {
    //Endpoint acessível apenas para ROLE_ADMIN
    @GetMapping("/admin")
    public String adminEndpoint(){
        return "Bem-vindo ao endpoint ADMIN! Apenas usuários com ROLE_ADMIN podem acessar.";
    }

    @GetMapping("/user")
    public String userEndpoint(Principal principal){
        // O parâmetro Principal principal é injetado automaticamente pelo Spring Security.
        // Ele contém as informações do usuário autenticado. Aqui usamos para obter o nome, mas podemos usar vários outros métodos
        return "Seja bem vindo, ao Endpoint USER! Usuário autenticado como: " + principal.getName();
    }
}
