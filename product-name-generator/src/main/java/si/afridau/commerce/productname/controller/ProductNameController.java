package si.afridau.commerce.productname.controller;

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
public class ProductNameController {

    @Autowired
    private ProductNameService productNameService;

    @PostMapping("/generate-name")
    public ResponseEntity<GenerateNameResponse> generateProductName(@RequestBody GenerateNameRequest request) {
        try {
            GenerateNameResponse response = productNameService.generateNames(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/generate-name/simple")
    public ResponseEntity<GenerateNameResponse> generateSimpleProductName(
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false) String category,
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

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Product Name Generator is running");
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAvailableCategories() {
        List<String> categories = Arrays.asList(
            "electronics", "clothing", "food", "books", "sports", "home"
        );
        return ResponseEntity.ok(categories);
    }
}