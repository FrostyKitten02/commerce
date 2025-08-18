package si.afridau.commerce.productname.model;

import java.util.List;

public class GenerationResponse {
    private List<NameSuggestion> suggestions;
    private Integer totalGenerated;
    private Integer processingTime;
    private String category;
    private GenerateNameRequest originalRequest;

    public GenerationResponse() {}

    public GenerationResponse(List<NameSuggestion> suggestions, Integer totalGenerated, Integer processingTime, String category, GenerateNameRequest originalRequest) {
        this.suggestions = suggestions;
        this.totalGenerated = totalGenerated;
        this.processingTime = processingTime;
        this.category = category;
        this.originalRequest = originalRequest;
    }

    public List<NameSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<NameSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public Integer getTotalGenerated() {
        return totalGenerated;
    }

    public void setTotalGenerated(Integer totalGenerated) {
        this.totalGenerated = totalGenerated;
    }

    public Integer getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Integer processingTime) {
        this.processingTime = processingTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public GenerateNameRequest getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(GenerateNameRequest originalRequest) {
        this.originalRequest = originalRequest;
    }
}