import axios, { AxiosResponse } from 'axios';
import { ConfigUtil } from '../util/ConfigUtil';

export interface ServiceStatus {
    serviceName: string;
    status: string;
    url: string;
    responseTime: number;
    lastChecked: string;
    errorMessage?: string;
}

export interface HealthReport {
    overallStatus: string;
    totalServices: number;
    servicesUp: number;
    servicesDown: number;
    reportTime: string;
    services: ServiceStatus[];
}

export class HealthMonitorService {
    private static getBaseUrl(): string {
        const config = ConfigUtil.getConfig();
        return config.healthMonitorUrl || 'http://localhost:8086';
    }

    static async getHealthStatus(): Promise<HealthReport> {
        const baseUrl = this.getBaseUrl();
        const response: AxiosResponse<HealthReport> = await axios.get(`${baseUrl}/api/health/status`);
        return response.data;
    }

    static async getServiceStatus(serviceName: string): Promise<ServiceStatus> {
        const baseUrl = this.getBaseUrl();
        const response: AxiosResponse<ServiceStatus> = await axios.get(`${baseUrl}/api/health/service/${serviceName}`);
        return response.data;
    }

    static async getAvailableServices(): Promise<Record<string, string>> {
        const baseUrl = this.getBaseUrl();
        const response: AxiosResponse<Record<string, string>> = await axios.get(`${baseUrl}/api/health/services`);
        return response.data;
    }
}