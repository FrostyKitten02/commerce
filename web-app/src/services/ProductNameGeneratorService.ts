import axios, { AxiosResponse } from 'axios';
import { ConfigUtil } from '../util/ConfigUtil';

export interface GenerateNameRequest {
    keywords: string[];
    category: string;
    count?: number;
}

export interface GenerateNameResponse {
    suggestions: string[];
    category: string;
    usedKeywords: string[];
}

export class ProductNameGeneratorService {
    private static getBaseUrl(): string {
        const config = ConfigUtil.getConfig();
        return config.productNameGeneratorUrl || 'http://localhost:8087';
    }

    static async generateNames(request: GenerateNameRequest): Promise<GenerateNameResponse> {
        const baseUrl = this.getBaseUrl();
        const response: AxiosResponse<GenerateNameResponse> = await axios.post(
            `${baseUrl}/api/generate-name`,
            request,
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        );
        return response.data;
    }

    static async generateNamesSimple(keywords: string, category: string, count: number = 5): Promise<GenerateNameResponse> {
        const baseUrl = this.getBaseUrl();
        const params = new URLSearchParams({
            keywords,
            category,
            count: count.toString()
        });
        
        const response: AxiosResponse<GenerateNameResponse> = await axios.get(
            `${baseUrl}/api/generate-name/simple?${params}`
        );
        return response.data;
    }

    static async getAvailableCategories(): Promise<string[]> {
        const baseUrl = this.getBaseUrl();
        const response: AxiosResponse<string[]> = await axios.get(`${baseUrl}/api/categories`);
        return response.data;
    }
}