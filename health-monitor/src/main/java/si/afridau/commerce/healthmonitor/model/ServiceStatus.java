package si.afridau.commerce.healthmonitor.model;

import java.time.LocalDateTime;

public class ServiceStatus {
    private String serviceName;
    private String status; // UP, DOWN, UNKNOWN
    private String url;
    private long responseTime;
    private LocalDateTime lastChecked;
    private String errorMessage;

    public ServiceStatus() {}

    public ServiceStatus(String serviceName, String url) {
        this.serviceName = serviceName;
        this.url = url;
        this.lastChecked = LocalDateTime.now();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}