# Restaurant Management App

A full-stack restaurant management system built with **Spring Boot** (REST API) and **Angular**, secured with **Spring Security + JWT**.

## Features
- **JWT authentication** with 2 access levels:
  - `ADMIN`: manages categories, menu items, tables, users and can delete orders
  - `STAFF`: views the menu, creates orders, updates order and table status
- **4 modules with CRUD APIs**: Categories, Menu Items, Tables, Orders
- Order flow: `PENDING → PREPARING → SERVED → PAID` (or `CANCELLED`), and the table becomes occupied / available automatically
- Validation and a global exception handler that returns clear JSON errors
- Postman collection for all endpoints

## Tech Stack
| Part | Technology |
| --- | --- |
| Backend | Java 17, Spring Boot 3, Spring Web, Spring Data JPA (Hibernate), Spring Security, JWT (jjwt), Bean Validation, Lombok |
| Database | MySQL |
| Frontend | Angular 18 (standalone components), HttpClient, interceptor and route guards |
| Testing | Postman |

## Architecture
```
backend/src/main/java/com/mahmoud/restaurant
├── controller   REST endpoints
├── service      Business logic
├── repository   Spring Data JPA repositories
├── entity       JPA entities (User, Category, MenuItem, RestaurantTable, Order, OrderItem)
├── dto          Request / response records
├── security     JwtService, JwtAuthFilter, CustomUserDetailsService
├── config       SecurityConfig, DataSeeder
└── exception    Custom exceptions and GlobalExceptionHandler
```

## API Endpoints
| Method | Endpoint | Access |
| --- | --- | --- |
| POST | `/api/auth/login` | Public |
| GET | `/api/auth/me` | Logged in |
| GET / POST / DELETE | `/api/users` | Admin |
| GET | `/api/categories`, `/api/menu-items`, `/api/tables` | Logged in |
| POST / PUT / DELETE | `/api/categories`, `/api/menu-items`, `/api/tables` | Admin |
| PATCH | `/api/tables/{id}/status` | Logged in |
| GET / POST | `/api/orders` | Logged in |
| PATCH | `/api/orders/{id}/status` | Logged in |
| DELETE | `/api/orders/{id}` | Admin |

## How to Run

### Backend
1. Start MySQL (the database `restaurant_db` is created automatically).
2. Update the username / password in `backend/src/main/resources/application.properties` or set `DB_USERNAME` and `DB_PASSWORD`.
3. Run:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
4. A default admin is created on first run: `admin / admin123`.

### Frontend
```bash
cd frontend
npm install
npm start
```
Open `http://localhost:4200`.

### Postman
Import `postman/Restaurant-API.postman_collection.json`, run **Login** first and the token is saved automatically for the other requests.
