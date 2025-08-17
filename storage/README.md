# Storage Service

A Go-based file storage microservice for the commerce application. Provides secure file upload and retrieval with JWT authentication and role-based access control.

## Features

- 🔐 **JWT Authentication** - Secure endpoints with role-based access
- 📁 **File Upload** - Admin-only file upload with validation
- 🖼️ **File Retrieval** - Public file access by UUID
- 🗄️ **PostgreSQL Storage** - File metadata stored in database
- 📝 **OpenAPI Documentation** - Auto-generated Swagger docs
- 🐳 **Docker Support** - Containerized deployment
- 🚀 **Gin Framework** - High-performance HTTP router

## API Endpoints

### Public Endpoints
- `GET /api/files/{id}` - Retrieve file content
- `GET /api/files/{id}/info` - Get file metadata
- `GET /api/health` - Health check

### Admin Endpoints (Requires ROLE_ADMIN)
- `POST /api/files/upload` - Upload file
- `DELETE /api/files/{id}` - Delete file

### Documentation
- `GET /swagger/index.html` - Swagger UI
- `GET /docs.yaml` - OpenAPI specification

## Quick Start

### Prerequisites
- Go 1.21+
- PostgreSQL database
- JWT secret (matching your auth service)

### Development Setup

1. **Clone and navigate to storage directory**
   ```bash
   cd storage
   ```

2. **Install dependencies**
   ```bash
   make deps
   ```

3. **Setup environment**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. **Create database**
   ```sql
   CREATE DATABASE "ws-commerce-storage";
   ```

5. **Generate documentation and run**
   ```bash
   make docs-run
   ```

## Configuration

Environment variables:
- `SERVER_PORT` - Server port (default: 8003)
- `DB_HOST` - Database host
- `DB_NAME` - Database name (ws-commerce-storage)
- `JWT_SECRET` - JWT signing secret
- `UPLOAD_DIR` - File storage directory
- `MAX_FILE_SIZE` - Maximum file size in bytes

## File Storage

Files are organized in date-based directories:
```
uploads/
├── 2024/
│   ├── 01/
│   │   ├── 15/
│   │   │   ├── uuid1.jpg
│   │   │   └── uuid2.png
```

## Authentication

The service validates JWT tokens from your auth service and checks for `ROLE_ADMIN` authority for protected endpoints.

## Usage Examples

### Upload File (Admin only)
```bash
curl -X POST \
  http://localhost:8003/api/files/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@image.jpg"
```

### Get File
```bash
curl http://localhost:8003/api/files/FILE_UUID
```

### Get File Info
```bash
curl http://localhost:8003/api/files/FILE_UUID/info
```

## Docker Deployment

```bash
# Build image
make docker-build

# Run container
make docker-run
```

## Available Make Commands

```bash
make build        # Build the application
make run          # Build and run
make dev          # Run with hot reload (requires air)
make docs         # Generate swagger documentation
make docs-run     # Generate docs and run
make test         # Run tests
make clean        # Clean build artifacts
make setup-dev    # Full development setup
```