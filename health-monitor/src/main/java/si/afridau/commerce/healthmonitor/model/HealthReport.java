package si.afridau.commerce.healthmonitor.model;

import java.time.LocalDateTime;
import java.util.List;

public class HealthReport {
    private String overallStatus;
    private int totalServices;
    private int servicesUp;
    private int servicesDown;
    private LocalDateTime reportTime;
    private List<ServiceStatus> services;
    private SystemMetrics systemMetrics;

    public HealthReport() {
        this.reportTime = LocalDateTime.now();
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public int getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(int totalServices) {
        this.totalServices = totalServices;
    }

    public int getServicesUp() {
        return servicesUp;
    }

    public void setServicesUp(int servicesUp) {
        this.servicesUp = servicesUp;
    }

    public int getServicesDown() {
        return servicesDown;
    }

    public void setServicesDown(int servicesDown) {
        this.servicesDown = servicesDown;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public List<ServiceStatus> getServices() {
        return services;
    }

    public void setServices(List<ServiceStatus> services) {
        this.services = services;
    }

    public SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public void setSystemMetrics(SystemMetrics systemMetrics) {
        this.systemMetrics = systemMetrics;
    }
}