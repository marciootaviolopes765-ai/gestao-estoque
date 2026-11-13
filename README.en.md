# Inventory Management - REST API

REST API for inventory and logistics management (simulated WMS/ERP), built with **Java 21 and Spring Boot**. It controls suppliers, products, goods entries and exits, minimum stock alerts, and turnover reports, featuring JWT authentication and access profiles.

## Features

- Registration of **suppliers** and **products** (full CRUD, with validations)
- Registration of stock **entries and exits**, with business rules:
    - exits cannot result in negative stock
    - entries cannot exceed maximum stock
    - movement history is never edited or deleted
- **Alerts** for low stock (at or below minimum) and high stock (90% of maximum or more), with replenishment suggestions
- **Reports** for stock position and **stock turnover** by period, implemented using VIEW, FUNCTION, and PROCEDURE in PostgreSQL
- Stock **reconciliation**: recalculates balances based on history
- **JWT authentication** with two profiles: `ADMIN` and `OPERATOR`
- **Interactive documentation** with Swagger UI

## Technologies

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 (Spring Web, Spring Data JPA, Validation) |
| Security | Spring Security, JWT (OAuth2 Resource Server), BCrypt |
| Database | PostgreSQL (with VIEW, FUNCTION, and PROCEDURE in SQL) |
| ORM | Hibernate / JPA |
| Documentation | springdoc-openapi (Swagger UI) |
| Build | Maven |

## Architecture

Project organized in layers:

```
controller  ->  service  ->  repository  ->  database
   (HTTP)      (rules)       (access)
```

| Package | Responsibility |
|---|---|
| `model` | JPA Entities (Fornecedor, Produto, MovimentacaoEstoque, Usuario) |
| `repository` | Database access (Spring Data JPA and JdbcTemplate) |
| `service` | Business rules |
| `controller` | REST endpoints |
| `dto` | API input and output objects (records) |
| `exception` | Exceptions and global error handling |
| `config` | Security and documentation |

## How to Run

### Prerequisites

- JDK 21
- PostgreSQL
- Maven (or use the included `mvnw`)

### Step by Step

1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR-USERNAME/gestao-estoque.git
   ```
2. Create the database `estoque_db` in PostgreSQL.
3. Set the environment variables:

   | Variable | Purpose |
      |---|---|
   | `DB_PASSWORD` | Password for the `postgres` user |
   | `JWT_SECRET` | JWT secret key (minimum 32 characters) |
   | `ADMIN_PASSWORD` | Initial administrator password |

   The last two have default values only for local study. **Do not use defaults in production.**
4. Run the application. Tables are created automatically.
5. Execute the script `src/main/resources/db/relatorios.sql` in the database (creates the view, function, and procedure for reports).

The API runs at `http://localhost:8080`.

## API Documentation

With the application running, access the Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

![Swagger UI](docs/img/img.png)

## Authentication

1. Log in at `POST /auth/login` with email and password.
2. Use the received token in the header `Authorization: Bearer <token>`.

An administrator user is created automatically on the first run (`admin@estoque.com`).

| Profile | Permissions |
|---|---|
| `ADMIN` | Everything: create, edit, and delete suppliers and products, create users, reconcile stock |
| `OPERATOR` | View everything and register stock entries and exits |

## Main Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/login` | Authentication (returns the JWT token) |
| POST | `/usuarios` | Creates a user (ADMIN) |
| GET, POST, PUT, DELETE | `/fornecedores` | Supplier CRUD |
| GET, POST, PUT, DELETE | `/produtos` | Product CRUD |
| POST | `/movimentacoes` | Registers an entry or exit |
| GET | `/movimentacoes` | History (optional filter `?produtoId=`) |
| GET | `/alertas/estoque-baixo` | Products needing replenishment |
| GET | `/alertas/estoque-alto` | Products near capacity |
| GET | `/alertas/resumo` | Product count by status |
| GET | `/relatorios/posicao-estoque` | Current position with stock value |
| GET | `/relatorios/giro-estoque?dias=30` | Stock turnover by period |
| POST | `/relatorios/conciliar-estoque` | Recalculates balances from history (ADMIN) |

## What I Learned from This Project

- Layered architecture with Spring Boot
- Relational modeling and mapping with JPA/Hibernate
- Business rules and transactions (`@Transactional`)
- VIEW, FUNCTION, and PROCEDURE in PostgreSQL
- Authentication and authorization with JWT
- API documentation with OpenAPI

## Author

Márcio Otávio - [GitHub](https://github.com/marciootaviolopes765-ai)