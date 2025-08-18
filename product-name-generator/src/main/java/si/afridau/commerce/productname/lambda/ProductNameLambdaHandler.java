package si.afridau.commerce.productname.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import si.afridau.commerce.productname.model.GenerateNameRequest;
import si.afridau.commerce.productname.model.GenerateNameResponse;
import si.afridau.commerce.productname.service.ProductNameService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductNameLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ProductNameService productNameService;
    private final ObjectMapper objectMapper;

    public ProductNameLambdaHandler() {
        this.productNameService = new ProductNameService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        
        // Set CORS headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "Content-Type");
        headers.put("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        try {
            String httpMethod = request.getHttpMethod();
            String path = request.getPath();

            if ("OPTIONS".equals(httpMethod)) {
                // Handle CORS preflight
                return response.withStatusCode(200).withBody("{}");
            }

            if ("GET".equals(httpMethod) && "/health".equals(path)) {
                return response.withStatusCode(200)
                    .withBody("{\"status\":\"Product Name Generator Lambda is running\"}");
            }

            if ("GET".equals(httpMethod) && "/categories".equals(path)) {
                List<String> categories = Arrays.asList(
                    "electronics", "clothing", "food", "books", "sports", "home"
                );
                String categoriesJson = objectMapper.writeValueAsString(categories);
                return response.withStatusCode(200).withBody(categoriesJson);
            }

            if ("POST".equals(httpMethod) && ("/generate-name".equals(path) || "/api/generate-name".equals(path))) {
                GenerateNameRequest nameRequest = objectMapper.readValue(request.getBody(), GenerateNameRequest.class);
                GenerateNameResponse nameResponse = productNameService.generateNames(nameRequest);
                String responseJson = objectMapper.writeValueAsString(nameResponse);
                return response.withStatusCode(200).withBody(responseJson);
            }

            if ("GET".equals(httpMethod) && ("/generate-name/simple".equals(path) || "/api/generate-name/simple".equals(path))) {
                // Handle query parameters for simple GET request
                Map<String, String> queryParams = request.getQueryStringParameters();
                GenerateNameRequest nameRequest = new GenerateNameRequest();
                
                if (queryParams != null) {
                    String keywords = queryParams.get("keywords");
                    if (keywords != null && !keywords.trim().isEmpty()) {
                        List<String> keywordList = Arrays.asList(keywords.split(","));
                        nameRequest.setKeywords(keywordList.stream().map(String::trim).toList());
                    }
                    
                    nameRequest.setCategory(queryParams.get("category"));
                    
                    String countStr = queryParams.get("count");
                    if (countStr != null) {
                        try {
                            nameRequest.setCount(Integer.parseInt(countStr));
                        } catch (NumberFormatException e) {
                            nameRequest.setCount(5);
                        }
                    }
                }
                
                GenerateNameResponse nameResponse = productNameService.generateNames(nameRequest);
                String responseJson = objectMapper.writeValueAsString(nameResponse);
                return response.withStatusCode(200).withBody(responseJson);
            }

            return response.withStatusCode(404).withBody("{\"error\":\"Not Found\"}");

        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            return response.withStatusCode(500)
                .withBody("{\"error\":\"Internal Server Error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}