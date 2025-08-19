# Commerce Platform - Docker Setup

This document describes how to run the entire commerce platform using Docker.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose v3.8+
- At least 4GB of available RAM
- Ports 80, 5432, 8000-8004 available on your system

## Services Overview

The platform consists of the following services:

| Service | Technology | Port | Description |
|---------|------------|------|-------------|
| postgres | PostgreSQL 15 | 5432 | Database for all services |
| auth-service | Spring Boot (Java 21) | 8000 | User authentication and authorization |
| catalog-service | Spring Boot (Java 21) | 8001 | Product catalog management |
| cart-service | Spring Boot (Java 21) | 8002 | Shopping cart functionality |
| storage-service | Go | 8003 | File upload and storage |
| checkout-service | Spring Boot (Java 21) | 8004 | Order processing and checkout |
| web-app | React/Nginx | 80 | Frontend web application |

## Quick Start

### 1. Build All Services

**Windows:**
```bash
build-all.bat
```

**Linux/Mac:**
```bash
chmod +x build-all.sh
./build-all.sh
```

### 2. Start All Services

```bash
docker-compose up --build
```

Or run in detached mode:
```bash
docker-compose up -d --build
```

### 3. Access the Application

- **Web Application**: http://localhost
- **API Documentation**:
  - Auth Service: http://localhost:8000/swagger-ui
  - Catalog Service: http://localhost:8001/swagger-ui  
  - Cart Service: http://localhost:8002/swagger-ui
  - Checkout Service: http://localhost:8004/swagger-ui

## Environment Variables

Each service receives its dependencies as environment variables:

### Java Services (Spring Boot)
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SECURITY_CONSTANT_SECRET`: JWT signing secret
- `*_WS_BASE_PATH`: URLs for inter-service communication

### Storage Service (Go)
- `DB_HOST`, `DB_PORT`, `DB_NAME`: Database connection
- `JWT_SECRET`: JWT verification secret
- `PORT`: Service port

## Docker Network

All services communicate within the `commerce-network` Docker network using hostnames:

- `postgres`: Database server
- `auth-service`: Authentication service
- `catalog-service`: Catalog service  
- `cart-service`: Cart service
- `checkout-service`: Checkout service
- `storage-service`: Storage service

## Data Persistence

The following data is persisted in Docker volumes:

- `postgres_data`: All database data
- `storage_uploads`: Uploaded files

## Development Commands

### View Logs
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs auth-service
docker-compose logs catalog-service
```

### Restart a Service
```bash
docker-compose restart auth-service
```

### Rebuild a Service
```bash
docker-compose up --build auth-service
```

### Stop All Services
```bash
docker-compose down
```

### Stop and Remove Volumes
```bash
docker-compose down -v
```

## Database Setup

The PostgreSQL container automatically creates the following databases:
- `auth_ws`: User authentication data
- `catalog_ws`: Product catalog data
- `cart_ws`: Shopping cart data
- `checkout_ws`: Order and transaction data
- `storage_ws`: File metadata

Database migrations run automatically when each service starts.

## Troubleshooting

### Service Won't Start
1. Check if all ports are available
2. Ensure Docker has enough memory allocated
3. Verify JAR files exist in target directories

### Database Connection Issues
1. Wait for PostgreSQL to be fully ready (health check)
2. Check database connection strings
3. Verify database names match

### Build Failures
1. Ensure all Maven dependencies are built (auth-common, exception-handling)
2. Check Node.js version for web-app
3. Verify Docker has access to build context

## Production Considerations

For production deployment:

1. Use external PostgreSQL database
2. Configure proper secrets management
3. Set up SSL/TLS certificates
4. Configure monitoring and logging
5. Use Docker secrets instead of environment variables
6. Set resource limits for containers