# Online Bookstore Application

## Project Overview
This project aims to deliver a fully functional online bookstore application. The app allows users to browse books, manage shopping carts, and place orders, while administrators can manage books, categories, and order statuses. The application follows a domain-driven design and is built using **Java** and **Spring Boot**, integrating several modern technologies.

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

## Architecture Overview
The architecture of this application is based on a layered approach:
- **Presentation Layer**: Handles user requests (via REST APIs) and returns responses.
- **Service Layer**: Contains the business logic and communicates between the presentation and data layers.
- **Data Access Layer**: Interacts with the database, using **Hibernate** as the ORM framework.
- **Security**: Implemented using **Spring Security** with **JWT** for authentication.

## Technologies Used
- **Java 17+**
- **Spring Boot** (REST APIs, Service Layer, Security)
- **Hibernate** (ORM framework)
- **MySQL** (Database)
- **MapStruct** (DTO mapping)
- **Docker** (Containerization for deployment)
- **JWT** (JSON Web Token for authentication)

## How to Run the Project

1. Clone this repository.
```bash
git clone https://github.com/citruscaviar/online-book-store
```
2. Set up the environment variables
Create a **.env** file in the root directory of the project.
Add the following environment variables inside the .env file:

```env
- DATABASE_URL=jdbc:mysql://localhost:3306/online_bookstore
- DATABASE_USERNAME=myusername
- DATABASE_PASSWORD=mypassword
- JWT_SECRET=my_jwt_secret_key
```
3. Run the application using Docker. Ensure that Docker is installed and running on your machine.
Then, in the root directory of the project, run the following command:

```bash
docker-compose up
```
This will start the application along with the MySQL database in Docker containers.
The application will be accessible at http://localhost:8080.
4. Swagger:Once the application is running,
you can view the API documentation via Swagger at http://localhost:8080/swagger-ui.html


