package si.afridau.commerce.productname.service;

import org.springframework.stereotype.Service;
import si.afridau.commerce.productname.model.CategoryStats;
import si.afridau.commerce.productname.model.GenerationStatistics;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final Map<String, AtomicInteger> categoryRequests = new ConcurrentHashMap<>();
    private final Map<String, List<Integer>> categoryProcessingTimes = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> keywordCounts = new ConcurrentHashMap<>();

    public void recordRequest(String category, List<String> keywords, int processingTime) {
        totalRequests.incrementAndGet();
        
        // Record category stats
        categoryRequests.computeIfAbsent(category.toLowerCase(), k -> new AtomicInteger(0)).incrementAndGet();
        categoryProcessingTimes.computeIfAbsent(category.toLowerCase(), k -> new ArrayList<>()).add(processingTime);
        
        // Record keyword stats
        if (keywords != null) {
            keywords.forEach(keyword -> 
                keywordCounts.computeIfAbsent(keyword.toLowerCase(), k -> new AtomicInteger(0)).incrementAndGet()
            );
        }
    }

    public GenerationStatistics getStatistics() {
        GenerationStatistics stats = new GenerationStatistics();
        stats.setTotalRequests(totalRequests.get());
        stats.setLastUpdated(LocalDateTime.now());

        // Calculate category statistics
        List<CategoryStats> categoryStats = categoryRequests.entrySet().stream()
                .map(entry -> {
                    String category = entry.getKey();
                    int count = entry.getValue().get();
                    
                    List<Integer> processingTimes = categoryProcessingTimes.get(category);
                    float avgProcessingTime = processingTimes != null ? 
                            (float) processingTimes.stream().mapToInt(Integer::intValue).average().orElse(0.0) : 0.0f;
                    
                    // Get popular keywords for this category (simplified)
                    List<String> popularKeywords = keywordCounts.entrySet().stream()
                            .sorted(Map.Entry.<String, AtomicInteger>comparingByValue(
                                    (a, b) -> b.get() - a.get()))
                            .limit(3)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());
                    
                    return new CategoryStats(category, count, avgProcessingTime, popularKeywords);
                })
                .collect(Collectors.toList());
        
        stats.setRequestsByCategory(categoryStats);

        // Calculate overall average processing time
        float overallAvg = categoryProcessingTimes.values().stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        stats.setAverageProcessingTime((float) overallAvg);

        // Get most popular keywords overall
        List<String> popularKeywords = keywordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, AtomicInteger>comparingByValue(
                        (a, b) -> b.get() - a.get()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        stats.setMostPopularKeywords(popularKeywords);

        return stats;
    }
}