import {StringUtil} from "./StringUtil";

export interface Config {
    baseUrl: {
        auth: string,
        catalog: string,
        cart: string,
        storage: string,
        checkout: string
    }
}

interface NullableConfig {
    baseUrl?: {
        auth?: string,
        catalog?: string,
        cart?: string,
        storage?: string,
        checkout?: string
    }
}

export class ConfigUtil {
    private constructor() {}

    private static config: Config | null = null;

    public static getConfig(): Config {
        if (this.config == null) {
            throw new Error("Missing config")
        }

        return this.config;
    }

    public static readConfig(): Config {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', "/conf.json", false);  // `false` makes the request synchronous
        xhr.send(null);

        if (xhr.status != 200) {
            throw new Error('Failed to fetch JSON. Status: ' + xhr.status);
        }

        const config = JSON.parse(xhr.responseText) as NullableConfig;
        const validatedConf = this.validateConfig(config);
        this.config = validatedConf;
        return validatedConf;
    }

    private static validateConfig(conf: NullableConfig): Config {
        if (conf.baseUrl == null) {
            throw new Error("Missing backend urls")
        }

        if (
            StringUtil.emptyOrNull(conf.baseUrl.auth) &&
            StringUtil.emptyOrNull(conf.baseUrl.cart) &&
            StringUtil.emptyOrNull(conf.baseUrl.catalog) &&
            StringUtil.emptyOrNull(conf.baseUrl.storage) &&
            StringUtil.emptyOrNull(conf.baseUrl.checkout)
        ) {
            throw new Error("Missing base at least one base url")
        }

        return conf as unknown as Config;
    }
}
