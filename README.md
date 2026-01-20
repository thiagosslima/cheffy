# Cheffy - Sistema de Gestão para Restaurantes

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.0-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 Sobre o Projeto

Cheffy é um sistema backend robusto desenvolvido para gerenciar operações de múltiplos restaurantes através de uma plataforma compartilhada. O projeto foi criado como parte do Tech Challenge do curso de Pós-Graduação em Arquitetura e Desenvolvimento Java da FIAP.

### Problema

Um grupo de restaurantes na região identificou a necessidade de um sistema de gestão compartilhado devido aos altos custos de sistemas individuais. A solução permite que clientes escolham restaurantes com base na qualidade da comida, enquanto os estabelecimentos compartilham uma plataforma eficiente para gerenciar suas operações.

### Objetivo

Desenvolver um backend completo utilizando Spring Boot, implementando gestão de usuários, autenticação segura, e preparando a base para futuras funcionalidades como gestão de restaurantes, cardápios e pedidos.

## 🚀 Tecnologias Utilizadas

### Core
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Maven** - Gerenciamento de dependências

### Frameworks e Bibliotecas
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados
- **Hibernate** - ORM
- **PostgreSQL** - Banco de dados relacional
- **JWT (jjwt)** - Tokens de autenticação
- **MapStruct** - Mapeamento de objetos
- **Lombok** - Redução de boilerplate
- **SpringDoc OpenAPI** - Documentação Swagger

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração de containers

## 📁 Estrutura do Projeto

```
cheffy/
├── src/main/java/br/com/fiap/cheffy/
│   ├── config/              # Configurações (Security, Swagger, Exception Handler)
│   ├── controller/          # Controllers REST
│   ├── service/             # Lógica de negócio
│   │   └── security/        # Serviços de autenticação
│   ├── repository/          # Acesso a dados (JPA)
│   ├── model/
│   │   ├── entities/        # Entidades JPA
│   │   ├── dtos/            # Data Transfer Objects
│   │   ├── enums/           # Enumerações
│   │   └── security/        # Modelos de segurança
│   ├── mapper/              # Mapeadores MapStruct
│   ├── validation/          # Validações customizadas
│   ├── exceptions/          # Exceções customizadas
│   └── CheffyApplication.java
├── src/main/resources/
│   ├── application.properties
│   ├── data.sql
│   └── messages.properties
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── Cheffy_API_Collection.json
└── DOCUMENTACAO_TECH_CHALLENGE.md
```

## 🎯 Funcionalidades Implementadas

### ✅ Gestão de Usuários
- Cadastro de usuários (Cliente e Dono de Restaurante)
- Atualização de dados do usuário
- Atualização de senha (endpoint separado)
- Busca por ID, nome
- Exclusão de usuários
- Validação de email único
- Auditoria automática (data de criação e última atualização)

### ✅ Autenticação e Segurança
- Login com JWT (JSON Web Token)
- Autenticação stateless
- Criptografia de senhas com BCrypt
- Validação de senha forte (mínimo 12 caracteres)
- Proteção de endpoints com Spring Security

### ✅ Gestão de Endereços
- Múltiplos endereços por usuário
- Endereço principal
- Adicionar, atualizar e remover endereços
- Validação de pelo menos um endereço por usuário

### ✅ Perfis de Usuário
- Perfil Cliente (CLIENT)
- Perfil Dono de Restaurante (OWNER)
- Sistema extensível para novos perfis

### ✅ Qualidade e Padrões
- Arquitetura em camadas
- Princípios SOLID
- Tratamento de erros RFC 7807 (Problem Details)
- Versionamento de API (/api/v1/)
- Documentação Swagger/OpenAPI
- Logging estruturado

## 🔧 Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução
- [Git](https://git-scm.com/) instalado
- Portas 8080 (aplicação) e 5432 (PostgreSQL) disponíveis

## 🚀 Como Executar

### 1. Clonar o Repositório

```bash
git clone https://github.com/[USUARIO]/cheffy.git
cd cheffy
```

### 2. Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```bash
touch .env
```

Adicione o seguinte conteúdo:

```env
POSTGRES_PASSWORD=postgres123
DB_HOST=postgres
DB_PORT=5432
JWT_SECRET=chave_secreta_jwt_minimo_256_bits_para_seguranca_adequada
```

### 3. Iniciar a Aplicação

```bash
docker compose up --build
```

Este comando irá:
- Baixar a imagem do PostgreSQL 18.0
- Construir a imagem da aplicação
- Iniciar o banco de dados
- Iniciar a aplicação Spring Boot
- Popular os perfis iniciais (CLIENT e OWNER)

### 4. Acessar a Aplicação

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 5. Testar a API

#### Criar um usuário:
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "login": "joao.silva",
    "password": "SenhaForte@123456",
    "profileType": "CLIENT",
    "address": {
      "streetName": "Rua Teste",
      "number": 123,
      "city": "São Paulo",
      "postalCode": 12345678,
      "neighborhood": "Centro",
      "stateProvince": "SP",
      "main": true
    }
  }'
```

#### Fazer login:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "password": "SenhaForte@123456"
  }'
