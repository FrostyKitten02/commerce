package si.afridau.commerce.productname.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.afridau.commerce.productname.model.GenerateNameRequest;
import si.afridau.commerce.productname.model.GenerateNameResponse;
import si.afridau.commerce.productname.service.ProductNameService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Product Name Generator", description = "API for generating creative product names")
public class ProductNameController {

    @Autowired
    private ProductNameService productNameService;

    @Operation(
        summary = "Generate product names",
        description = "Generate creative product names based on keywords and category"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Product names generated successfully",
        content = @Content(schema = @Schema(implementation = GenerateNameResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/generate-name")
    public ResponseEntity<GenerateNameResponse> generateProductName(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request containing keywords, category, and count",
            content = @Content(schema = @Schema(implementation = GenerateNameRequest.class))
        )
        @RequestBody GenerateNameRequest request) {
        try {
            GenerateNameResponse response = productNameService.generateNames(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
        summary = "Generate product names (simple)",
        description = "Generate product names using query parameters for easy testing"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Product names generated successfully",
        content = @Content(schema = @Schema(implementation = GenerateNameResponse.class))
    )
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/generate-name/simple")
    public ResponseEntity<GenerateNameResponse> generateSimpleProductName(
            @Parameter(description = "Comma-separated keywords", example = "wireless,bluetooth")
            @RequestParam(required = false) String keywords,
            @Parameter(description = "Product category", example = "electronics")
            @RequestParam(required = false) String category,
            @Parameter(description = "Number of suggestions to generate", example = "5")
            @RequestParam(defaultValue = "5") int count) {
        
        try {
            GenerateNameRequest request = new GenerateNameRequest();
            
            if (keywords != null && !keywords.trim().isEmpty()) {
                List<String> keywordList = Arrays.asList(keywords.split(","));
                request.setKeywords(keywordList.stream().map(String::trim).toList());
            }
            
            request.setCategory(category);
            request.setCount(count);
            
            GenerateNameResponse response = productNameService.generateNames(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
        summary = "Health check",
        description = "Simple health check endpoint"
    )
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Product Name Generator is running");
    }

    @Operation(
        summary = "Get available categories",
        description = "Returns list of supported product categories"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Categories retrieved successfully"
    )
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAvailableCategories() {
        List<String> categories = Arrays.asList(
            "electronics", "clothing", "food", "books", "sports", "home"
        );
        return ResponseEntity.ok(categories);
    }
}