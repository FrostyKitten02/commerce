package si.afridau.commerce.productname.model;

import java.time.LocalDateTime;
import java.util.List;

public class GenerateNameRequest {
    private List<String> keywords;
    private String category;
    private int count = 5; // Default number of suggestions
    private LocalDateTime requestTime;

    public GenerateNameRequest() {
        this.requestTime = LocalDateTime.now();
    }

    public GenerateNameRequest(List<String> keywords, String category) {
        this.keywords = keywords;
        this.category = category;
        this.requestTime = LocalDateTime.now();
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }
}