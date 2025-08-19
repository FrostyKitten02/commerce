package si.afridau.commerce.healthmonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.afridau.commerce.healthmonitor.model.HealthReport;
import si.afridau.commerce.healthmonitor.model.ServiceStatus;
import si.afridau.commerce.healthmonitor.model.StatusEntry;
import si.afridau.commerce.healthmonitor.model.SystemMetrics;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HealthCheckService {

    @Value("${services.auth.url}")
    private String authUrl;

    @Value("${services.catalog.url}")
    private String catalogUrl;

    @Value("${services.cart.url}")
    private String cartUrl;

    @Value("${services.storage.url}")
    private String storageUrl;

    @Value("${services.checkout.url}")
    private String checkoutUrl;

    @Value("${services.product-name-generator.url}")
    private String productNameGeneratorUrl;

    private final RestTemplate restTemplate;
    private final SystemMetricsService systemMetricsService;

    public HealthCheckService(SystemMetricsService systemMetricsService) {
        this.systemMetricsService = systemMetricsService;
        this.restTemplate = new RestTemplate();
        this.restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "HealthMonitor/1.0");
            return execution.execute(request, body);
        });
    }

    public HealthReport checkAllServices() {
        HealthReport report = new HealthReport();
        List<ServiceStatus> services = new ArrayList<>();

        services.add(checkService("Auth Service", authUrl + "/actuator/health"));
        services.add(checkService("Catalog Service", catalogUrl + "/actuator/health"));
        services.add(checkService("Cart Service", cartUrl + "/actuator/health"));
        services.add(checkService("Storage Service", storageUrl + "/health"));
        services.add(checkService("Checkout Service", checkoutUrl + "/actuator/health"));
        services.add(checkService("Product Name Generator", productNameGeneratorUrl + "/actuator/health"));

        report.setServices(services);
        report.setTotalServices(services.size());

        long upCount = services.stream().filter(s -> "UP".equals(s.getStatus())).count();
        report.setServicesUp((int) upCount);
        report.setServicesDown(services.size() - (int) upCount);

        if (upCount == services.size()) {
            report.setOverallStatus("ALL_UP");
        } else if (upCount == 0) {
            report.setOverallStatus("ALL_DOWN");
        } else {
            report.setOverallStatus("PARTIAL");
        }

        // Add system metrics to the report
        report.setSystemMetrics(systemMetricsService.getSystemMetrics());

        return report;
    }

    public ServiceStatus checkService(String serviceName, String healthUrl) {
        ServiceStatus status = new ServiceStatus(serviceName, healthUrl);
        long startTime = System.currentTimeMillis();

        try {
            restTemplate.getForEntity(healthUrl, String.class);
            long responseTime = System.currentTimeMillis() - startTime;
            
            status.setStatus("UP");
            status.setResponseTime(responseTime);
            status.setLastChecked(LocalDateTime.now());
            
            // Add to history
            status.addStatusEntry(new StatusEntry(LocalDateTime.now(), "UP", responseTime, null));
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            
            status.setStatus("DOWN");
            status.setResponseTime(responseTime);
            status.setErrorMessage(e.getMessage());
            status.setLastChecked(LocalDateTime.now());
            
            // Add to history
            status.addStatusEntry(new StatusEntry(LocalDateTime.now(), "DOWN", responseTime, e.getMessage()));
        }

        return status;
    }

    public Map<String, String> getServiceUrls() {
        return Map.of(
            "auth", authUrl,
            "catalog", catalogUrl,
            "cart", cartUrl,
            "storage", storageUrl,
            "checkout", checkoutUrl,
            "product-name-generator", productNameGeneratorUrl
        );
    }
}