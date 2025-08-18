# Product Name Generator

A serverless function that generates creative product names based on keywords and categories.

## Features

- **Category-based Generation**: Supports electronics, clothing, food, books, sports, and home categories
- **Keyword Integration**: Uses provided keywords to create relevant product names
- **Multiple Patterns**: Uses different naming patterns for variety
- **Serverless Ready**: Can be deployed to AWS Lambda, Vercel, or run as a regular Spring Boot service
- **REST API**: Simple HTTP endpoints for integration

## API Endpoints

### Generate Product Names
- `POST /api/generate-name` - Generate names with detailed request
- `GET /api/generate-name/simple` - Generate names with query parameters

### Utility Endpoints  
- `GET /health` - Health check
- `GET /categories` - Get available categories

## Usage Examples

### POST Request
```json
{
  "keywords": ["wireless", "bluetooth"],
  "category": "electronics",
  "count": 5
}
```

### Response
```json
{
  "suggestions": [
    "Tech Wireless Pro",
    "Bluetooth Elite",
    "Smart Device Max",
    "WirelessBluetooth",
    "Ultra Gadget Plus"
  ],
  "category": "electronics",
  "usedKeywords": ["wireless", "bluetooth"]
}
```

### GET Request
```
GET /api/generate-name/simple?keywords=coffee,organic&category=food&count=3
```

## Deployment Options

### 1. Spring Boot Service (Local/Docker)
```bash
# Run locally
mvn spring-boot:run

# Access at http://localhost:8087
```

### 2. Local Lambda Testing (Serverless)
**Prerequisites**: Install [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)

```bash
# Windows
run-local-lambda.bat

# Linux/Mac
chmod +x run-local-lambda.sh
./run-local-lambda.sh

# Access at http://localhost:3000
```

**Test the local Lambda**:
```bash
# Windows
test-lambda.bat

# Linux/Mac
chmod +x test-lambda.sh
./test-lambda.sh
```

**Direct Lambda invocation**:
```bash
sam local invoke ProductNameGeneratorFunction --event events/generate-name-event.json
```

### 3. AWS Lambda (Production)
1. Build the JAR:
   ```bash
   mvn clean package
   ```
2. Upload `target/product-name-generator-0.0.1-SNAPSHOT.jar` to AWS Lambda
3. Set handler: `si.afridau.commerce.productname.lambda.ProductNameLambdaHandler`
4. Configure API Gateway to trigger the function

### 4. AWS SAM Deploy (Infrastructure as Code)
```bash
# Deploy to AWS
sam build
sam deploy --guided
```

## Categories Supported

- **electronics**: Tech, Smart, Digital products
- **clothing**: Fashion, Style, Apparel items  
- **food**: Gourmet, Organic, Natural products
- **books**: Guides, Manuals, Reference materials
- **sports**: Athletic, Performance, Fitness gear
- **home**: Comfort, Modern, Elegant furnishings

## Integration with Commerce Platform

This service can be integrated into your admin panel for:
- Generating product names during product creation
- Suggesting names based on product descriptions
- Bulk name generation for import processes

### Example Integration
```javascript
// From admin panel
const response = await fetch('/api/generate-name', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    keywords: ['smartphone', 'android'],
    category: 'electronics',
    count: 10
  })
});

const { suggestions } = await response.json();
// Display suggestions in product creation form
```

## Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Java Version**: 21
- **Serverless**: AWS Lambda compatible
- **Build Tool**: Maven
- **Dependencies**: Jackson for JSON, AWS Lambda runtime