# API Testing Results

All 6 API test cases have been tested and verified working.

## Test Results

### 1. POST /api/orders (Create Order) - 201 Created
![alt text](screenshots/image.png)

### 2. GET /api/orders (Get All Orders) - 200 OK
![alt text](screenshots/image-1.png)

### 3. GET /api/orders/{id} (Get Single Order) - 200 OK
![alt text](screenshots/image-2.png)

### 4. PUT /api/orders/{id} (Update Status - Valid) - 200 OK
![alt text](screenshots/image-3.png)

### 5. PUT /api/orders/{id} (Update Status - Invalid) - 400 Bad Request
![alt text](screenshots/image-4.png)

### 6. DELETE /api/orders/{id} (Cancel Order) - 200 OK
![alt text](screenshots/image-5.png)

All tests demonstrate:
- Correct HTTP status codes
- Proper JSON response formatting
- Order status validation
- Error handling
