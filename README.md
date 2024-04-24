# AuthService Microservice

## Overview

AuthService is a crucial microservice in our larger architecture, designed to handle user authentication and authorization tasks. It supports operations for user registration, login, and email verification.

## Features

- **User Registration**: Register new users via the `/auth/register` endpoint.
- **User Login**: Authenticate users and provide tokens via the `/auth/login` endpoint.
- **Email Verification**: Verify user email addresses via the `GET /verify/` endpoint.
- **Swagger Documentation**: Access the Swagger UI for the service's API documentation at `/swagger-ui.html`.

## Getting Started

### Prerequisites

#### Standalone

- Java 17+
- Git (to clone the repository)
- Maven (for building, dependencies and running)

#### Running as Docker

- Docker

> NB!:
> To build the docker you would still need maven and Java 17+
> You could still run the docker by downloading it from the docker repository

#### Running Everything

> This is for running the whole project with the architecture that is intended for production.
> This is for running all servises as well as the frontend

- Docker
- Docker Compose

### Cloning the Repository

To clone the repository and navigate into the authService directory, run:

```bash
git clone git@gitlab.stud.idi.ntnu.no:idatt2106_2024_11/authservice.git

Husk å ha gyldig SSH Nøkkler
```

### Building and Running Locally with Maven

To build and run AuthService locally using Maven, follow these steps:

1. **Build the project**:
   Navigate to the AuthService directory after cloning and run the following command to compile the project and download dependencies:

   ```bash
   mvn clean install
   ```

2. **Run the application**:
   After building, you can start the service locally using:

   ```bash
   mvn spring-boot:run
   ```

   This will start the AuthService on the default port defined in the application's configuration (typically port 8111).

### Building and Running with Docker Locally

To build and run AuthService using Docker, you can follow these instructions:

1. **Build the Docker image**:
   Ensure you are in the AuthService directory where the Dockerfile is located and run:

   ```bash
   docker build -t authservice:latest .
   ```

   This command builds a Docker image named `authservice` and tags it as `latest`.

2. **Run the Docker container**:
   After building the image, run it locally with:

   ```bash
   docker run -p 8111:8111 authservice:latest
   ```

   This command starts the container and exposes it on port 8111 on your local machine, allowing you to interact with AuthService through `localhost:8111`.

### Pulling and Running the Image from the Repository

If you prefer not to build the image yourself, you can pull it directly from the Docker registry and run it:

1. **Pull the image**:

   ```bash
   docker pull reg.sysdevservices.tech/authservice:latest
   ```

2. **Run the pulled image**:

   ```bash
   docker run -p 8111:8111 reg.sysdevservices.tech/authservice:latest
   ```

   As before, this will make the AuthService available on `localhost:8111`.

### Accessing the Service

Once the AuthService is running, either through Maven locally or via Docker, you can access its endpoints and Swagger UI:

- **Swagger UI**: Navigate to `http://localhost:8111/swagger-ui.html` in your web browser to view and interact with the API documentation and test out the endpoints.

## Additional Commands

### Docker Compose

To run AuthService as part of the full architecture, ensure you have Docker Compose installed and use the following command in the directory containing the `docker-compose.yml` file:

```bash
docker-compose up
```
