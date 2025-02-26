package com.zup.secureBasic;

import com.zup.secureBasic.infra.security.JwtUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureBasicApplication {

    //	public static void main(String[] args) {
//		SpringApplication.run(SecureBasicApplication.class, args);
//	}
    public static void main(String[] args) {
        // Gerar um JWT
        String token = JwtUtil.generateToken("Joe", "ROLE_ADMIN", "Finance");
        System.out.println("Token gerado: " + token);

        // Validar o JWT com os valores corretos
        boolean isValid = JwtUtil.validateToken(token, "Joe", "ROLE_ADMIN", "Finance");
        System.out.println("O token é válido? " + isValid);

        // Testar com valores incorretos
        boolean isInvalid = JwtUtil.validateToken(token, "Joe", "ROLE_USER", "IT");
        System.out.println("O token é válido para ROLE_USER e IT? " + isInvalid);
    }

}
