package service

import (
	"fmt"
	"io"
	"mime/multipart"
	"os"
	"path/filepath"
	"strings"
	"time"

	"github.com/google/uuid"

	"storage/internal/config"
	"storage/internal/models"
	"storage/internal/repository"
)

type FileService interface {
	UploadFile(file *multipart.FileHeader, userID *uuid.UUID) (*models.FileUploadResponse, error)
	GetFile(id uuid.UUID) (*models.FileRecord, error)
	GetFileContent(id uuid.UUID) ([]byte, string, error)
	DeleteFile(id uuid.UUID) error
}

type fileService struct {
	repo   repository.FileRepository
	config *config.Config
}

func NewFileService(repo repository.FileRepository, cfg *config.Config) FileService {
	return &fileService{
		repo:   repo,
		config: cfg,
	}
}

func (s *fileService) UploadFile(fileHeader *multipart.FileHeader, userID *uuid.UUID) (*models.FileUploadResponse, error) {
	// Validate file size
	if fileHeader.Size > s.config.Storage.MaxFileSize {
		return nil, fmt.Errorf("file size exceeds maximum allowed size of %d bytes", s.config.Storage.MaxFileSize)
	}

	// Validate file type (basic validation)
	if !s.isAllowedFileType(fileHeader.Header.Get("Content-Type")) {
		return nil, fmt.Errorf("file type not allowed")
	}

	// Open uploaded file
	src, err := fileHeader.Open()
	if err != nil {
		return nil, fmt.Errorf("failed to open uploaded file: %w", err)
	}
	defer src.Close()

	// Generate unique filename and path
	fileID := uuid.New()
	ext := filepath.Ext(fileHeader.Filename)
	newFileName := fmt.Sprintf("%s%s", fileID.String(), ext)

	// Create directory structure based on date
	now := time.Now()
	dateDir := fmt.Sprintf("%d/%02d/%02d", now.Year(), now.Month(), now.Day())
	relativeDir := filepath.Join(dateDir)
	absoluteDir := filepath.Join(s.config.Storage.UploadDir, relativeDir)

	// Create directory if it doesn't exist
	if err := os.MkdirAll(absoluteDir, 0755); err != nil {
		return nil, fmt.Errorf("failed to create upload directory: %w", err)
	}

	// Full file paths
	relativePath := filepath.Join(relativeDir, newFileName)
	absolutePath := filepath.Join(absoluteDir, newFileName)

	// Create destination file
	dst, err := os.Create(absolutePath)
	if err != nil {
		return nil, fmt.Errorf("failed to create destination file: %w", err)
	}
	defer dst.Close()

	// Copy file content
	if _, err := io.Copy(dst, src); err != nil {
		return nil, fmt.Errorf("failed to copy file content: %w", err)
	}

	// Create file record
	fileRecord := &models.FileRecord{
		ID:           fileID,
		FileName:     newFileName,
		OriginalName: fileHeader.Filename,
		MimeType:     fileHeader.Header.Get("Content-Type"),
		Size:         fileHeader.Size,
		RelativePath: relativePath,
		AbsolutePath: absolutePath,
		UploadedBy:   userID,
	}

	// Save to database
	if err := s.repo.Create(fileRecord); err != nil {
		// Clean up file if database save fails
		os.Remove(absolutePath)
		return nil, fmt.Errorf("failed to save file record: %w", err)
	}

	// Return response
	response := &models.FileUploadResponse{
		ID:           fileRecord.ID,
		FileName:     fileRecord.FileName,
		OriginalName: fileRecord.OriginalName,
		MimeType:     fileRecord.MimeType,
		Size:         fileRecord.Size,
		URL:          fmt.Sprintf("/api/files/%s", fileRecord.ID.String()),
		CreatedAt:    fileRecord.CreatedAt,
	}

	return response, nil
}

func (s *fileService) GetFile(id uuid.UUID) (*models.FileRecord, error) {
	return s.repo.GetByID(id)
}

func (s *fileService) GetFileContent(id uuid.UUID) ([]byte, string, error) {
	fileRecord, err := s.repo.GetByID(id)
	if err != nil {
		return nil, "", err
	}

	content, err := os.ReadFile(fileRecord.AbsolutePath)
	if err != nil {
		return nil, "", fmt.Errorf("failed to read file content: %w", err)
	}

	return content, fileRecord.MimeType, nil
}

func (s *fileService) DeleteFile(id uuid.UUID) error {
	fileRecord, err := s.repo.GetByID(id)
	if err != nil {
		return err
	}

	// Delete file from filesystem
	if err := os.Remove(fileRecord.AbsolutePath); err != nil && !os.IsNotExist(err) {
		return fmt.Errorf("failed to delete file from filesystem: %w", err)
	}

	// Delete from database
	return s.repo.Delete(id)
}

func (s *fileService) isAllowedFileType(mimeType string) bool {
	allowedTypes := []string{
		"image/jpeg",
		"image/jpg",
		"image/png",
		"image/gif",
		"image/webp",
		"image/svg+xml",
		"application/pdf",
		"text/plain",
		"application/json",
	}

	for _, allowedType := range allowedTypes {
		if strings.EqualFold(mimeType, allowedType) {
			return true
		}
	}

	return false
}
