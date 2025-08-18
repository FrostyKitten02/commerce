package si.afridau.commerce.healthmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.afridau.commerce.healthmonitor.model.HealthReport;
import si.afridau.commerce.healthmonitor.model.ServiceStatus;
import si.afridau.commerce.healthmonitor.service.HealthCheckService;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping("/status")
    public ResponseEntity<HealthReport> getHealthStatus() {
        HealthReport report = healthCheckService.checkAllServices();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/service/{serviceName}")
    public ResponseEntity<ServiceStatus> checkSpecificService(@PathVariable String serviceName) {
        Map<String, String> serviceUrls = healthCheckService.getServiceUrls();
        String serviceUrl = serviceUrls.get(serviceName.toLowerCase());
        
        if (serviceUrl == null) {
            return ResponseEntity.notFound().build();
        }

        ServiceStatus status = healthCheckService.checkService(serviceName, serviceUrl + "/actuator/health");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/services")
    public ResponseEntity<Map<String, String>> getAvailableServices() {
        return ResponseEntity.ok(healthCheckService.getServiceUrls());
    }
}