package models

import (
	"time"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

// FileRecord represents a file stored in the system
type FileRecord struct {
	ID           uuid.UUID      `json:"id" gorm:"type:uuid;primary_key;default:gen_random_uuid()" example:"123e4567-e89b-12d3-a456-426614174000"`
	FileName     string         `json:"fileName" gorm:"not null" example:"image.jpg"`
	OriginalName string         `json:"originalName" gorm:"not null" example:"my-photo.jpg"`
	MimeType     string         `json:"mimeType" gorm:"not null" example:"image/jpeg"`
	Size         int64          `json:"size" gorm:"not null" example:"1024000"`
	RelativePath string         `json:"relativePath" gorm:"not null" example:"2024/01/15/image.jpg"`
	AbsolutePath string         `json:"absolutePath" gorm:"not null" example:"/uploads/2024/01/15/image.jpg"`
	UploadedBy   *uuid.UUID     `json:"uploadedBy,omitempty" gorm:"type:uuid" example:"123e4567-e89b-12d3-a456-426614174000"`
	CreatedAt    time.Time      `json:"createdAt" example:"2024-01-15T14:30:00Z"`
	UpdatedAt    time.Time      `json:"updatedAt" example:"2024-01-15T14:30:00Z"`
	DeletedAt    gorm.DeletedAt `json:"-" gorm:"index"`
}

// FileUploadResponse represents the response after successful file upload
type FileUploadResponse struct {
	ID           uuid.UUID `json:"id" example:"123e4567-e89b-12d3-a456-426614174000"`
	FileName     string    `json:"fileName" example:"image.jpg"`
	OriginalName string    `json:"originalName" example:"my-photo.jpg"`
	MimeType     string    `json:"mimeType" example:"image/jpeg"`
	Size         int64     `json:"size" example:"1024000"`
	URL          string    `json:"url" example:"/api/files/123e4567-e89b-12d3-a456-426614174000"`
	CreatedAt    time.Time `json:"createdAt" example:"2024-01-15T14:30:00Z"`
}

// ErrorResponse represents an error response
type ErrorResponse struct {
	Error   string `json:"error" example:"File not found"`
	Message string `json:"message,omitempty" example:"The requested file does not exist"`
	Code    int    `json:"code,omitempty" example:"404"`
}

// TableName returns the table name for FileRecord
func (FileRecord) TableName() string {
	return "files"
}
