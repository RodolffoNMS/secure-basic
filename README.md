# Gerenciamento de JWT com Roles e Claims
## Descrição do Projeto
Este projeto é um sistema de autenticação e autorização baseado em JWT (JSON Web Token), desenvolvido com Spring Boot. Ele foi projetado para atender às necessidades de um grande banco, como o Itaú, permitindo que diferentes tipos de usuários (administradores e clientes) acessem recursos específicos com base em suas permissões (roles) e informações adicionais (claims).

## O sistema inclui:

1. Geração de JWT ao autenticar um usuário.
2. Inclusão de roles e claims no token.
3. Validação do token para garantir acesso seguro aos endpoints protegidos.
# Funcionalidades
## Geração de JWT:

1. Endpoint /login para autenticação de usuários.
2. Geração de um JWT contendo:
3. **Role:** Exemplo: ROLE_ADMIN ou ROLE_USER.
4. **Claim adicional:** Exemplo: department (como Finance ou IT).
## Validação de JWT:
1. Filtro de autenticação para validar o JWT em cada requisição.
2. Extração de roles e claims do token para autorização.
## Proteção de Endpoints:
1. Endpoint /admin: Acessível apenas para usuários com a role ROLE_ADMIN.
2. Endpoint /user: Acessível para qualquer usuário autenticado.
3. Exibição do valor do claim department no endpoint /user.
## Tecnologias Utilizadas
1. **Java 17**
2. **Spring Boot 3.x**
3. **Spring Security**
4. **jjwt (Java JWT Library)**
5. **Maven**
## Configuração do Projeto
### Dependências
Certifique-se de incluir as seguintes dependências no arquivo pom.xml:
```properties
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Spring Boot Starter Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- JWT Library -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```
## Como Executar
Clone o repositório:
```bash
git clone <URL_DO_REPOSITORIO>
cd <NOME_DO_PROJETO>
```
## Compile e execute o projeto:
```bash
mvn spring-boot:run
```
## Teste os endpoints:
Login: Envie uma requisição POST para /login com o corpo:
```json 
{
"username": "admin",
"password": "password"
}
```
- Admin: Envie uma requisição GET para /admin com o token no cabeçalho Authorization.
- User: Envie uma requisição GET para /user com o token no cabeçalho Authorization.
## Exemplos de Requisição
Login
```bash
curl -X POST http://localhost:8080/login \
-H "Content-Type: application/json" \
-d '{"username": "admin", "password": "password"}'
```
## Acesso ao Endpoint /admin
```bash
curl -X GET http://localhost:8080/admin \
-H "Authorization: Bearer <TOKEN>"
```
## Acesso ao Endpoint /user
```bash
curl -X GET http://localhost:8080/user \
-H "Authorization: Bearer <TOKEN>"
```