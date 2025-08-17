package repository

import (
	"github.com/google/uuid"
	"gorm.io/gorm"

	"storage/internal/models"
)

type FileRepository interface {
	Create(file *models.FileRecord) error
	GetByID(id uuid.UUID) (*models.FileRecord, error)
	GetAll(limit, offset int) ([]*models.FileRecord, error)
	Delete(id uuid.UUID) error
}

type fileRepository struct {
	db *gorm.DB
}

func NewFileRepository(db *gorm.DB) FileRepository {
	return &fileRepository{db: db}
}

func (r *fileRepository) Create(file *models.FileRecord) error {
	return r.db.Create(file).Error
}

func (r *fileRepository) GetByID(id uuid.UUID) (*models.FileRecord, error) {
	var file models.FileRecord
	err := r.db.Where("id = ?", id).First(&file).Error
	if err != nil {
		return nil, err
	}
	return &file, nil
}

func (r *fileRepository) GetAll(limit, offset int) ([]*models.FileRecord, error) {
	var files []*models.FileRecord
	err := r.db.Limit(limit).Offset(offset).Find(&files).Error
	return files, err
}

func (r *fileRepository) Delete(id uuid.UUID) error {
	return r.db.Where("id = ?", id).Delete(&models.FileRecord{}).Error
}