```

#### Buscar usuário (com token):
```bash
curl -X GET http://localhost:8080/api/v1/users/name/João%20Silva \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 6. Parar a Aplicação

```bash
docker compose down
```

Para remover também os volumes (dados do banco):
```bash
docker compose down -v
```

## 📚 Documentação

### Swagger UI
Acesse http://localhost:8080/swagger-ui.html para visualizar e testar todos os endpoints da API de forma interativa.

### Collection Postman
Importe o arquivo [Cheffy_API_Collection.json](Cheffy_API_Collection.json) no Postman para testar todos os cenários:
- Autenticação
- CRUD de usuários
- Gerenciamento de endereços
- Validações e tratamento de erros

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← REST Controllers
├─────────────────────────────────────┤
│          Service Layer              │  ← Business Logic
├─────────────────────────────────────┤
│        Repository Layer             │  ← Data Access (JPA)
├─────────────────────────────────────┤
│         Database Layer              │  ← PostgreSQL
└─────────────────────────────────────┘
```

### Princípios SOLID Aplicados
- **S**ingle Responsibility: Cada classe tem uma única responsabilidade
- **O**pen/Closed: Aberto para extensão, fechado para modificação
- **L**iskov Substitution: Subtipos podem substituir tipos base
- **I**nterface Segregation: Interfaces específicas e coesas
- **D**ependency Inversion: Dependência de abstrações

## 🔐 Segurança

- **Autenticação JWT**: Tokens stateless com expiração de 1 hora
- **BCrypt**: Criptografia de senhas com salt automático
- **Validação de Senha Forte**: Mínimo de 12 caracteres
- **HTTPS Ready**: Preparado para uso com certificados SSL/TLS
- **CORS**: Configurável para ambientes de produção
- **SQL Injection Protection**: Uso de JPA/Hibernate com prepared statements

## 📊 Banco de Dados

### Entidades Principais
- **User**: Usuários do sistema
- **Profile**: Perfis de acesso (CLIENT, OWNER)
- **Address**: Endereços dos usuários

### Relacionamentos
- User ↔ Profile (Many-to-Many)
- User → Address (One-to-Many)

## 🧪 Testes

### Collection Postman
A collection inclui testes para:
- ✅ Cadastro válido e inválido
- ✅ Login com credenciais válidas e inválidas
- ✅ Busca de usuários
- ✅ Atualização de dados
- ✅ Atualização de senha
- ✅ Gerenciamento de endereços
- ✅ Validações e tratamento de erros

## 📝 Endpoints Principais

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/v1/auth/login` | Autenticar usuário | Não |
| POST | `/api/v1/users` | Criar usuário | Não |
| GET | `/api/v1/users` | Listar usuários | Sim |
| GET | `/api/v1/users/{id}` | Buscar por ID | Sim |
| GET | `/api/v1/users/name/{name}` | Buscar por nome | Sim |
| PATCH | `/api/v1/users/{id}` | Atualizar dados | Sim |
| PATCH | `/api/v1/users/{id}/password` | Atualizar senha | Sim |
| DELETE | `/api/v1/users/{id}` | Deletar usuário | Sim |
| POST | `/api/v1/users/{userId}/addresses` | Adicionar endereço | Sim |
| GET | `/api/v1/profiles` | Listar perfis | Não |

## 🛠️ Desenvolvimento Local (Sem Docker)

### Pré-requisitos
- Java 21
- Maven 3.8+
- PostgreSQL 18.0

### Configuração
1. Instale e configure o PostgreSQL
2. Crie o banco de dados `cheffy`
3. Configure as variáveis de ambiente
4. Execute:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## 👥 Equipe

- Leandro Fita
- Igor Costa
- Rodrigo Ferreira
- Thiago Soares
- Victor Reis

## 📄 Licença

Este projeto foi desenvolvido como parte do Tech Challenge da FIAP e é disponibilizado para fins educacionais.

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões e feedback são bem-vindos!

## 📞 Contato

Para dúvidas ou sugestões, abra uma issue no repositório.

---

**Desenvolvido pela equipe Cheffy - FIAP 2026**
