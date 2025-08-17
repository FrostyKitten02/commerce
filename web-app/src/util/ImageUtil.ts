import { ConfigUtil } from './ConfigUtil';

export class ImageUtil {
    private constructor() {}

    public static getImageUrl(imageId: string | undefined): string | null {
        if (!imageId) {
            return null;
        }

        const config = ConfigUtil.getConfig();
        return `${config.baseUrl.storage}/files/${imageId}`;
    }

    public static getImageUrlWithAuth(imageId: string | undefined, token: string): string | null {
        const baseUrl = this.getImageUrl(imageId);
        if (!baseUrl || !token) {
            return baseUrl;
        }

        // For images that might require authentication, we'll need to handle this differently
        // Since we can't add Authorization headers to img src attributes
        return baseUrl;
    }
}