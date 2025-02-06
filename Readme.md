# Online Book Store API

## Overview

The Online Book Store API is a Restful service for managing books, shopping carts, and purchases. It allows users to browse books, add items to a cart, and proceed to checkout.

## Technologies Used

- Spring Boot - Backend framework for building REST APIs

- PostgreSQL - Relational database for persistent data storage

- Redis - In-memory data store used for caching and cart management

- Docker - Containerization for easy deployment and scalability

- Maven - Dependency management and build automation

- JWT (JSON Web Token) - Used for secure authentication and authorization

##  Why Redis for the Shopping Cart?

Instead of using a traditional database table and repository for cart management, Redis is utilized for the following reasons:

Performance Optimization: Redis is an in-memory store, making cart operations extremely fast.

Avoiding Redundant Data: Users may add items to the cart but never complete a purchase. Storing this data in the database would be unnecessary and inefficient.

Session-Based Storage: Since carts are temporary and user-driven, Redis is a better fit than persisting unconfirmed orders in a relational database.

### Getting Started

### Prerequisites

- Docker and Docker Compose installed on your machine.

### Running the Application

- Clone the repository:
```
git clone https://github.com/Michael4real-ux/Online-Book-Store-API.git
```

```
cd bookstore-api
```

- Ensure Docker is installed and running.

Start the application using Docker Compose:

```
docker compose up --build
```

To stop the application:

```
docker compose stop
```

### API Endpoints

Public Endpoints (No Authentication Required)

Books

- Retrieve all books: 
```
GET /api/v1/books
```

- Search for books
```
GET /api/v1/books/search
```

- Get cart contents: 
```
GET /api/v1/cart/{cartId}
```
Authenticated Endpoints (Requires Authentication)

Authentication

```
Register a user: POST /api/v1/auth/register
```
```
Login: POST /api/v1/auth/login
```

Books

- Create a new book 

```
POST /api/v1/books
```

Cart

- Add books to the cart

```
POST /api/v1/cart/{userId}/add
```

- Remove books from the cart

```
POST /api/v1/cart/{userId}/remove
```

Checkout

- Checkout process: 

```
POST /api/v1/checkout

```

- Get total amount to pay: 

```
GET /api/v1/checkout/total-amount
```

- Get purchase history

```
GET /api/v1/checkout/purchase/history
```

### NOTE
Authentication Details

For authenticated routes, You need to log in with the following seeded user credentials:

```
{
"username": "author1",
"password": "password"
}

```

- Response
```
  {
  "message": "Login successful",
  "username": "author1",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhdXRob3IxIiwiaWF0IjoxNzM4ODMzMzAwLCJleHAiOjE3Mzg4MzY5MDB9.Jpb-Y7ZBGCdZmrB_zX7-FdygrML_NzLvqZqtARf7grg"
  }
```
Authentication is handled using JWT tokens. Upon successful login, the API returns an access token. The access token must be included in the Authorization header for all protected endpoints:
Please ensure you copy it and paste in the Authorization header for testing

Authorization: Bearer <your-access-token>

### Api documentation
The app is documented with postman you can find the link below

```
https://documenter.getpostman.com/view/17452818/2sAYX8GfvK
```

### Note: The user data is seeded when you spin the app, userId is 1 by default.

### You can use the following credential on a gui
```
- Postgres
Host: localhost
Username=bookstore_user
port=5432
Password=bookstore_password
DB: bookstore_db
```
- Redis
Host: localhost
port=6379
DB: 0
password=redis_password

### Running Tests

Unit tests can be found in the src/test/java directory. To run tests:

```
mvn test
```

### High-Level Architecture

The Online Book Store API follows a layered architecture:

Components

- Models:

User: Represents authenticated users.

Book: Represents books in inventory.

Cart: Managed in Redis to optimize performance.

Purchase: Represents a completed order.

- Repositories:

UserRepository: Manages user authentication and data.

BookRepository: Handles book storage and retrieval.

PurchaseRepository: Stores completed purchases.

- Services:

AuthService: Handles user authentication and authorization.

BookService: Handles book-related operations.

CartService: Uses Redis for cart management.

CheckoutService: Manages the checkout process.

- Controllers:

AuthController: Manages authentication (register, login).

BookController: Manages book-related endpoints.

CartController: Handles cart-related operations.

CheckoutController: Processes checkouts.

### Architecture Flow

Authentication

```
+-------------------+            +---------------------+
|   HTTP Requests   |  <------>  |   AuthController   |
+-------------------+            +---------------------+
                                         |
                                         v
                                +---------------------+
                                |    AuthService     |
                                +---------------------+
                                         |
                                         v
                                +---------------------+
                                |   UserRepository   |
                                +---------------------+
                                         |
                                         v
                                  +-------------+
                                  | PostgreSQL  |
                                  +-------------+
```
Books

```
+-------------------+            +---------------------+
|   HTTP Requests   |  <------>  |   BookController   |
+-------------------+            +---------------------+
                                         |
                                         v
                                +---------------------+
                                |    BookService      |
                                +---------------------+
                                         |
                                         v
                                +---------------------+
                                |   BookRepository    |
                                +---------------------+
                                         |
                                         v
                                  +-------------+
                                  | PostgreSQL  |
                                  +-------------+
```

Cart

```
+-------------------+            +---------------------+
|   HTTP Requests   |  <------>  |   CartController   |
+-------------------+            +---------------------+
                                         |
                                         v
                                +---------------------+
                                |    CartService      |
                                +---------------------+
                                         |
                                         v
                                +---------------------+
                                |       Redis        |
                                +---------------------+
```

Checkout

```
+-------------------+            +---------------------+
|   HTTP Requests   |  <------>  | CheckoutController  |
+-------------------+            +---------------------+
                                         |
                                         v
                                +---------------------+
                                |  CheckoutService    |
                                +---------------------+
                                         |
                                         v
                                +---------------------+
                                | PurchaseRepository |
                                +---------------------+
                                         |
                                         v
                                  +-------------+
                                  | PostgreSQL  |
                                  +-------------+
```

Attached to the code is an High Level design in pdf

![High-Level-Design png](https://github.com/user-attachments/assets/bf093afb-9ecb-4fc2-ae77-93f813953bff)

### Conclusion

This API is designed for efficiency and scalability. The decision to use Redis for cart management helps improve performance and reduces unnecessary database writes. Authentication is managed using JWT to ensure secure access to protected resources. The application runs inside Docker containers, making it easy to deploy and manage in different environments. Future enhancements can include payment gateway integration and book recommendations.
