# 1. Chave Secreta
```java
private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
```
A chave secreta é usada para assinar e validar os tokens JWT.
Estamos usando o algoritmo HS256 (HMAC com SHA-256), que é seguro e amplamente utilizado.

# 2. Tempo de Expiração

```java
private final long jwtExpirationMs = 1000 * 60 * 60; // 1 hora
```
Define o tempo de expiração do token em milissegundos. Aqui, configuramos para 1 hora.
# 3. Geração do Token
```java
public String generateToken(String username, Map<String, Object> claims) {
return Jwts.builder()
.setClaims(claims)
.setSubject(username)
.setIssuedAt(new Date())
.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
.signWith(secretKey)
.compact();
}
```
- setClaims(claims): Adiciona informações adicionais ao token, como roles e department.
- setSubject(username): Define o "subject" do token, que geralmente é o username.
- setIssuedAt(new Date()): Define a data de emissão do token.
- setExpiration(...): Define a data de expiração do token.
- signWith(secretKey): Assina o token com a chave secreta.
- compact(): Gera o token no formato JWT.
# 4. Extração do Username
```java
public String extractUsername(String token) {
return extractClaim(token, Claims::getSubject);
}
```
Extrai o "subject" do token, que é o username.
# 5. Validação do Token
```java
public boolean isTokenValid(String token, UserDetails userDetails) {
final String username = extractUsername(token);
return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
}
```
Verifica se o username no token corresponde ao username do usuário autenticado.
Também verifica se o token não está expirado.
# 6. Verificação de Expiração
```java
private boolean isTokenExpired(String token) {
return extractExpiration(token).before(new Date());
}
```
Verifica se a data de expiração do token é anterior à data atual.
# 7. Extração de Claims
```java
public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
final Claims claims = extractAllClaims(token);
return claimsResolver.apply(claims);
}
```
Permite extrair qualquer claim do token usando uma função de resolução.
# 8. Extração de Todas as Claims
```java
private Claims extractAllClaims(String token) {
return Jwts.parserBuilder()
.setSigningKey(secretKey)
.build()
.parseClaimsJws(token)
.getBody();
}
```
- Analisa o token JWT e retorna todas as claims contidas nele.
- Exemplo de Uso
- Geração do Token

# Exemplo de Uso
## Geração do Token
```java
Map<String, Object> claims = new HashMap<>();
claims.put("role", "ROLE_ADMIN");
claims.put("department", "Finance");

String token = jwtService.generateToken("admin", claims);
System.out.println("Token gerado: " + token);
```
## Validação do Token
```java
boolean isValid = jwtService.isTokenValid(token, userDetails);
System.out.println("O token é válido? " + isValid);
```
## Extração de Claims
```java
String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
System.out.println("Role extraída: " + role);
```

