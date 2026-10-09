# 🏥 Hospital Management Backend

A RESTful API for hospital management, developed with **Java 21, Spring Boot, Spring Security, and MySQL**. The application provides a secure backend architecture with JWT authentication, role-based access control, database persistence, and Docker support.

## 📌 Table of Contents

* [Overview](#-overview)
* [Technologies](#-technologies)
* [Features](#-features)
* [Project Structure](#-project-structure)
* [Prerequisites](#-prerequisites)
* [Getting Started with Docker](#-getting-started-with-docker)
* [Environment Configuration](#-environment-configuration)
* [API Documentation](#-api-documentation)
* [Security and Authentication](#-security-and-authentication)
* [Database Configuration](#-database-configuration)
* [Troubleshooting](#-troubleshooting)
* [Author](#-author)

## 📖 Overview

The Hospital Management Backend is a Spring Boot application designed to provide REST API endpoints for managing hospital-related resources.

The project uses a layered backend architecture, a relational database, and JWT-based authentication to protect API endpoints and control access according to user roles.

The application can be run locally or deployed using Docker Compose, which starts the backend and MySQL database in separate containers.

## 🛠️ Technologies

| Technology        | Purpose                                    |
| ----------------- | ------------------------------------------ |
| Java 21           | Programming language                       |
| Spring Boot 3.4.4 | Backend framework                          |
| Spring Web        | REST API development                       |
| Spring Data JPA   | Database access and persistence            |
| Spring Security   | Authentication and authorization           |
| JWT               | Token-based authentication                 |
| MySQL 8.0         | Relational database                        |
| Maven             | Dependency management and build automation |
| MapStruct         | Object mapping                             |
| Lombok            | Boilerplate code reduction                 |
| Springdoc OpenAPI | API documentation with Swagger UI          |
| ZXing             | QR code and barcode-related functionality  |
| Docker            | Application containerization               |
| Docker Compose    | Multi-container orchestration              |

## ✨ Features

### Authentication and Security

* REST API authentication endpoints.
* JWT-based authentication.
* Role-based authorization using Spring Security.
* Password hashing with BCrypt.
* Stateless session management.
* Protected endpoints based on user roles.

### Hospital Resource Management

The security configuration defines access rules for the following API resources:

* Employee management
* File management
* Material management
* Medication management
* Location management
* Supplier management
* Stock management
* Stock order management
* Product order management
* Service access

The available operations for each resource depend on the implemented controllers and services.

### API Documentation

* OpenAPI documentation.
* Interactive Swagger UI.
* Bearer token authentication support in Swagger UI.

### Database

* MySQL relational database.
* Spring Data JPA integration.
* Hibernate schema management.
* Persistent database storage using a Docker volume.

### Docker Support

* Multi-stage Docker build.
* Separate backend and database containers.
* Automatic database readiness checks.
* Environment-based application configuration.
* Persistent MySQL data across container restarts.

## 📁 Project Structure

```text
hospital-management-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── hospital/
│   │   │           └── hospital_management_backend/
│   │   │               ├── config/
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               ├── repository/
│   │   │               ├── entity/
│   │   │               ├── dto/
│   │   │               └── HospitalManagementBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── .dockerignore
├── .env
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

*The package and folder names shown above are illustrative; keep the structure consistent with the actual files in your repository.*

## ⚙️ Prerequisites

Make sure you have installed:

* [Java 21](https://adoptium.net/) — required for local execution.
* [Maven](https://maven.apache.org/) — or use the Maven Wrapper included in the project.
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) — required for Docker deployment.
* [Git](https://git-scm.com/) — to clone the repository.

For the Docker setup, you do not need to install Java or MySQL separately on your host machine.

## 🚀 Getting Started with Docker

### 1. Clone the repository

```bash
git clone https://github.com/Anasadiouane/hospital-management-backend.git
cd hospital-management-backend
```

### 2. Configure environment variables

Create a `.env` file in the project root directory.

Add the following variables, using your own local development credentials:

```dotenv
MYSQL_ROOT_PASSWORD=your_mysql_password
JWT_TOKEN_SECRET=your_strong_jwt_secret
JWT_ACCESS_TOKEN_EXPIRATION=86400000
ADMIN_CIN=your_admin_cin
ADMIN_PASSWORD=your_admin_password
```

**Important:** Use strong, unique credentials. Do not commit the `.env` file to a public repository.

The `ADMIN_CIN` and `ADMIN_PASSWORD` variables are passed to the backend container. The application must explicitly read these variables and use them to initialize an administrator account for them to have any effect.

### 3. Build and start the containers

Make sure Docker Desktop is running, then execute:

```bash
docker compose up --build
```

This command builds the backend image, starts MySQL, waits for the database health check, and starts the Spring Boot application.

To run the services in the background:

```bash
docker compose up --build -d
```

### 4. Check the running containers

```bash
docker compose ps
```

View backend logs:

```bash
docker compose logs -f backend
```

View database logs:

```bash
docker compose logs -f db
```

### 5. Access the application

Once the backend has started successfully, use:

* **Backend base URL:** http://localhost:8081
* **Swagger UI:** http://localhost:8081/swagger-ui/index.html
* **OpenAPI JSON:** http://localhost:8081/v3/api-docs

The root URL may return `403 Forbidden` if no public route is configured for `/`. This does not necessarily indicate that the application failed to start. Test the Swagger UI or a valid API endpoint instead.

### 6. Stop the containers

```bash
docker compose down
```

To stop the containers and delete the database volume as well, run:

```bash
docker compose down -v
```

**Warning:** The `-v` option deletes the persistent MySQL data stored in the Compose volume.

## 🔐 Environment Configuration

The application uses environment variables to configure the database and JWT authentication.

| Variable                        | Description                                                          |
| ------------------------------- | -------------------------------------------------------------------- |
| `SERVER_PORT`                   | Backend port; defaults to `8081`                                     |
| `SPRING_DATASOURCE_URL`         | JDBC URL used to connect to MySQL                                    |
| `SPRING_DATASOURCE_USERNAME`    | Database username                                                    |
| `SPRING_DATASOURCE_PASSWORD`    | Database password                                                    |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Hibernate schema management mode                                     |
| `JWT_TOKEN_SECRET`              | Secret used by the application's JWT implementation                  |
| `JWT_ACCESS_TOKEN_EXPIRATION`   | Access-token lifetime in milliseconds                                |
| `MYSQL_ROOT_PASSWORD`           | MySQL root password used by Docker Compose                           |
| `ADMIN_CIN`                     | Optional administrator identifier, if implemented by the application |
| `ADMIN_PASSWORD`                | Optional administrator password, if implemented by the application   |

The current token expiration value, `86400000`, represents 24 hours.

## 📚 API Documentation

Swagger UI provides an interactive interface for exploring the API endpoints and testing requests.

Open:

http://localhost:8081/swagger-ui/index.html

To test a protected endpoint:

1. Authenticate using the implemented login endpoint.
2. Obtain a valid JWT access token from the API response.
3. Click **Authorize** in Swagger UI.
4. Enter the token using the format expected by the Swagger bearer authentication configuration.
5. Execute the protected request.

### Main API route groups

| Route                      | Purpose                            |
| -------------------------- | ---------------------------------- |
| `/api/v1/auth/login`       | Authentication                     |
| `/api/v1/auth/info`        | Authentication-related information |
| `/api/v1/employees/**`     | Employee resources                 |
| `/api/v1/files/**`         | File resources                     |
| `/api/v1/materials/**`     | Material resources                 |
| `/api/v1/medications/**`   | Medication resources               |
| `/api/v1/locations/**`     | Location resources                 |
| `/api/v1/suppliers/**`     | Supplier resources                 |
| `/api/v1/stocks/**`        | Stock resources                    |
| `/api/v1/stockOrders/**`   | Stock order resources              |
| `/api/v1/productOrders/**` | Product order resources            |
| `/api/v1/services`         | Service resources                  |

These route groups are based on the application's security configuration. Consult Swagger UI for the actual HTTP methods, request bodies, response formats, and complete endpoint list.

## 🛡️ Security and Authentication

The application uses Spring Security with JWT authentication and role-based access control.

Depending on the endpoint, access may be restricted to roles such as:

* `ADMIN`
* `MANAGEMENT_EMPLOYEES`
* `PRODUCT`
* `PHARMACY`
* `MATERIALS`

The login endpoint is configured as publicly accessible. Other endpoints may require authentication and the appropriate role.

**Security recommendations:**

* Never commit passwords, `.env` files, or production secrets to Git.
* Use a cryptographically strong JWT secret.
* Use HTTPS in production.
* Avoid using the MySQL root account for the backend in production; create a dedicated database user with limited permissions.
* Review CORS settings before deploying the application.
* Use a database migration tool such as Flyway or Liquibase for controlled production schema changes.

## 🗄️ Database Configuration

The application uses MySQL 8.0 with Spring Data JPA and Hibernate.

In Docker Compose, the backend connects to the database using the service hostname:

```text
jdbc:mysql://db:3306/hospital_db
```

Within the Docker network, `db` resolves to the MySQL container. The database does not need to expose port `3306` to the host for the backend to connect.

The named Docker volume:

```text
hospital_mysql_data
```

persists database files across container restarts and normal container removal.

The configured Hibernate mode is `update`. This is convenient for development, but production environments should use a controlled database migration strategy.

## 🧰 Troubleshooting

### Port 8081 is already in use

Stop the application or process currently using port `8081`, or change the host-side port mapping in `docker-compose.yml`.

### Port 3306 is already in use

If MySQL is only needed by the backend container, do not publish the database port to the host. The backend can still connect to `db:3306`.

### Backend returns 403 Forbidden

A `403 Forbidden` response may be caused by Spring Security rejecting a request.

* Check whether the URL is a configured public endpoint.
* Test Swagger UI at `/swagger-ui/index.html`.
* Review the authorization rules in `SecurityConfig`.
* Check backend logs for security-related errors.

A `403` at the root URL `/` alone does not prove that Docker or MySQL is broken.

### Backend cannot connect to MySQL

Check the container status and logs:

```bash
docker compose ps
docker compose logs db
docker compose logs backend
```

Verify that the database health check passes and that the backend uses `db:3306`, not `localhost:3306`.

### Changes are not reflected in Docker

Rebuild the image and restart the containers:

```bash
docker compose down
docker compose up --build
```

## 👨‍💻 Author

**Anas Adiouane**

GitHub: [Anasadiouane](https://github.com/Anasadiouane)

## 📄 License

No license has been specified in this repository documentation. Add a `LICENSE` file and update this section if you intend to distribute the project under an open-source license.
