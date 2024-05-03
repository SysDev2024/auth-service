# AuthService Microservice

## Overview

AuthService is a microservice in our larger architecture, designed to handle user authorization tasks such as user registration and log inn, generating JWT token,validating email and reseting password. It supports operations for user registration, login, and email verification.

## Swagger Docs

Swagger documentaion is available when running the location localy in spring, docker or on a endpoint in the produciton server.

- **Local mvn spring-boot run**: `http://localhost:8111/auth/swagger-ui.html`
- **Local docker run**: `http://localhost:8111/auth/swagger-ui.html`
- **Endpoint API gateway production server**: `https://api.sparesti.tech/auth/swagger-ui.html`

## Getting Started

### Production Environment

In the production environment, AuthService is designed to operate within a secure, private network. It communicates with other services through this internal network, ensuring enhanced security and performance:

- **Internal Network**: AuthService and other microservices communicate over a private network, isolated from public access. This communication is done done through RabbitMQ.
- **API Gateway**: The only point of public access to AuthService in production is through the API gateway. This gateway routes external requests to the appropriate services securely.
- **Cloudflared Reverse Proxy Tunnel**
We use a Cloudflare Tunnel to securely expose the API Gateway in our private network to the internet. This approach ensures that the actual network endpoints of our services are never directly exposed online, which dramatically reduces the surface area for potential attacks. Cloudflare also handels TLS, DDOS Attacks, RateLimiting and Caching. 


* The API Gateway takes care of all JWT authentication, while authservice is used for authorization tasks.

[https://api.sparesti.tech/auth](https://api.sparesti.tech/auth)

[https://api.sparesti.tech/auth/swagger-ui.html](https://api.sparesti.tech/auth/swagger-ui.html)

This setup helps in managing security and traffic efficiently, ensuring that direct access to individual microservices like AuthService is restricted and controlled through the API gateway.

### Test Users

Test user for [https://api.sparesti.tech/auth](https://api.sparesti.tech/auth)
is

```

{
   "username": test
   "password": test
}
```

The same test user is provided in the docker-compose containing all the services. To run the spring-boot authService with maven, you can use the flag `--Dspring.profiles.active=build` to use a auto-generated H2 database. No user credentials is generated in H2, but you can register your own users. You can also run your own local MySQL database, and the maven spring-boot AuthService will try to connect to 127.0.0.1, with username: `service` and Password: `service`. You can edit the application.properties to change the database. Hibernate will auto-generate the tables.

### Prerequisites

#### Standalone

- Java 17+
- Git (to clone the repository)
- Maven (for building, dependencies and running)

#### Running as Docker

- Docker

> NB!:
> To build the docker you would still need maven and Java 17+
> You could still run the docker by downloading it from our docker repository

#### Running Everything

> This is for running the whole project with the architecture that is intended for production.
> This is for running all servises as well as the frontend

- Docker
- Docker Compose

### Cloning the Repository

To clone the repository and navigate into the authService directory, run:

```bash
git clone git@gitlab.stud.idi.ntnu.no:idatt2106_2024_11/auth-service.git --recurse

#This will pull the submodules aswell

cd auth-service
git submodule init
git submodule update --remote
#This is to make sure the submodules are updated

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

   Remember to have a MySQL database running. The easiest way is to run a MySQL docker:

   ```
   docker run -d \
    --name mysql \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=prodDB \
    -e MYSQL_USER=service \
    -e MYSQL_PASSWORD=service \
    -p 3306:3306 \
    --restart unless-stopped \
    mysql:8.0

   ```

   Or use a docker-compose with MySQL `docker-compose.yml`:

```
   mysql:
        image: mysql:8.0
        container_name: mysql
        environment:
        MYSQL_ROOT_PASSWORD: root
        MYSQL_DATABASE: prodDB
        MYSQL_USER: service
        MYSQL_PASSWORD: service
        ports:
            - "3306:3306"

```

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

- **Swagger UI**: Navigate to `http://localhost:8111/auth/swagger-ui.html` in your web browser to view and interact with the API documentation and test out the endpoints.

## Running the whole system

### Docker Compose

To run AuthService as part of the full architecture localy, ensure you have Docker Compose installed and use the following command in the `Local` directory containing the `docker-compose.yml` file:

```bash
docker-compose up
```

To run the docker-compose in the backgroun use this command:

```bash
docker-compose up -d
```

The docker compose setup includes a MySql database that spins up test data. This data and this database will be different for the production database.

##### As teachers you can ofc access our actual production database aswell
