package handlers

import (
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"

	"storage/internal/models"
	"storage/internal/service"
)

type FileHandler struct {
	fileService service.FileService
}

func NewFileHandler(fileService service.FileService) *FileHandler {
	return &FileHandler{
		fileService: fileService,
	}
}

// UploadFile godoc
// @Summary Upload a file
// @Description Upload a file to the storage service. Only accessible by admin users.
// @Tags files
// @Accept multipart/form-data
// @Produce json
// @Param file formData file true "File to upload"
// @Success 201 {object} models.FileUploadResponse
// @Failure 400 {object} models.ErrorResponse
// @Failure 401 {object} models.ErrorResponse
// @Failure 403 {object} models.ErrorResponse
// @Failure 413 {object} models.ErrorResponse
// @Failure 500 {object} models.ErrorResponse
// @Security BearerAuth
// @Router /files/upload [post]
func (h *FileHandler) UploadFile(c *gin.Context) {
	// Get file from form
	file, err := c.FormFile("file")
	if err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{
			Error:   "No file provided",
			Message: "Please provide a file in the 'file' form field",
			Code:    http.StatusBadRequest,
		})
		return
	}

	// Get user ID from context (set by JWT middleware)
	userIDStr, exists := c.Get("userID")
	var userID *uuid.UUID
	if exists {
		if parsedID, err := uuid.Parse(userIDStr.(string)); err == nil {
			userID = &parsedID
		}
	}

	// Upload file
	response, err := h.fileService.UploadFile(file, userID)
	if err != nil {
		statusCode := http.StatusInternalServerError
		if err.Error() == "file type not allowed" {
			statusCode = http.StatusBadRequest
		} else if err.Error() == "file size exceeds maximum allowed size" {
			statusCode = http.StatusRequestEntityTooLarge
		}

		c.JSON(statusCode, models.ErrorResponse{
			Error:   "Upload failed",
			Message: err.Error(),
			Code:    statusCode,
		})
		return
	}

	c.JSON(http.StatusCreated, response)
}

// GetFile godoc
// @Summary Get file by ID
// @Description Retrieve a file by its UUID. Returns the actual file content.
// @Tags files
// @Accept json
// @Produce application/octet-stream
// @Param id path string true "File ID (UUID)"
// @Success 200 {file} binary
// @Failure 400 {object} models.ErrorResponse
// @Failure 404 {object} models.ErrorResponse
// @Failure 500 {object} models.ErrorResponse
// @Router /files/{id} [get]
func (h *FileHandler) GetFile(c *gin.Context) {
	idParam := c.Param("id")
	fileID, err := uuid.Parse(idParam)
	if err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{
			Error:   "Invalid file ID",
			Message: "File ID must be a valid UUID",
			Code:    http.StatusBadRequest,
		})
		return
	}

	content, mimeType, err := h.fileService.GetFileContent(fileID)
	if err != nil {
		statusCode := http.StatusNotFound
		if err.Error() != "record not found" {
			statusCode = http.StatusInternalServerError
		}

		c.JSON(statusCode, models.ErrorResponse{
			Error:   "File not found",
			Message: err.Error(),
			Code:    statusCode,
		})
		return
	}

	c.Header("Content-Type", mimeType)
	c.Header("Content-Length", strconv.Itoa(len(content)))
	c.Data(http.StatusOK, mimeType, content)
}

// GetFileInfo godoc
// @Summary Get file information by ID
// @Description Retrieve file metadata by its UUID without downloading the file content.
// @Tags files
// @Accept json
// @Produce json
// @Param id path string true "File ID (UUID)"
// @Success 200 {object} models.FileRecord
// @Failure 400 {object} models.ErrorResponse
// @Failure 404 {object} models.ErrorResponse
// @Failure 500 {object} models.ErrorResponse
// @Router /files/{id}/info [get]
func (h *FileHandler) GetFileInfo(c *gin.Context) {
	idParam := c.Param("id")
	fileID, err := uuid.Parse(idParam)
	if err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{
			Error:   "Invalid file ID",
			Message: "File ID must be a valid UUID",
			Code:    http.StatusBadRequest,
		})
		return
	}

	fileRecord, err := h.fileService.GetFile(fileID)
	if err != nil {
		statusCode := http.StatusNotFound
		if err.Error() != "record not found" {
			statusCode = http.StatusInternalServerError
		}

		c.JSON(statusCode, models.ErrorResponse{
			Error:   "File not found",
			Message: err.Error(),
			Code:    statusCode,
		})
		return
	}

	c.JSON(http.StatusOK, fileRecord)
}

// DeleteFile godoc
// @Summary Delete a file
// @Description Delete a file by its UUID. Only accessible by admin users.
// @Tags files
// @Accept json
// @Produce json
// @Param id path string true "File ID (UUID)"
// @Success 204 "No Content"
// @Failure 400 {object} models.ErrorResponse
// @Failure 401 {object} models.ErrorResponse
// @Failure 403 {object} models.ErrorResponse
// @Failure 404 {object} models.ErrorResponse
// @Failure 500 {object} models.ErrorResponse
// @Security BearerAuth
// @Router /files/{id} [delete]
func (h *FileHandler) DeleteFile(c *gin.Context) {
	idParam := c.Param("id")
	fileID, err := uuid.Parse(idParam)
	if err != nil {
		c.JSON(http.StatusBadRequest, models.ErrorResponse{
			Error:   "Invalid file ID",
			Message: "File ID must be a valid UUID",
			Code:    http.StatusBadRequest,
		})
		return
	}

	err = h.fileService.DeleteFile(fileID)
	if err != nil {
		statusCode := http.StatusNotFound
		if err.Error() != "record not found" {
			statusCode = http.StatusInternalServerError
		}

		c.JSON(statusCode, models.ErrorResponse{
			Error:   "Delete failed",
			Message: err.Error(),
			Code:    statusCode,
		})
		return
	}

	c.Status(http.StatusNoContent)
}
