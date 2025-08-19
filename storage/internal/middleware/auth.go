package middleware

import (
	"encoding/base64"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"

	"storage/internal/config"
)

type Claims struct {
	Email       string   `json:"sub"`   // This is actually email/username
	Authorities []string `json:"auths"` // Changed to match Java AUTHORITIES_KEY = "auths"
	UserDetails string   `json:"usr"`   // JSON string containing actual user details
	jwt.RegisteredClaims
}

type UserDetails struct {
	ID    string `json:"id"` // The actual user ID
	Email string `json:"email"`
	// Add other fields as needed from JwtUser
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
		log.Printf("Attempting to parse JWT token with length: %d", len(tokenString))

		token, err := jwt.ParseWithClaims(tokenString, &Claims{}, func(token *jwt.Token) (interface{}, error) {
			log.Printf("JWT Header: %+v", token.Header)

			// Validate the signing method - must be HMAC
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				log.Printf("Invalid signing method: %v", token.Header["alg"])
				return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
			}

			// Ensure it's HS512 to match Java implementation
			if token.Method.Alg() != "HS512" {
				log.Printf("Invalid algorithm: %v, expected HS512", token.Method.Alg())
				return nil, fmt.Errorf("unexpected algorithm: %v, expected HS512", token.Method.Alg())
			}

			// Decode the secret using Base64URL to match Java implementation
			log.Printf("Decoding JWT secret...")
			secretBytes, err := base64.RawURLEncoding.DecodeString(cfg.JWT.Secret)
			if err != nil {
				log.Printf("Failed to decode JWT secret: %v", err)
				return nil, fmt.Errorf("failed to decode JWT secret: %v", err)
			}

			log.Printf("Successfully decoded secret, length: %d bytes", len(secretBytes))
			return secretBytes, nil
		})

		if err != nil {
			c.JSON(http.StatusUnauthorized, gin.H{"error": fmt.Sprintf("Invalid token: %v", err)})
			c.Abort()
			return
		}

		if claims, ok := token.Claims.(*Claims); ok && token.Valid {
			// Parse user details from JSON string immediately
			var userDetails UserDetails
			if claims.UserDetails != "" {
				if err := json.Unmarshal([]byte(claims.UserDetails), &userDetails); err != nil {
					log.Printf("Failed to parse user details JSON: %v", err)
					log.Printf("Raw user details string: %s", claims.UserDetails)
					c.JSON(http.StatusUnauthorized, gin.H{"error": "Invalid user details"})
					c.Abort()
					return
				}
			}

			log.Printf("Successfully parsed JWT - Email: %s, UserID: %s, Authorities: %v",
				claims.Email, userDetails.ID, claims.Authorities)

			c.Set("userID", userDetails.ID)
			c.Set("email", claims.Email)
			c.Set("authorities", claims.Authorities)
			c.Set("userDetails", userDetails)
			c.Next()
		} else {
			log.Printf("Token claims validation failed - Valid: %v, Claims OK: %v", token.Valid, ok)
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
			if auth == "ROLE_ADMIN" {
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
