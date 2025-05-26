# LeonardoSR Food - Sistema de Microserviços

Sistema de microserviços para delivery de comida, construído com Spring Boot e arquitetura moderna de microserviços.

## 🏗️ Arquitetura

O projeto é composto pelos seguintes microserviços:

- **Gateway (Porto 8082)**: API Gateway central que roteia todas as requisições
- **Auth Service**: Serviço de autenticação integrado com Keycloak
- **Pedidos**: Gerenciamento de pedidos
- **Pagamentos**: Processamento e gestão de pagamentos
- **Discovery**: Service discovery com Eureka (registro dinâmico de serviços)

## 🚀 Tecnologias

- Java 17
- Spring Boot
- Spring Cloud
- Spring Security
- Keycloak
- Docker
- PostgreSQL
- Eureka Server
- Maven

## 📋 Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Maven
- PostgreSQL (ou Docker)
- Keycloak (ou Docker)

## 🛠️ Configuração e Instalação

1. Clone o repositório:
```bash
git clone https://github.com/le0nard0sr/leonardosr-food.git
cd leonardosr-food
```

2. Inicie os serviços com Docker Compose:
```bash
docker-compose up -d
```

3. O Keycloak estará disponível em:
   - URL: http://localhost:8080
   - Usuário padrão: admin
   - Senha padrão: admin

## 🔌 Portas e Endpoints

- **Gateway**: 8082
  - `/auth-service/**` - Serviço de Autenticação
  - `/pagamentos-ms/**` - Serviço de Pagamentos
  - `/pedidos/**` - Serviço de Pedidos

- **Keycloak**: 8080
- **Demais serviços**: Portas dinâmicas (registradas no Eureka)

## 🔒 Autenticação

O sistema utiliza Keycloak para autenticação e autorização:

1. Obter token:
```bash
POST http://localhost:8082/auth-service/auth/token
{
    "username": "admin",
    "password": "admin"
}
```

2. Usar o token nas requisições:
```bash
Authorization: Bearer {seu-token}
```

## 💳 Endpoints de Pagamentos

### Criar Pagamento
```bash
POST http://localhost:8082/pagamentos-ms/pagamentos
{
    "valor": 100.00,
    "pedidoId": 1,
    "status": "PENDENTE"
}
```

### Listar Pagamentos
```bash
GET http://localhost:8082/pagamentos-ms/pagamentos
```

### Atualizar Status
```bash
PATCH http://localhost:8082/pagamentos-ms/pagamentos/{id}/status
{
    "status": "CONFIRMADO"
}
```

## 🏃‍♂️ Execução Local

1. Inicie o Docker Compose:
```bash
docker-compose up -d
```

2. Execute os serviços na ordem:
   - Discovery
   - Gateway
   - Auth Service
   - Pagamentos
   - Pedidos

## 👥 Contribuição

1. Faça o fork do projeto
2. Crie sua feature branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adicionando nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes. 