package si.afridau.commerce.productname.model;

import java.time.LocalDateTime;
import java.util.List;

public class NameSuggestion {
    private String name;
    private String category;
    private Float confidence;
    private List<String> keywords;
    private LocalDateTime generatedAt;
    private String pattern;

    public NameSuggestion() {
        this.generatedAt = LocalDateTime.now();
    }

    public NameSuggestion(String name, String category, Float confidence, List<String> keywords, String pattern) {
        this.name = name;
        this.category = category;
        this.confidence = confidence;
        this.keywords = keywords;
        this.generatedAt = LocalDateTime.now();
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}