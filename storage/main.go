package main

import (
	"log"

	"storage/internal/app"
	"storage/internal/config"
)

// @title Storage Service API
// @version 1.0
// @description A file storage service for commerce application
// @termsOfService http://swagger.io/terms/

// @contact.name API Support
// @contact.url http://www.swagger.io/support
// @contact.email support@swagger.io

// @license.name Apache 2.0
// @license.url http://www.apache.org/licenses/LICENSE-2.0.html

// @host localhost:8003
// @BasePath /api

// @securityDefinitions.apikey BearerAuth
// @in header
// @name Authorization
// @description Type "Bearer" followed by a space and JWT token.

func main() {
	cfg, err := config.Load()
	if err != nil {
		log.Fatalf("Failed to load config: %v", err)
	}

	app, err2 := app.New(cfg)
	if err2 != nil {
		log.Fatalf("Failed to create app: %v", err2)
	}

	if err3 := app.Run(); err3 != nil {
		log.Fatalf("Failed to run app: %v", err3)
	}
}
