# Build stage
FROM golang:1.21-alpine AS builder

WORKDIR /app

# Install build dependencies
RUN apk add --no-cache git

# Copy go mod files
COPY go.mod go.sum ./
RUN go mod download

# Copy source code
COPY . .

# Build the application
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o storage main.go

# Final stage
FROM alpine:latest

# Install ca-certificates for HTTPS requests
RUN apk --no-cache add ca-certificates

WORKDIR /root/

# Create upload directory
RUN mkdir -p uploads

# Copy binary from builder stage
COPY --from=builder /app/storage .

# Copy docs directory (commented out - create if needed)
# COPY --from=builder /app/docs ./docs

# Expose port
EXPOSE 8003

# Run the binary
CMD ["./storage"]