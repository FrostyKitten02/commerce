package si.afridau.commerce.productname.model;

import java.time.LocalDateTime;
import java.util.List;

public class GenerationStatistics {
    private Integer totalRequests;
    private List<CategoryStats> requestsByCategory;
    private Float averageProcessingTime;
    private List<String> mostPopularKeywords;
    private LocalDateTime lastUpdated;

    public GenerationStatistics() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Integer getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(Integer totalRequests) {
        this.totalRequests = totalRequests;
    }

    public List<CategoryStats> getRequestsByCategory() {
        return requestsByCategory;
    }

    public void setRequestsByCategory(List<CategoryStats> requestsByCategory) {
        this.requestsByCategory = requestsByCategory;
    }

    public Float getAverageProcessingTime() {
        return averageProcessingTime;
    }

    public void setAverageProcessingTime(Float averageProcessingTime) {
        this.averageProcessingTime = averageProcessingTime;
    }

    public List<String> getMostPopularKeywords() {
        return mostPopularKeywords;
    }

    public void setMostPopularKeywords(List<String> mostPopularKeywords) {
        this.mostPopularKeywords = mostPopularKeywords;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}