# Serviço de Autenticação

Este serviço é responsável pela autenticação e registro de usuários utilizando Keycloak como provedor de identidade.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.3
- Spring Security
- Spring Cloud Netflix Eureka Client
- Keycloak 21.1.1
- SpringDoc OpenAPI 2.3.0
- Lombok

## Pré-requisitos

- JDK 21
- Maven
- Keycloak Server
- Eureka Server

## Configuração

### Ambiente de Desenvolvimento

1. Clone o repositório
2. Configure o Keycloak:
   - Crie um realm chamado `myrealm`
   - Crie um client chamado `auth-service`
   - Configure os roles `ADMIN` e `USER`
   - Configure o client secret

3. Configure as variáveis de ambiente (opcional, valores padrão disponíveis):
   ```bash
   export KEYCLOAK_ADMIN_URL=http://localhost:8080
   export KEYCLOAK_ADMIN_REALM=myrealm
   export KEYCLOAK_ADMIN_CLIENT_ID=admin-cli
   export KEYCLOAK_ADMIN_USERNAME=admin
   export KEYCLOAK_ADMIN_PASSWORD=admin
   ```

### Ambiente de Produção

Configure as seguintes variáveis de ambiente:

```bash
export SPRING_PROFILES_ACTIVE=prod
export KEYCLOAK_URL=https://seu-keycloak.com
export KEYCLOAK_REALM=seu-realm
export KEYCLOAK_RESOURCE=seu-client
export KEYCLOAK_SECRET=seu-secret
export KEYCLOAK_ADMIN_URL=https://seu-keycloak.com
export KEYCLOAK_ADMIN_REALM=seu-realm
export KEYCLOAK_ADMIN_CLIENT_ID=seu-client-id
export KEYCLOAK_ADMIN_USERNAME=seu-usuario
export KEYCLOAK_ADMIN_PASSWORD=sua-senha
export EUREKA_URL=http://seu-eureka:8761
export CORS_ALLOWED_ORIGINS=https://sua-aplicacao.com
```

## Executando o Serviço

1. Compile o projeto:
   ```bash
   mvn clean install
   ```

2. Execute o serviço:
   ```bash
   mvn spring-boot:run
   ```

O serviço estará disponível em `http://localhost:8081`

## Documentação da API

A documentação da API está disponível através do Swagger UI:
- Desenvolvimento: http://localhost:8081/swagger-ui.html
- Produção: Desabilitado por segurança

## Endpoints

### Públicos
- POST `/auth/register` - Registro de novo usuário
- POST `/auth/login` - Login de usuário

### Protegidos
- GET `/auth/user/**` - Endpoints que requerem role USER
- GET `/auth/admin/**` - Endpoints que requerem role ADMIN

## Segurança

- Autenticação via JWT
- Autorização baseada em roles (ADMIN e USER)
- CORS configurável
- Endpoints protegidos
- Validação de tokens JWT
- Logging de eventos de segurança

## Monitoramento

- Logs detalhados no console
- Integração com Eureka para service discovery
- Tratamento global de exceções

## Suporte

Para suporte, entre em contato com leonardosr@email.com

## Configuração do Ambiente

### Keycloak

O serviço requer uma instância do Keycloak configurada com os seguintes usuários:

1. Usuário Administrador (Desenvolvimento/Testes):
   - Username: `leonardosr`
   - Senha: [definida durante a configuração]
   - Permissões: Admin

2. Usuários de Teste (Opcional):
   - Username: `test@example.com`
   - Senha: `password123`
   - Permissões: User

### Executando os Testes

Os testes são divididos em duas categorias:

1. Testes Unitários e de Integração Mock:
   - Não requerem conexão real com o Keycloak
   - Executar: `mvn test`

2. Testes de Integração com Keycloak:
   - Requerem conexão real com o Keycloak
   - Requerem usuários configurados
   - Executar: `mvn test -Dgroups=integration`

### Configuração de Novos Usuários

Para adicionar novos usuários no ambiente de desenvolvimento:

1. Acesse a interface administrativa do Keycloak
2. Navegue até o realm correto
3. Adicione um novo usuário com as credenciais necessárias
4. Atribua as roles apropriadas

**Importante:** Nunca commite senhas ou tokens no repositório. 