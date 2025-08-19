package si.afridau.commerce.healthmonitor.service;

import org.springframework.stereotype.Service;
import si.afridau.commerce.healthmonitor.model.SystemMetrics;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class SystemMetricsService {

    public SystemMetrics getSystemMetrics() {
        SystemMetrics metrics = new SystemMetrics();
        metrics.setLastUpdated(LocalDateTime.now());
        
        try {
            // Get uptime
            long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
            long uptimeSeconds = TimeUnit.MILLISECONDS.toSeconds(uptimeMs);
            long hours = uptimeSeconds / 3600;
            long minutes = (uptimeSeconds % 3600) / 60;
            long seconds = uptimeSeconds % 60;
            metrics.setUptime(String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds));

            // Get memory usage
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
            long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
            double memoryPercentage = (double) usedMemory / maxMemory * 100;
            metrics.setMemoryUsage(String.format("%.1f%% (%d MB / %d MB)", 
                    memoryPercentage, usedMemory / (1024 * 1024), maxMemory / (1024 * 1024)));

            // Get disk usage
            Path currentPath = FileSystems.getDefault().getPath(".");
            FileStore store = Files.getFileStore(currentPath);
            long totalSpace = store.getTotalSpace();
            long usableSpace = store.getUsableSpace();
            long usedSpace = totalSpace - usableSpace;
            double diskPercentage = (double) usedSpace / totalSpace * 100;
            metrics.setDiskUsage(String.format("%.1f%% (%d GB / %d GB)", 
                    diskPercentage, usedSpace / (1024 * 1024 * 1024), totalSpace / (1024 * 1024 * 1024)));

            // Get CPU usage (approximate)
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            double loadAverage = osBean.getSystemLoadAverage();
            if (loadAverage >= 0) {
                metrics.setCpuUsage(String.format("%.1f (Load Average)", loadAverage));
            } else {
                metrics.setCpuUsage("N/A");
            }

        } catch (Exception e) {
            metrics.setUptime("N/A");
            metrics.setMemoryUsage("N/A");
            metrics.setDiskUsage("N/A");
            metrics.setCpuUsage("N/A");
        }

        return metrics;
    }
}