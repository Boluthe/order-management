# Mini Order Management System

A REST API for managing customer orders, built with Java 21, Spring Boot, Spring Data JPA, and an H2 in-memory database.

## Tech Stack
- **Java 21**, Spring Boot 4.1.0
- **Spring Data JPA**, Hibernate
- **H2 Database** (in-memory)
- **JUnit 5**, **Mockito**, **MockMvc** (tests)
- **Lombok**, **Spring Boot Validation** (JSR-303)

## Project Structure

```text
order-management/
├── src/
│   ├── main/java/com/example/ordermanagement/
│   │   ├── controller/         # REST API Endpoints
│   │   ├── dto/                # Data Transfer Objects (Requests/Responses)
│   │   ├── entity/             # Database Entities and Enums
│   │   ├── exception/          # Global Error Handling & Custom Exceptions
│   │   ├── repository/         # Spring Data JPA Repositories
│   │   ├── service/            # Core Business Logic
│   │   └── OrderManagementApplication.java
│   └── test/java/com/example/ordermanagement/
│       ├── controller/         # Integration tests with MockMvc
│       └── service/            # Unit tests with Mockito
├── pom.xml                     # Maven configuration
└── README.md
```

## API Endpoints

### 1. Create an Order
`POST /api/orders`
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "vendorName": "TechStore",
    "productName": "Laptop",
    "quantity": 1,
    "unitPrice": 1200.00,
    "deliveryAddress": "123 Main St, City"
  }'
```
**Response**: `201 Created`

### 2. Retrieve All Orders
`GET /api/orders`
```bash
curl -X GET http://localhost:8080/api/orders
```
**Response**: `200 OK` (JSON array)

### 3. Retrieve a Single Order by ID
`GET /api/orders/{id}`
```bash
curl -X GET http://localhost:8080/api/orders/1
```
**Response**: `200 OK` or `404 Not Found`

### 4. Update Order Status
`PUT /api/orders/{id}`
Valid transitions follow a strict linear progression. See *Order Status Flow* below.
```bash
curl -X PUT http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{
    "orderStatus": "CONFIRMED"
  }'
```
**Response**: `200 OK` or `400 Bad Request` (Invalid transition or validation error)

### 5. Cancel an Order
`DELETE /api/orders/{id}`
```bash
curl -X DELETE http://localhost:8080/api/orders/1
```
**Response**: `200 OK` or `400 Bad Request`

## Order Status Flow

Status transitions are strictly enforced to prevent illegal jumps in the fulfillment process:
`PENDING` → `CONFIRMED` → `READY_FOR_PICKUP` → `DELIVERED`

*(Note: `CANCELLED` is allowed from any active state prior to delivery. Once an order is `DELIVERED` or `CANCELLED`, it cannot be changed).*

## Database Schema

The system uses a single `orders` table.

| Column | Type | Description |
|---|---|---|
| `id` | BIGINT | Primary key, auto-generated |
| `customer_name` | VARCHAR | Name of the customer |
| `vendor_name` | VARCHAR | Name of the vendor |
| `product_name` | VARCHAR | Name of the product |
| `quantity` | INT | Number of items |
| `unit_price` | DOUBLE | Price per item |
| `total_amount` | DOUBLE | Auto-calculated: quantity * unit_price |
| `delivery_address` | VARCHAR | Delivery destination |
| `order_status` | VARCHAR | Status enum (e.g., PENDING) |
| `created_at` | TIMESTAMP | Creation timestamp |
| `updated_at` | TIMESTAMP | Last update timestamp |

## How to Run

1. Navigate to the root directory.
2. Run the application using the Maven wrapper:
```bash
./mvnw spring-boot:run
```
*(On Windows, use `mvnw.cmd spring-boot:run`)*

3. The API will be available at `http://localhost:8080`.

## How to Test

Run the test suite using Maven:
```bash
./mvnw test
```
*(On Windows, use `mvnw.cmd test`)*

### Manual API Testing
For Postman testing results and endpoint verification, see [TESTING.md](./TESTING.md).

### Test Coverage
* **`OrderServiceTest` (Unit Tests)**: Validates pure business logic using Mockito.
    * Tests successful order creation and total calculation.
    * Enforces valid status progressions.
    * Throws `InvalidStatusException` on illegal state jumps.
* **`OrderControllerIntegrationTest` (Integration Tests)**: Tests the full API slice with an embedded database, `@SpringBootTest`, and `MockMvc`.
    * Covers API endpoints natively.
    * Validates HTTP response codes (`201 Created`, `200 OK`, `404 Not Found`).

## Assumptions
- No authentication required.
- **In-Memory Database**: An H2 in-memory database is used. All data resets when the application stops.
- Order addresses are treated as raw strings with no external geocoding validation.
- Payment processing is not included in the scope of this API.
