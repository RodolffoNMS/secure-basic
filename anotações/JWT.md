# O que é um JWT?
### JWT (JSON Web Token) é um padrão para transmitir informações de forma segura entre duas partes como um objeto JSON. Ele é amplamente usado para autenticação e autorização em aplicações web.

### Um JWT é composto por três partes principais:
1. Header: Contém informações sobre o tipo de token e o algoritmo de assinatura.
2. Payload: Contém as informações (claims) que queremos transmitir, como o usuário autenticado, permissões, etc.
3. Signature: É a assinatura digital que garante que o token não foi alterado.

### Exemplo de um JWT:
```JSON
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.1KP0SsvENi7Uz1oQc07aXTL7kpQG5jBNIybqr60AlD4
```
 Cada parte é separada por um ponto (.) e é codificada em Base64.

---
## Gerar um JWT

```java
public static String generateToken(String subject) {
    return Jwts.builder()
        .setSubject(subject) // Define o "sub" (subject) do JWT
        .signWith(key)       // Assina o JWT com a chave secreta
        .compact();          // Compacta o JWT em uma String
}
```
- Jwts.builder(): Inicia a construção do JWT.
- setSubject(subject): Define o "subject" (sub), que é uma claim padrão do JWT. No nosso caso, usamos o nome do usuário, como "Joe".
- signWith(key): Assina o JWT com uma chave secreta. Isso garante que o token não pode ser alterado sem a chave correta.
- compact(): Finaliza o processo e retorna o JWT como uma string.
### Resultado: Um JWT assinado, que pode ser enviado para o cliente.

### Como adicionar mais campos ao Payload?
No método generateToken, você pode usar o método .claim() para adicionar informações personalizadas ao payload. Aqui está um exemplo atualizado:

```java
public static String generateToken(String subject, String role, String department) {
    return Jwts.builder()
            .setSubject(subject) // Claim padrão, geralmente é usado para identificar o usuário
            .claim("role", role) // Adiciona uma claim chamada "role"                    <- Claim personalizada
            .claim("department", department) // Adiciona uma claim chamada "department"  <- Claim personalizada
            .signWith(key) 
            .compact(); 
}
```

##  Validar um JWT
### No método validateToken, verificamos se o JWT é válido e se o "subject" é o esperado:

```java
public static boolean validateToken(String token, String expectedSubject) {
    try {
        String subject = Jwts.parserBuilder()
            .setSigningKey(key) // Define a chave para verificar a assinatura
            .build()
            .parseClaimsJws(token) // Analisa o JWT e extrai as claims
            .getBody()
            .getSubject(); // Obtém o "sub" (subject) do JWT

        return subject.equals(expectedSubject); // Verifica se o subject é o esperado
    } catch (Exception e) {
        return false; // Se houver qualquer problema, o token é inválido
    }
}
```
- Jwts.parserBuilder(): Inicia o processo de validação do JWT.
- setSigningKey(key): Define a chave secreta usada para verificar a assinatura do token.
- parseClaimsJws(token): Analisa o token e verifica se a assinatura é válida.
- getBody().getSubject(): Extrai o "subject" do token.
- subject.equals(expectedSubject): Compara o "subject" do token com o valor esperado.
### Se o token for inválido (por exemplo, assinatura incorreta ou token expirado), uma exceção será lançada, e retornamos false.
