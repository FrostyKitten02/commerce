package app

import (
	"fmt"
	"log"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"

	"storage/internal/config"
	"storage/internal/database"
	"storage/internal/handlers"
	"storage/internal/middleware"
	"storage/internal/repository"
	"storage/internal/service"
)

type App struct {
	config *config.Config
	router *gin.Engine
}

func New(cfg *config.Config) (*App, error) {
	// Initialize database
	db, err := database.New(cfg)
	if err != nil {
		return nil, fmt.Errorf("failed to initialize database: %w", err)
	}

	// Initialize repositories
	fileRepo := repository.NewFileRepository(db.DB)

	// Initialize services
	fileService := service.NewFileService(fileRepo, cfg)

	// Initialize handlers
	fileHandler := handlers.NewFileHandler(fileService)

	// Initialize Gin router
	router := gin.Default()

	// Configure CORS
	config := cors.DefaultConfig()
	config.AllowAllOrigins = true
	config.AllowHeaders = []string{"Origin", "Content-Length", "Content-Type", "Authorization"}
	config.AllowMethods = []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"}
	router.Use(cors.New(config))

	// API routes group
	api := router.Group("/api")
	{
		// Health check endpoint
		api.GET("/health", func(c *gin.Context) {
			c.JSON(200, gin.H{"status": "ok", "service": "storage"})
		})

		// File routes
		files := api.Group("/files")
		{
			// Public endpoints (no auth required)
			files.GET("/:id", fileHandler.GetFile)
			files.GET("/:id/info", fileHandler.GetFileInfo)

			// Admin-only endpoints (require authentication and admin role)
			files.POST("/upload",
				middleware.JWTAuthMiddleware(cfg),
				middleware.RequireAdminRole(),
				fileHandler.UploadFile,
			)
			files.DELETE("/:id",
				middleware.JWTAuthMiddleware(cfg),
				middleware.RequireAdminRole(),
				fileHandler.DeleteFile,
			)
		}
	}

	// Swagger documentation
	router.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))
	router.GET("/docs.yaml", func(c *gin.Context) {
		c.Header("Content-Type", "application/x-yaml")
		c.File("./docs/swagger.yaml")
	})

	return &App{
		config: cfg,
		router: router,
	}, nil
}

func (a *App) Run() error {
	address := fmt.Sprintf("%s:%s", a.config.Server.Host, a.config.Server.Port)
	log.Printf("Starting storage service on %s", address)
	return a.router.Run(address)
}
