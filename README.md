# Online Bookstore Application

## Project Overview

This project aims to deliver a fully-functional online bookstore application.
The app allows users to browse, manage shopping carts, and place orders,
while admins can manage books, categories, and order statuses.
The application is developed using Java and Spring Boot and follows a domain-driven design.
Below are the core domain models used in the app.

## Domain Models

The following domain models are central to the app:

- **User**: Stores data about registered users, including authentication details and personal information.
- **Role**: Represents the user's role in the system (e.g., admin, user).
- **Book**: Represents a book available for purchase in the store.
- **Category**: Represents a category that books can belong to.
- **ShoppingCart**: Represents a user's shopping cart.
- **CartItem**: Represents an item in the user's shopping cart.
- **Order**: Represents an order placed by a user.
- **OrderItem**: Represents an item within a user’s order.

## User Roles and Features

### Shopper (User)

Shoppers have the following capabilities:

- Sign up and sign in to the store.
- Browse books across all categories or within a selected category.
- View detailed information about each book.
- Add books to or remove books from the shopping cart.
- View the shopping cart and place an order.
- View past orders and check details of each order.

### Manager (Admin)

Admins can:

- Add, remove, or update books in the store.
- Create and delete book categories.
- Update book and category details.
- Update the order status (e.g., mark as "shipped" or "delivered").

## Technologies Used

- Java 17+
- Spring Boot
- Hibernate
- MySQL (or other relational databases)
- MapStruct (for DTO mapping)

## How to Run the Project

1. Clone this repository.
2. Set up your local environment (e.g., MySQL database, Java 17+).
3. Configure the application.yml file with your database connection details.
4. Run the project using your preferred method (e.g., mvn spring-boot:run).
