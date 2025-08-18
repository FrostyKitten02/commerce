import {AuthControllerApi, Configuration as AuthConfig} from "../../client/auth";
import {Configuration as CatalogConfig, ProductControllerApi} from "../../client/catalog";
import {CartControllerApi, Configuration as CartConfig} from "../../client/cart";
import {CheckoutControllerApi, Configuration as CheckoutConfig} from "../../client/checkout";
import {HealthMonitorApi, Configuration as HealthConfig} from "../../client/healthCheck";
import {ProductNameGeneratorApi, Configuration as ProductNameConfig} from "../../client/productNameGenerator";
import {ConfigUtil} from "./ConfigUtil";
import {RawAxiosRequestConfig} from "axios";
import StorageUtil from "./StorageUtil";

export default class RequestUtil {
    private constructor() {}

    public static createBaseAxiosRequestConfig(): RawAxiosRequestConfig {
        const session: string | null = StorageUtil.getSessionToken();

        if (session == null) {
            return {};
        }

        return {
            headers: {
                Authorization: `Bearer ${session}`,
            },
        };
    }

    public static createAuthApi() {
        return new AuthControllerApi(RequestUtil.createAuthConfig());
    }

    public static createProductsApi() {
        return new ProductControllerApi(RequestUtil.createCatalogConfig());
    }

    public static createCartApi() {
        return new CartControllerApi(RequestUtil.createCartConfig());
    }

    public static createCheckoutApi() {
        return new CheckoutControllerApi(RequestUtil.createCheckoutConfig());
    }

    public static createHealthMonitorApi() {
        return new HealthMonitorApi(RequestUtil.createHealthConfig());
    }

    public static createProductNameGeneratorApi() {
        return new ProductNameGeneratorApi(RequestUtil.createProductNameConfig());
    }

    private static createAuthConfig(): AuthConfig {
        const conf = ConfigUtil.getConfig();

        return new AuthConfig({
            basePath: conf.baseUrl.auth
        });
    }

    private static createCatalogConfig(): CatalogConfig {
        const conf = ConfigUtil.getConfig();

        return new CatalogConfig({
            basePath: conf.baseUrl.catalog
        });
    }

    private static createCartConfig(): CartConfig {
        const conf = ConfigUtil.getConfig();

        return new CartConfig({
            basePath: conf.baseUrl.cart
        });
    }

    private static createCheckoutConfig(): CheckoutConfig {
        const conf = ConfigUtil.getConfig();

        return new CheckoutConfig({
            basePath: conf.baseUrl.checkout
        });
    }

    private static createHealthConfig(): HealthConfig {
        const conf = ConfigUtil.getConfig();

        return new HealthConfig({
            basePath: conf.healthMonitorUrl
        });
    }

    private static createProductNameConfig(): ProductNameConfig {
        const conf = ConfigUtil.getConfig();

        return new ProductNameConfig({
            basePath: conf.productNameGeneratorUrl
        });
    }

}