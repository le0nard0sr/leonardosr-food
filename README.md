# Microsserviços com Spring Boot e Keycloak

Este projeto é uma implementação de microsserviços usando Spring Boot, com autenticação e autorização gerenciadas pelo Keycloak.

## Pré-requisitos

- Docker
- Docker Compose
- JDK 21
- Maven

## Estrutura do Projeto

O projeto é composto pelos seguintes serviços:

- **Discovery Service (Eureka)**: Registro e descoberta de serviços
- **Gateway Service**: API Gateway usando Spring Cloud Gateway
- **Auth Service**: Serviço de autenticação integrado com Keycloak
- **Pagamentos Service**: Gerenciamento de pagamentos
- **Pedidos Service**: Gerenciamento de pedidos
- **Keycloak**: Servidor de autenticação e autorização
- **MySQL**: Banco de dados

## Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/leonardosr.git
   cd leonardosr
   ```

2. Inicie os serviços com Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Aguarde todos os serviços iniciarem (isso pode levar alguns minutos)

4. Execute o script de configuração do Keycloak:
   ```bash
   chmod +x keycloak-config.sh
   ./keycloak-config.sh
   ```

## Endpoints

### Keycloak
- **URL**: http://localhost:8080
- **Admin Console**: http://localhost:8080/admin
- **Credenciais Admin**:
  - Username: admin
  - Password: admin

### Eureka Server
- **URL**: http://localhost:8761

### API Gateway
- **URL**: http://localhost:8082

### Serviços
- **Auth Service**: http://localhost:8083
- **Pagamentos Service**: http://localhost:8084
- **Pedidos Service**: http://localhost:8085

## Autenticação

Para obter um token de acesso:

```bash
curl -X POST http://localhost:8080/realms/leonardosr/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=auth-service" \
  -d "client_secret=your-client-secret" \
  -d "grant_type=password" \
  -d "username=admin@leonardosr.com.br" \
  -d "password=admin123"
```

Use o token recebido no header Authorization das requisições:

```bash
curl -H "Authorization: Bearer {seu-token}" http://localhost:8082/api/...
```

## Usuários Padrão

### Admin
- **Email**: admin@leonardosr.com.br
- **Senha**: admin123
- **Role**: ADMIN

## Desenvolvimento

Para adicionar um novo serviço:

1. Crie um novo diretório para o serviço
2. Copie o template do Dockerfile
3. Adicione o serviço no docker-compose.yml
4. Configure a segurança usando o Keycloak
5. Registre o serviço no Eureka

## Troubleshooting

### Serviços não iniciam
1. Verifique os logs: `docker-compose logs -f [serviço]`
2. Verifique se o MySQL está acessível
3. Verifique se o Keycloak está configurado corretamente

### Problemas de autenticação
1. Verifique se o token está válido
2. Verifique as configurações do cliente no Keycloak
3. Verifique os logs do serviço de autenticação 