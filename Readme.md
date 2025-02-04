# Online Book Store API

## Overview
This is a Restful API for managing an online bookstore. It allows users to manage books, a shopping cart, and purchase history.

## Technologies Used
- Spring Boot
- H2 Database (in-memory)
- Maven

## Getting Started

### Prerequisites
- Java 17 or later
- Maven

### Running the Application
1. Clone the repository:
   ```bash
   git clone <will-update-this-to-repository-url>
   cd bookstore-api
   
2. Before running the application ensure you set the h2 memory database


   Option 1: Start H2 Using Maven (Easiest), If Maven is installed, run:
```
     mvn exec:java -Dexec.mainClass="org.h2.tools.Server" -Dexec.args="-tcp -tcpAllowOthers -tcpPort 9092 -ifNotExists"
```
   Option 2: Start H2 Manually from JAR, If  downloaded the H2 JAR, run:

```
     java -cp h2*.jar org.h2.tools.Server -tcp -tcpAllowOthers -tcpPort 9092 -ifNotExists

```

Note: Please make sure the H2 database is running continuously during testing the application

3. Run/Start the application:
  ```bash
    mvn spring-boot:run
 ```

API Endpoints

Books

- Retrieve all books
```
 GET /api/v1/books 
```
- Create a new book
```
POST /api/v1/books
```
- Search for books
```
GET /api/v1/books/search 
```

Cart

- Create a new cart
```
POST /api/v1/cart
```
- Add a book to cart
```
POST /api/v1/cart/{cartId}/add 
```
- Get cart contents
```
GET /api/v1/cart/{cartId}
```

Checkout

- Simulate the checkout process
```
POST /api/v1//checkout
```

Testing
Unit tests can be found in the src/test/java directory. You can run them using:

````
mvn test
````

## High-Level Design for the Online Book Store API

Overview:
The high-level design of this Online Book Store API is structured to separate concerns among various components, which enhances scalability and maintainability. The design follows a layered architecture, consisting of the Model, Repository, Service, and Controller layers. Below is a description of each layer along with a diagram to illustrate the relationships between components.

Components Description
Models:

Book: Represents a book in the inventory with attributes such as title, genre, ISBN, author, and year of publication.
Cart: Represents a shopping cart that can hold multiple books.
Purchase: Represents a purchase made by a user, linking to the cart and including the purchase date.
Repositories:

BookRepository: Handles CRUD operations for books in the database.
CartRepository: Manages the storage and retrieval of shopping carts.
PurchaseRepository: Handles operations related to purchase records.
Services:

BookService: Contains business logic for managing books, including searching and saving books.
CartService: Manages the shopping cart functionalities, such as adding books and retrieving cart contents.
CheckoutService: Handles the checkout process, simulating a purchase transaction.
Controllers:

BookController: Exposes RESTful endpoints for interacting with books.
CartController: Provides endpoints for managing shopping carts.
CheckoutController: Offers endpoints for processing checkouts.

High-Level Architecture Diagram

```
+-------------------+            +---------------------+
|   HTTP Requests    |  <------> |   BookController    |
+-------------------+            +---------------------+
                                         |
                                         | 
                                         v
                                +---------------------+
                                |    BookService      |
                                +---------------------+
                                         |
                                         |
                                         v
                                +---------------------+
                                |   BookRepository    |
                                +---------------------+
                                         |
                                         |
                                         v
                                  +-------------+
                                  |   Database  |
                                  +-------------+

+-------------------+            +---------------------+
|   HTTP Requests    |  <------> |   CartController    |
+-------------------+            +---------------------+
                                         |
                                         |
                                         v
                                +---------------------+
                                |    CartService      |
                                +---------------------+
                                         |
                                         |
                                         v
                                +---------------------+
                                |   CartRepository    |
                                +---------------------+

+-------------------+            +---------------------+
|   HTTP Requests    |  <------> |  CheckoutController  |
+-------------------+            +---------------------+
                                         |
                                         |
                                         v
                                +---------------------+
                                |  CheckoutService    |
                                +---------------------+
                                         |
                                         |
                                         v
                                +---------------------+
                                |   PurchaseRepository |
                                +---------------------+
```

Explanation of the Diagram

1. HTTP Requests: The clients (e.g., web or mobile apps) send HTTP requests to the RESTful API endpoints.

2. Controllers: Each controller (BookController, CartController, CheckoutController) receives requests and routes them to the corresponding service layer.

3. Services: Services contain the business logic and interact with the repository layer to perform operations on the data.

4. Repositories: Repositories handle data access, providing methods for CRUD operations on the respective data models.

5. Database: The application uses an H2 in-memory database (or any other database) to store data persistently.

Conclusion
This high-level design provides a clear understanding of how the Online Book Store API is structured. The separation of concerns among models, repositories, services, and controllers allows for easier maintenance and scalability. This architecture can be further extended to integrate additional features, such as authentication, payment processing, and caching, as needed.

Note: A png file for the High-Level Architecture Diagram is attached in the repository.