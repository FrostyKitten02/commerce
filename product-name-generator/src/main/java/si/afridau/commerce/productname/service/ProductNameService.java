package si.afridau.commerce.productname.service;

import org.springframework.stereotype.Service;
import si.afridau.commerce.productname.model.GenerateNameRequest;
import si.afridau.commerce.productname.model.GenerateNameResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductNameService {

    private static final Map<String, List<String>> CATEGORY_PREFIXES = Map.of(
        "electronics", Arrays.asList("Tech", "Digital", "Smart", "Pro", "Ultra", "Max", "Elite", "Prime", "Cyber", "Neo"),
        "clothing", Arrays.asList("Style", "Fashion", "Comfort", "Luxury", "Classic", "Modern", "Urban", "Premium", "Casual", "Elegant"),
        "food", Arrays.asList("Fresh", "Organic", "Gourmet", "Natural", "Pure", "Artisan", "Premium", "Classic", "Homemade", "Delicious"),
        "books", Arrays.asList("Complete", "Essential", "Ultimate", "Comprehensive", "Advanced", "Beginner", "Master", "Expert", "Guide", "Handbook"),
        "sports", Arrays.asList("Pro", "Athletic", "Performance", "Elite", "Champion", "Power", "Speed", "Flex", "Ultimate", "Active"),
        "home", Arrays.asList("Comfort", "Cozy", "Modern", "Classic", "Luxury", "Essential", "Perfect", "Smart", "Elegant", "Stylish")
    );

    private static final List<String> GENERAL_PREFIXES = Arrays.asList(
        "Super", "Mega", "Ultra", "Premium", "Deluxe", "Professional", "Advanced", "Perfect", "Ultimate", "Superior"
    );

    private static final List<String> SUFFIXES = Arrays.asList(
        "Pro", "Plus", "Max", "Elite", "Prime", "X", "Ultra", "Edition", "Series", "Collection", "Line", "Set"
    );

    private static final Map<String, List<String>> CATEGORY_KEYWORDS = Map.of(
        "electronics", Arrays.asList("Device", "Gadget", "System", "Unit", "Hub", "Station", "Player", "Display", "Monitor", "Controller"),
        "clothing", Arrays.asList("Wear", "Apparel", "Outfit", "Style", "Collection", "Line", "Series", "Fashion", "Garment", "Attire"),
        "food", Arrays.asList("Blend", "Mix", "Recipe", "Taste", "Flavor", "Delight", "Treat", "Cuisine", "Dish", "Specialty"),
        "books", Arrays.asList("Guide", "Manual", "Handbook", "Reference", "Collection", "Series", "Edition", "Volume", "Compendium", "Library"),
        "sports", Arrays.asList("Gear", "Equipment", "Kit", "Set", "Tools", "Accessories", "Performance", "Training", "Fitness", "Athletic"),
        "home", Arrays.asList("Collection", "Set", "Series", "Line", "Suite", "System", "Kit", "Essentials", "Decor", "Furnishing")
    );

    public GenerateNameResponse generateNames(GenerateNameRequest request) {
        String category = request.getCategory() != null ? request.getCategory().toLowerCase() : "general";
        List<String> keywords = request.getKeywords() != null ? request.getKeywords() : new ArrayList<>();
        int count = Math.min(request.getCount(), 20); // Limit to 20 suggestions max

        List<String> suggestions = new ArrayList<>();
        Random random = new Random();

        // Get category-specific prefixes and keywords
        List<String> prefixes = CATEGORY_PREFIXES.getOrDefault(category, GENERAL_PREFIXES);
        List<String> categoryKeywords = CATEGORY_KEYWORDS.getOrDefault(category, Arrays.asList("Product", "Item", "Thing"));

        // Generate names using different patterns
        for (int i = 0; i < count; i++) {
            String name = generateSingleName(keywords, prefixes, categoryKeywords, SUFFIXES, random, i);
            if (!suggestions.contains(name)) {
                suggestions.add(name);
            }
        }

        // Ensure we have enough unique suggestions
        while (suggestions.size() < count) {
            String name = generateSingleName(keywords, prefixes, categoryKeywords, SUFFIXES, random, suggestions.size());
            if (!suggestions.contains(name)) {
                suggestions.add(name);
            }
        }

        return new GenerateNameResponse(suggestions, category, keywords);
    }

    private String generateSingleName(List<String> keywords, List<String> prefixes, 
                                    List<String> categoryKeywords, List<String> suffixes, 
                                    Random random, int iteration) {
        
        // Different naming patterns
        int pattern = iteration % 6;
        
        switch (pattern) {
            case 0: // Prefix + Keyword
                if (!keywords.isEmpty()) {
                    return getRandomElement(prefixes, random) + " " + 
                           capitalizeFirst(getRandomElement(keywords, random));
                }
                return getRandomElement(prefixes, random) + " " + 
                       getRandomElement(categoryKeywords, random);
                
            case 1: // Keyword + Suffix
                if (!keywords.isEmpty()) {
                    return capitalizeFirst(getRandomElement(keywords, random)) + " " + 
                           getRandomElement(suffixes, random);
                }
                return getRandomElement(categoryKeywords, random) + " " + 
                       getRandomElement(suffixes, random);
                
            case 2: // Prefix + Category Keyword + Suffix
                return getRandomElement(prefixes, random) + " " + 
                       getRandomElement(categoryKeywords, random) + " " + 
                       getRandomElement(suffixes, random);
                
            case 3: // Two Keywords Combined
                if (keywords.size() >= 2) {
                    List<String> shuffled = new ArrayList<>(keywords);
                    Collections.shuffle(shuffled, random);
                    return capitalizeFirst(shuffled.get(0)) + 
                           capitalizeFirst(shuffled.get(1));
                }
                return getRandomElement(prefixes, random) + 
                       getRandomElement(categoryKeywords, random);
                
            case 4: // Keyword + Category Keyword
                if (!keywords.isEmpty()) {
                    return capitalizeFirst(getRandomElement(keywords, random)) + " " + 
                           getRandomElement(categoryKeywords, random);
                }
                return getRandomElement(prefixes, random) + " " + 
                       getRandomElement(categoryKeywords, random);
                
            default: // Simple combination
                if (!keywords.isEmpty()) {
                    return capitalizeFirst(getRandomElement(keywords, random)) + "Craft";
                }
                return getRandomElement(prefixes, random) + "Craft";
        }
    }

    private String getRandomElement(List<String> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}