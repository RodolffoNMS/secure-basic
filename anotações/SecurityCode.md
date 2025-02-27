### @EnableGlobalMethodSecurity(prePostEnabled = true):
Habilita o uso de anotações como @PreAuthorize para proteger endpoints com base em roles.
### csrf().disable():
Desabilita a proteção contra CSRF porque estamos usando JWT, que já protege contra ataques de falsificação de requisições.
### sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS):
Define que o Spring Security não deve criar sessões, pois o JWT será usado para autenticação.
### authorizeRequests():
### Configura as permissões de acesso para os endpoints:
1. /login: Acesso público.
2. /admin: Apenas usuários com a role ROLE_ADMIN.
3. /user: Qualquer usuário autenticado.
### addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class):
Adiciona o filtro de autenticação JWT antes do filtro padrão de autenticação do Spring Security.
### PasswordEncoder:
Define o algoritmo de hash para senhas. O BCrypt é amplamente utilizado por ser seguro e fácil de usar.