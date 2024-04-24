# AuthService Microservice

## Overview

AuthService is a microservice in our larger architecture, designed to handle user authentication and authorization tasks. It supports operations for user registration, login, and email verification.

## Features

- **User Registration**: Register new users via the `/auth/register` endpoint.
- **User Login**: Authenticate users and provide tokens via the `/auth/login` endpoint.
- **Email Verification**: Verify user email addresses via the `GET /verify/` endpoint.
- **Swagger Documentation**: Access the Swagger UI for the service's API documentation at `/swagger-ui.html`.

## Getting Started

## Test Access Point

### Testing and Development

For development and testing purposes, the AuthService is directly accessible via the following URL: [https://auth.sysdevservices.tech/](https://auth.sysdevservices.tech/)

This test endpoint allows developers and testers to interact with the AuthService independently of the larger system.

### Production Environment

In the production environment, AuthService is designed to operate within a secure, private network. It communicates with other services through this internal network, ensuring enhanced security and performance:

- **Internal Network**: AuthService and other microservices communicate over a private network, isolated from public access. This communication is done done through RabbitMQ.
- **API Gateway**: The only point of public access to AuthService in production is through the API gateway. This gateway routes external requests to the appropriate services securely. 

[https://api.sysdevservices.tech/](https://api.sysdevservices.tech/)

This setup helps in managing security and traffic efficiently, ensuring that direct access to individual microservices like AuthService is restricted and controlled through the API gateway.



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

#Remember to have valid SSH Keys to gitlab!
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

### Running tests
Testing the AuthService can be done by using the maven command:

 ```
mvn run test
 ```
This is also done automatically in the CICD pipeline


### Accessing the Service

Once the AuthService is running, either through Maven locally or via Docker, you can access its endpoints and Swagger UI:

- **Swagger UI**: Navigate to `http://localhost:8111/swagger-ui.html` in your web browser to view and interact with the API documentation and test out the endpoints.

## Running the whole system

### Docker Compose

To run AuthService as part of the full architecture, ensure you have Docker Compose installed and use the following command in the directory containing the `docker-compose.yml` file:

```bash
docker-compose up
```
To run the docker-compose in the backgroun use this command:
```bash
docker-compose up -d
```
The docker compose setup includes a MySql database that spins up test data. This data and this database will be different for the production database. 

##### As teachers you can ofc access our actual production database aswell

