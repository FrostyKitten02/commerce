package si.afridau.commerce.productname.model;

import java.util.List;

public class GenerateNameResponse {
    private List<String> suggestions;
    private String category;
    private List<String> usedKeywords;

    public GenerateNameResponse() {}

    public GenerateNameResponse(List<String> suggestions, String category, List<String> usedKeywords) {
        this.suggestions = suggestions;
        this.category = category;
        this.usedKeywords = usedKeywords;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getUsedKeywords() {
        return usedKeywords;
    }

    public void setUsedKeywords(List<String> usedKeywords) {
        this.usedKeywords = usedKeywords;
    }
}