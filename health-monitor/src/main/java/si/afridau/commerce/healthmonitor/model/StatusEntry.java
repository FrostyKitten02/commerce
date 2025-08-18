package si.afridau.commerce.healthmonitor.model;

import java.time.LocalDateTime;

public class StatusEntry {
    private LocalDateTime timestamp;
    private String status;
    private long responseTime;
    private String errorMessage;

    public StatusEntry() {}

    public StatusEntry(LocalDateTime timestamp, String status, long responseTime, String errorMessage) {
        this.timestamp = timestamp;
        this.status = status;
        this.responseTime = responseTime;
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}