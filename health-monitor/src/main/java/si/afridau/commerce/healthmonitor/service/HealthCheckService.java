package si.afridau.commerce.healthmonitor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import si.afridau.commerce.healthmonitor.model.HealthReport;
import si.afridau.commerce.healthmonitor.model.ServiceStatus;

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

    private final RestTemplate restTemplate;

    public HealthCheckService() {
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
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            
            status.setStatus("DOWN");
            status.setResponseTime(responseTime);
            status.setErrorMessage(e.getMessage());
            status.setLastChecked(LocalDateTime.now());
        }

        return status;
    }

    public Map<String, String> getServiceUrls() {
        return Map.of(
            "auth", authUrl,
            "catalog", catalogUrl,
            "cart", cartUrl,
            "storage", storageUrl,
            "checkout", checkoutUrl
        );
    }
}