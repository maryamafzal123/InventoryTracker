# Inventory & Order Management System

A backend REST API for managing products, suppliers, customers, and orders, built with Java Spring Boot. Includes JWT-based authentication, role-based access control, and automatic stock management.

## Features

- **Product Management** — CRUD operations with stock tracking
- **Supplier & Customer Management** — CRUD with relational data
- **Order Processing** — Automatic stock deduction on order creation, with insufficient-stock validation
- **Search & Pagination** — Search products by name with paginated results
- **Low-Stock Alerts** — Retrieve products below a stock threshold
- **Revenue Reporting** — Calculate total revenue within a date range
- **JWT Authentication** — Secure register/login endpoints
- **Role-Based Access Control** — ADMIN, MANAGER, and STAFF roles with different permissions
- **Global Exception Handling** — Clean, structured error responses

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.5.4
- **Security:** Spring Security, JWT (JJWT)
- **Database:** PostgreSQL, Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Other:** Lombok, Bean Validation

## Getting Started

### Prerequisites
- Java 17+
- Maven
- PostgreSQL

### Setup

1. Clone the repository
   ```
   git clone https://github.com/maryamafzal123/InventoryTracker.git
   ```
2. Create a PostgreSQL database named `inventorytracker_db`
3. Copy `application.properties.example` to `application.properties` and update the database credentials
4. Run the application:
   ```
   mvn spring-boot:run
   ```
5. The API will be available at `http://localhost:8080`

## API Endpoints (Overview)

| Method | Endpoint | Description |
|--------|----------|--------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive JWT token |
| GET | `/api/products` | List all products |
| POST | `/api/products` | Create a product (ADMIN, MANAGER) |
| DELETE | `/api/products/{id}` | Delete a product (ADMIN only) |
| GET | `/api/products/search?name=` | Search products by name |
| GET | `/api/products/low-stock?threshold=` | Get low-stock products |
| POST | `/api/orders` | Create an order (stock auto-deducted) |
| GET | `/api/orders/revenue?startDate=&endDate=` | Get revenue for a date range |

## Entity Relationships

- Supplier → Product (One-to-Many)
- Customer → Order (One-to-Many)
- Order → OrderItem (One-to-Many)
- Product → OrderItem (One-to-Many)