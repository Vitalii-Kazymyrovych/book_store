# Bookstore Spring Boot Project

## Introduction
The **Bookstore** project is a Spring Boot application designed to manage books, categories, user orders, and shopping carts. Whether you're a book enthusiast, an administrator, or a developer, this project aims to simplify book-related tasks and enhance the user experience.

## Technologies Used
The following technologies and tools were employed in the development of the Bookstore project:

- **Spring Boot**: Provides a robust framework for building production-ready applications.
- **Spring Web**: Enables RESTful endpoints and handles HTTP requests.
- **Spring Data JPA**: Simplifies database interactions using JPA (Java Persistence API).
- **Spring Security**: Ensures secure authentication and authorization.
- **Swagger**: Generates API documentation and provides an interactive UI for testing endpoints.
- **MySQL**: The chosen relational database for storing book and user data.
- **Liquibase**: Manages database schema changes and versioning.
- **Hibernate**: An ORM (Object-Relational Mapping) framework for efficient database access.
- **Lombok**: Reduces boilerplate code by generating getters, setters, and constructors.
- **Mapstruct**: Simplifies mapping between DTOs (Data Transfer Objects) and entities.
- **JWT (JSON Web Tokens)**: Used for secure user authentication.

## Challenges Faced
- **Authentication and Authorization**: Implementing secure user authentication and role-based access.
- **Database Design**: Designing efficient database schemas for books, categories, and orders.
- **Entities relations**: Creating efficient and logically correct relations between different entities.
- **Custom repository methods**: Creating dao methods with custom queries to reduce amount of calls to database.
- **Adjusting Dockerfile and docker-compose**: Providing required data with correct structure to gain possibility of creating docker image of the project. 
- **Testing**: Ensuring comprehensive test coverage using Junit, Mockito, Test containers and other tools.
- **Error Handling**: Handling exceptions gracefully and providing meaningful error messages.

## Controller Functionalities

### Authentication (`/api/auth`)
- **POST /registration**: Allows users to register by providing necessary details.
- **POST /login**: Handles user login and returns a JWT (JSON Web Token) for subsequent requests.

### Books (`/api/books`)
- **GET**: Retrieves a list of all books.
- **GET /search**: Searches for books based on specified parameters.
- **GET /{id}**: Retrieves book details by its unique ID.
- **POST**: Creates a new book (admin-only).
- **POST /all**: Creates multiple books (admin-only).
- **PUT /{id}**: Updates book details by ID (admin-only).
- **DELETE /{id}**: Deletes a book by ID (admin-only).

### Categories (`/api/categories`)
- **POST**: Creates a new category (admin-only).
- **POST /all**: Creates multiple categories (admin-only).
- **GET**: Retrieves all available categories.
- **GET /{id}**: Retrieves category details by ID.
- **PUT /{id}**: Updates category details by ID (admin-only).
- **GET /{id}/books**: Retrieves all books within a specific category.
- **DELETE /{id}**: Deletes a category by ID (admin-only).

### Orders (`/api/orders`)
- **GET**: Retrieves all orders for a specific user.
- **POST**: Creates a new order.
- **GET /{orderId}/items**: Retrieves items within a specific order.
- **GET /{orderId}/items/{itemId}**: Retrieves details of a specific order item.
- **PUT /{orderId}**: Updates order status (admin-only).

### Roles (`/api/roles`)
- **GET**: Retrieves a list of all available roles.

### Cart (`/api/cart`)
- **GET**: Retrieves the user's shopping cart.
- **POST**: Adds a shopping cart item.
- **PUT /cart-items/{id}**: Updates a shopping cart item.
- **DELETE /cart-items/{id}**: Deletes a shopping cart item.

## Setup and Usage 
### Using code editor
1. Install Mysql server.
2. Clone the repository to your local machine.
3. Fill .env file in book_store directory.
4. Run `mvn clean package` command in terminal. If command doesn't run, install maven. Wait for project to build.
5. Run project.
6. Access the Swagger UI at `http://localhost:8080/api/swagger-ui.html` for API documentation and testing.

### Using docker-compose
1. Set up docker.
2. Pull mysql image from the docker hub.
3. Follow first 4 steps from previous guide.
4. Run `docker-compose build` command. Wait for image to build.
5. Run `docker-compose up` command.
6. Access the Swagger UI at `http://localhost:8088/api/swagger-ui.html` for API documentation and testing.

## Postman Collection
### https://elements.getpostman.com/redirect?entityId=29603624-3c0e94be-62a7-4e66-9c45-bebbe87f13e8&entityType=collection

* If you are using **IDE** for launch project, use **{{ide_link}}** variable as base URL (it is set by default). 
* If you choose **Docker**, use **{{docker_link}}** variable as base URL.

## Conclusion
The Bookstore project aims to simplify book-related operations while adhering to best practices in Spring Boot development. Feel free to explore, contribute, and enhance this project further!
