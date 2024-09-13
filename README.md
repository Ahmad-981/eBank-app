# Banking Application

Welcome to the **Banking Application**, a full-stack project designed to manage user accounts, transactions, and balances. This application is built using **Java Spring Boot** for the backend and **React** for the frontend, with a focus on security, scalability, and ease of use.

## **Table of Contents**

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [Contributing](#contributing)
- [License](#license)

---

## **Overview**

This banking application allows users to create and manage bank accounts, perform transactions (deposits, withdrawals, and transfers), and view account balances. The application is designed with security in mind, implementing **JWT authentication**, **role-based access control**, and **input validation** to ensure safe and secure transactions.

The application follows **RESTful API** best practices and is built with scalability in mind, using **HATEOAS** for navigation between related resources and **pagination** for large datasets.

## **Features**

- **User Registration & Authentication**: Users can register, log in, and manage their sessions using JWT-based authentication.
- **Account Management**: Users can create accounts, view account details, and manage account types.
- **Transactions**: Support for deposits, withdrawals, and transfers between accounts.
- **View Transaction History**: Users can view their past transactions with filtering and pagination options.
- **Admin Panel**: Admins can manage user accounts, view all transactions, and access detailed reports.
- **Error Handling**: Proper error handling and validation for all API requests.
- **Security**: Role-based access control (Admin, User) with JWT authentication.

## **Technologies Used**

### **Backend:**
- **Java Spring Boot**: For RESTful API development and server-side logic.
- **Spring Security**: For authentication and authorization using JWT tokens.
- **Hibernate/JPA**: For database interactions and ORM.
- **H2 Database / MySQL**: H2 for development/testing, MySQL for production.
- **Liquibase**: For database schema version control.

### **Frontend:**
- **React.js**: For building the user interface.
- **Chakra UI**: For consistent and modern UI components.
- **Axios**: For making HTTP requests to the backend.
- **SweetAlert2**: For alerting users with styled pop-ups.

### **Other Tools:**
- **Swagger (OpenAPI)**: For API documentation and testing.
- **JUnit/Mockito**: For unit and integration testing.
