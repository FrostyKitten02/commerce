package middleware

import (
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"

	"storage/internal/config"
)

type Claims struct {
	UserID      string   `json:"sub"`
	Email       string   `json:"email"`
	Authorities []string `json:"authorities"`
	jwt.RegisteredClaims
}

func JWTAuthMiddleware(cfg *config.Config) gin.HandlerFunc {
	return func(c *gin.Context) {
		authHeader := c.GetHeader("Authorization")
		if authHeader == "" {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Authorization header required"})
			c.Abort()
			return
		}

		bearerToken := strings.Split(authHeader, " ")
		if len(bearerToken) != 2 || strings.ToLower(bearerToken[0]) != "bearer" {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Invalid authorization header format"})
			c.Abort()
			return
		}

		tokenString := bearerToken[1]
		token, err := jwt.ParseWithClaims(tokenString, &Claims{}, func(token *jwt.Token) (interface{}, error) {
			return []byte(cfg.JWT.Secret), nil
		})

		if err != nil {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Invalid token"})
			c.Abort()
			return
		}

		if claims, ok := token.Claims.(*Claims); ok && token.Valid {
			c.Set("userID", claims.UserID)
			c.Set("email", claims.Email)
			c.Set("authorities", claims.Authorities)
			c.Next()
		} else {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Invalid token claims"})
			c.Abort()
			return
		}
	}
}

func RequireAdminRole() gin.HandlerFunc {
	return func(c *gin.Context) {
		authorities, exists := c.Get("authorities")
		if !exists {
			c.JSON(http.StatusForbidden, gin.H{"error": "No authorities found"})
			c.Abort()
			return
		}

		authList, ok := authorities.([]string)
		if !ok {
			c.JSON(http.StatusForbidden, gin.H{"error": "Invalid authorities format"})
			c.Abort()
			return
		}

		hasAdminRole := false
		for _, auth := range authList {
			if auth == "ROLE_ADMIN" || auth == "ADMIN" {
				hasAdminRole = true
				break
			}
		}

		if !hasAdminRole {
			c.JSON(http.StatusForbidden, gin.H{"error": "Admin role required"})
			c.Abort()
			return
		}

		c.Next()
	}
}
