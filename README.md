# 🛠️ Welcome to SilkRoad! 🛠️

Hello, E-commerce Enthusiasts! 👋

Welcome to **SilkRoad**, a secure web-based order management application designed for businesses to manage their products, orders, and users seamlessly. **SilkRoad** leverages modern technologies like **JakartaEE, Thymeleaf**, and **PostgreSQL** to provide an efficient and scalable platform for handling online orders and user management.

## 🚀 About SilkRoad

**SilkRoad** enables businesses to manage user roles (Admins and Clients), handle product catalogs, and track orders through an intuitive web interface. With **CRUD** functionality for users, products, and orders, **SilkRoad** is built with robust security and session management using **HttpSession**. The app employs **Thymeleaf** for dynamic and reusable layouts to enhance navigation and user experience.

## 📁 Project Structure

Here's an overview of the project structure for **SilkRoad**:

- **controllers**: Handles HTTP requests for managing users, products, and orders.
- **dto**: Data Transfer Objects facilitating data transfer between layers.
- **models**: Classes representing core entities: `User`, `Product`, `Order`, and associated enums.
- **repositories**: Interfaces extending `JpaRepository` for managing database queries.
- **services**: Business logic layer for managing users, products, and orders.
- **utils**: Utility classes for session handling, validation, and security.
- **templates**: Thymeleaf HTML templates for the user interface.
- **resources**: Contains configuration files such as `application.properties` and database setup scripts.

## 🧩 Key Features

- **User Management**: Authentication with session-based role management (Admin/Client).
- **Product Management**: Full CRUD functionality with pagination and search for Admins.
- **Order Management**:
    - Clients: Place, update, and cancel orders before they are shipped.
    - Admins: Manage all orders and update statuses.
- **Secure Session Handling**: Ensures authenticated access to admin and client functionalities.
- **Thymeleaf Layouts**: Reusable, dynamic layouts for consistent navigation.
- **Unit Tests**: Using **JUnit** and **Mockito** to test business and data access components.

## 🌐 Web Application Pages

### Authentication Page
- **Login/Register/Logout**: Handles authentication using session management.

### Product Management (Admin-only)
- **Product Listing**: Paginated list of products with search functionality.
- **CRUD Operations**: Admins can create, update, and delete products.

### Order Management
- **For Clients**:
    - Place new orders.
    - Modify or cancel orders in "Pending" or "Processing" statuses.
    - View order history and track current order status.
- **For Admins**:
    - View all orders and update statuses (e.g., "Shipped", "Delivered").

### User Management (SuperAdmin-only)
- **User Listing**: Admins can view and search users (Admins/Clients) with pagination.
- **CRUD Operations**: Admins can create, update, and delete users.

## 🎯 Project Objectives

- Develop a secure, session-based user authentication system.
- Use **Thymeleaf** for dynamic and reusable layouts.
- Implement CRUD operations for products, orders, and users.
- Use **JUnit** and **Mockito** for unit testing.
- Separate concerns with an MVC architecture to maintain clear presentation, business logic, and data persistence layers.

## 🛠️ How to Use SilkRoad

### Prerequisites

Before running **SilkRoad**, ensure you have the following installed:

- **Java 8** or later
- **Docker** to run the PostgreSQL database
- **Maven** for project build and dependency management

### Running the Application with Docker

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/zinebMachrouh/SilkRoad.git
   cd SilkRoad

2. Start the PostgreSQL database using Docker:
   ```bash
   docker-compose up -d
3. Update the `hibernate.cfg.xml` file in the `resources` directory with your database connection details.
4. Deploy the application on Apache Tomcat or run it locally via your browser at `http://localhost:8080/SilkRoad`.


## 🎉 Get Started with SilkRoad Today!

For any questions, feedback, or suggestions, feel free to reach out to us. 📧