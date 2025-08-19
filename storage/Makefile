.PHONY: build run dev clean test docs docker-build docker-run

# Build the application
build:
	go build -o bin/storage main.go

# Run the application
run: build
	./bin/storage

# Run in development mode with hot reload (requires air)
dev:
	air

# Clean build artifacts
clean:
	rm -rf bin/
	rm -rf docs/

# Run tests
test:
	go test -v ./...

# Generate swagger documentation
docs:
	swag init -g main.go -o docs

# Generate swagger docs and run
docs-run: docs run

# Install dependencies
deps:
	go mod download
	go mod tidy

# Format code
fmt:
	go fmt ./...

# Lint code (requires golangci-lint)
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t storage-service .

# Docker run
docker-run:
	docker run -p 8003:8003 --env-file .env storage-service

# Create upload directory
setup:
	mkdir -p uploads

# Full setup for development
setup-dev: deps setup docs