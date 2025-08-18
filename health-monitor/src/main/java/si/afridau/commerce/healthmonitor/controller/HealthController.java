package si.afridau.commerce.healthmonitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Health Monitor", description = "Health monitoring endpoints for microservices")
public class HealthController {

    @Autowired
    private HealthCheckService healthCheckService;

    @Operation(
        summary = "Get overall health status",
        description = "Returns comprehensive health status report for all monitored microservices including response times, status counts, and system metrics"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Health report retrieved successfully",
        content = @Content(schema = @Schema(implementation = HealthReport.class))
    )
    @GetMapping("/status")
    public ResponseEntity<HealthReport> getHealthStatus() {
        HealthReport report = healthCheckService.checkAllServices();
        return ResponseEntity.ok(report);
    }

    @Operation(
        summary = "Check specific service health",
        description = "Returns detailed health status for a specific service including response time, last check time, and error details if any"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service health status retrieved successfully",
        content = @Content(schema = @Schema(implementation = ServiceStatus.class))
    )
    @ApiResponse(responseCode = "404", description = "Service not found")
    @GetMapping("/service/{serviceName}")
    public ResponseEntity<ServiceStatus> checkSpecificService(
        @Parameter(description = "Name of the service to check", example = "auth")
        @PathVariable String serviceName) {
        Map<String, String> serviceUrls = healthCheckService.getServiceUrls();
        String serviceUrl = serviceUrls.get(serviceName.toLowerCase());
        
        if (serviceUrl == null) {
            return ResponseEntity.notFound().build();
        }

        ServiceStatus status = healthCheckService.checkService(serviceName, serviceUrl + "/actuator/health");
        return ResponseEntity.ok(status);
    }

    @Operation(
        summary = "Get available services",
        description = "Returns map of all services being monitored with their service names and base URLs"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Available services retrieved successfully"
    )
    @GetMapping("/services")
    public ResponseEntity<Map<String, String>> getAvailableServices() {
        return ResponseEntity.ok(healthCheckService.getServiceUrls());
    }
}