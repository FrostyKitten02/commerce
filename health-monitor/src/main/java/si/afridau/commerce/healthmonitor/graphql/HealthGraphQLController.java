package si.afridau.commerce.healthmonitor.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import si.afridau.commerce.healthmonitor.model.HealthReport;
import si.afridau.commerce.healthmonitor.model.ServiceStatus;
import si.afridau.commerce.healthmonitor.model.SystemMetrics;
import si.afridau.commerce.healthmonitor.service.HealthCheckService;
import si.afridau.commerce.healthmonitor.service.SystemMetricsService;

@Controller
public class HealthGraphQLController {

    @Autowired
    private HealthCheckService healthCheckService;

    @Autowired
    private SystemMetricsService systemMetricsService;

    @QueryMapping
    public HealthReport healthReport() {
        return healthCheckService.checkAllServices();
    }

    @QueryMapping
    public ServiceStatus serviceStatus(@Argument String serviceName) {
        HealthReport report = healthCheckService.checkAllServices();
        return report.getServices().stream()
                .filter(service -> service.getServiceName().equalsIgnoreCase(serviceName))
                .findFirst()
                .orElse(null);
    }

    @QueryMapping
    public SystemMetrics systemMetrics() {
        return systemMetricsService.getSystemMetrics();
    }
}