package si.afridau.commerce.productname.model;

import java.util.List;

public class CategoryStats {
    private String category;
    private Integer count;
    private Float averageProcessingTime;
    private List<String> popularKeywords;

    public CategoryStats() {}

    public CategoryStats(String category, Integer count, Float averageProcessingTime, List<String> popularKeywords) {
        this.category = category;
        this.count = count;
        this.averageProcessingTime = averageProcessingTime;
        this.popularKeywords = popularKeywords;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getAverageProcessingTime() {
        return averageProcessingTime;
    }

    public void setAverageProcessingTime(Float averageProcessingTime) {
        this.averageProcessingTime = averageProcessingTime;
    }

    public List<String> getPopularKeywords() {
        return popularKeywords;
    }

    public void setPopularKeywords(List<String> popularKeywords) {
        this.popularKeywords = popularKeywords;
    }
}