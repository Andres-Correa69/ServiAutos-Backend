# 📘 Project Documentation – ServiAutos Backend

## 🔹 Overview

This project is a **Java Spring Boot** backend application built with:

- **Spring Boot** → Core framework to build RESTful APIs.  
- **Gradle Kotlin DSL** → Build and dependency management tool.  
- **MongoDB** → NoSQL database for flexible and scalable data storage.  
- **Lombok** → Library to reduce boilerplate code (getters, setters, constructors, etc.).  
- **Layered Architecture** → Separation of concerns into distinct layers (Controller, Service, Repository, Domain, DTO).  

The goal of this architecture is to ensure **clean code organization, testability, and maintainability**.

---

## 🔹 Project Architecture

The project follows a **layered architecture**, typically structured as:

```
    Controller --> Service --> Repository --> Database[(MongoDB)]
    DTOs[DTOs & Domain Models] --> Service
```  

---

## 📂 Package Structure

### 1. `controller`
- Contains **REST Controllers** that expose endpoints to clients (frontend, mobile apps, etc.).
- Responsible for handling **HTTP requests/responses**.
- Uses **DTOs** to communicate with the service layer.


---

### 2. `domain.models`
- Contains **core business entities (models)** that map to **MongoDB collections**.
- Annotated with **Spring Data** and **Lombok** annotations.
- Represents how data is structured and stored.


---

### 3. `dto` (Data Transfer Objects)
- Defines **DTO classes** used to transfer data between the client, controller, and service layer.
- Provides **validation** (via `jakarta.validation` annotations).
- Shields **internal domain models** from direct exposure.

- `ResponseDTO` → Standardized response object to return messages, status, and ayloads.
---

### 4. `repository`
- Contains **repository interfaces** that interact with MongoDB.
- Uses **Spring Data MongoDB** to automatically provide CRUD operations.
- Keeps **persistence logic separated** from business logic.

---

### 5. `service`
- Defines the **business logic layer**.  
- **Interfaces** in this layer declare the available operations.  
- **Implementations** in `impl` contain the actual logic and call repositories.  

---

### 6. `resources`
- Contains **configuration and property files** such as:
  - `application.properties` or `application.yml` → Database connection, server port, security configs.  
- Static resources if necessary.  

---

### 7. Main Application Class
- `ServiAutosBackendApplication` → The entry point of the Spring Boot application.  
- Annotated with `@SpringBootApplication`.  
- Bootstraps the application, scans for components, and starts the embedded server.  

---

