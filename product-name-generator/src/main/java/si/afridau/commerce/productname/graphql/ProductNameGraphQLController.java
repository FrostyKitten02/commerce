package si.afridau.commerce.productname.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import si.afridau.commerce.productname.model.*;
import si.afridau.commerce.productname.service.ProductNameService;
import si.afridau.commerce.productname.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class ProductNameGraphQLController {

    @Autowired
    private ProductNameService productNameService;

    @Autowired
    private StatisticsService statisticsService;

    @QueryMapping
    public GenerationResponse generateNames(@Argument String category, @Argument List<String> keywords, @Argument Integer count) {
        long startTime = System.currentTimeMillis();
        
        GenerateNameRequest request = new GenerateNameRequest();
        request.setCategory(category);
        request.setKeywords(keywords != null ? keywords : List.of());
        request.setCount(count != null ? count : 5);
        request.setRequestTime(LocalDateTime.now());

        // Generate names using existing service
        GenerateNameResponse response = productNameService.generateNames(request);
        
        long processingTime = System.currentTimeMillis() - startTime;

        // Convert to GraphQL response format
        List<NameSuggestion> suggestions = response.getSuggestions().stream()
                .map(name -> createNameSuggestion(name, category, keywords, processingTime))
                .collect(Collectors.toList());

        statisticsService.recordRequest(category, keywords, (int)processingTime);

        GenerationResponse graphQLResponse = new GenerationResponse();
        graphQLResponse.setSuggestions(suggestions);
        graphQLResponse.setTotalGenerated(suggestions.size());
        graphQLResponse.setProcessingTime((int)processingTime);
        graphQLResponse.setCategory(category);
        graphQLResponse.setOriginalRequest(request);

        return graphQLResponse;
    }

    @QueryMapping
    public GenerationStatistics getStatistics() {
        return statisticsService.getStatistics();
    }

    @QueryMapping
    public List<String> getSupportedCategories() {
        return Arrays.asList("electronics", "clothing", "food", "books", "sports", "home");
    }

    private NameSuggestion createNameSuggestion(String name, String category, List<String> keywords, long processingTime) {
        Random random = new Random();
        float confidence = 0.7f + random.nextFloat() * 0.3f; // Random confidence between 0.7-1.0
        
        String pattern = determinePattern(name);
        
        return new NameSuggestion(name, category, confidence, keywords != null ? keywords : List.of(), pattern);
    }

    private String determinePattern(String name) {
        if (name.contains(" ")) {
            return "PREFIX_KEYWORD";
        } else if (name.endsWith("Pro") || name.endsWith("Max") || name.endsWith("Plus")) {
            return "KEYWORD_SUFFIX";
        } else {
            return "COMPOSITE";
        }
    }
}