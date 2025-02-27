# O que é o Spring Security?
O Spring Security é um framework do Spring que fornece recursos avançados de autenticação e autorização para aplicações Java. Ele é amplamente utilizado para proteger APIs, aplicações web e gerenciar acessos de usuários.

## Principais Conceitos
1. Autenticação
    1. Processo de verificar a identidade de um usuário (ex.: login com nome de usuário e senha).
    2. O Spring Security gerencia isso criando um objeto de Authentication, que contém informações sobre o usuário autenticado.

2. Autorização
   1. Processo de verificar se o usuário autenticado tem permissão para acessar um recurso específico.
   2. Baseia-se em roles (ex.: ROLE_USER, ROLE_ADMIN) ou permissões específicas.

3. Filtros de Segurança
    1. O Spring Security intercepta todas as requisições HTTP usando uma cadeia de filtros (filter chain).
    2. Esses filtros verificam autenticação, autorização e outras regras de segurança.

4. Contexto de Segurança
    1. O Spring Security armazena informações do usuário autenticado no SecurityContext, que pode ser acessado em qualquer parte da aplicação.

## Configuração Básica
1. Classe de Configuração

Usamos uma classe anotada com @Configuration e @EnableWebSecurity para configurar as regras de segurança.
**Exemplo básico:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
http
.csrf().disable() // Desabilita CSRF (para APIs REST)
.authorizeHttpRequests()
.requestMatchers("/public").permitAll() // Permite acesso público
.anyRequest().authenticated() // Exige autenticação para outros endpoints
.and()
.httpBasic(); // Usa autenticação básica (pode ser substituído por JWT)
return http.build();
}
}
```
2. PasswordEncoder

Para armazenar senhas de forma segura, usamos o BCryptPasswordEncoder:
```java
@Bean
public PasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}
```
## Autenticação com JWT

1. JWT (JSON Web Token)
   1. Um padrão para autenticação sem estado. O token é gerado no login e enviado em cada requisição no cabeçalho HTTP (Authorization).
   2. O Spring Security pode ser configurado para validar JWTs e extrair informações como roles e claims.
2. Filtro de Autenticação JWT
   1. Criamos um filtro personalizado para interceptar requisições, validar o JWT e configurar o contexto de segurança.
3. Proteção de Endpoints
**Podemos proteger endpoints com base em roles ou permissões:**
```java
http
.authorizeHttpRequests()
.requestMatchers("/admin").hasRole("ADMIN") // Apenas usuários com ROLE_ADMIN
.requestMatchers("/user").authenticated(); // Qualquer usuário autenticado
```
## Objetos Importantes
1. Principal
   1. Representa o usuário autenticado. Usado para obter o nome do usuário no endpoint.
2. Authentication
   1. Contém informações completas sobre o usuário autenticado, como username, roles e detalhes adicionais.
3. SecurityContext
   1. Armazena o objeto Authentication e pode ser acessado globalmente.
## Resumo 
1. O Spring Security é um framework poderoso para gerenciar autenticação e autorização.
2. Ele intercepta requisições HTTP, verifica credenciais e aplica regras de acesso.
3. Pode ser configurado para usar diferentes métodos de autenticação, como login com senha, OAuth2 ou JWT.
4. É altamente personalizável e seguro, sendo amplamente utilizado em aplicações modernas.


---
## O que é o Principal?
No Spring Security, o objeto Principal representa o usuário autenticado que está fazendo a requisição. 
Ele contém informações básicas sobre o usuário, como o nome de usuário (username).

Quando um usuário é autenticado com sucesso, o Spring Security associa o objeto Principal à sessão ou ao contexto da requisição. 
Esse objeto pode ser usado para identificar o usuário que está acessando o sistema.

### Por que usamos o Principal no endpoint?
No método:

```java
@GetMapping("/user")
public String userEndpoint(Principal principal) {
    return "Bem-vindo ao endpoint USER! Usuário autenticado: " + principal.getName();
}
```
O parâmetro Principal principal é injetado automaticamente pelo Spring Security. Ele contém as informações do usuário autenticado. 

No exemplo acima, usamos o método principal.getName() para obter o nome do usuário.

### De onde vem o Principal?
O Principal é gerado pelo Spring Security durante o processo de autenticação. Quando um usuário faz login com sucesso, o Spring Security cria um objeto de autenticação (chamado de Authentication) que contém as informações do usuário, como:

1. Nome de usuário (username).
2. Roles (permissões).
3. Claims ou outros detalhes adicionais.

O Principal é uma abstração que encapsula essas informações básicas, como o nome do usuário.

### Exemplo Prático
Imagine que você tem um usuário chamado "joao" que fez login no sistema. Quando ele acessa o endpoint /user, o Spring Security injeta o Principal no método. O método principal.getName() retornará o nome do usuário autenticado, ou seja, "joao".

#### A resposta do endpoint seria algo como:

Bem-vindo ao endpoint USER! Usuário autenticado: joao

### E se eu quiser mais informações do usuário?

Se você precisar de mais informações além do nome do usuário (como roles ou claims), pode usar o objeto Authentication em vez do Principal. 
O Authentication é mais completo e contém todos os detalhes da autenticação.

**Por exemplo:**
```java
@GetMapping("/user")
public String userEndpoint(Authentication authentication) {
    String username = authentication.getName(); // Nome do usuário
    Collection<? extends GrantedAuthority> roles = authentication.getAuthorities(); // Roles do usuário
    return "Usuário autenticado: " + username + ", Roles: " + roles;
}
```
### Resumo
1. O Principal é uma interface que representa o usuário autenticado.
2. Ele é injetado automaticamente pelo Spring Security nos métodos dos endpoints.
3. O método principal.getName() retorna o nome do usuário autenticado.
4. Se precisar de mais informações, você pode usar o objeto Authentication.
---