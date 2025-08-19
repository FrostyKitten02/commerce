package si.afridau.commerce.healthmonitor.model;

import java.time.LocalDateTime;

public class SystemMetrics {
    private String uptime;
    private String memoryUsage;
    private String diskUsage;
    private String cpuUsage;
    private LocalDateTime lastUpdated;

    public SystemMetrics() {}

    public SystemMetrics(String uptime, String memoryUsage, String diskUsage, String cpuUsage, LocalDateTime lastUpdated) {
        this.uptime = uptime;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
        this.cpuUsage = cpuUsage;
        this.lastUpdated = lastUpdated;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}