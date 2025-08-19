package si.afridau.commerce.checkout.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import si.afridau.commerce.checkout.client.catalog.ApiClient;
import si.afridau.commerce.checkout.client.catalog.api.ProductControllerApi;

import java.util.List;

@Configuration
public class CatalogClientConfig {

    @Value("${catalog-ws.base-path}")
    private String catalogBaseUrl;

    @Bean
    public RestTemplate catalogRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        restTemplate.setInterceptors(List.of(catalogJwtInterceptor()));
        
        return restTemplate;
    }

    @Bean
    public ApiClient catalogApiClient(RestTemplate catalogRestTemplate) {
        ApiClient apiClient = new ApiClient(catalogRestTemplate);
        apiClient.setBasePath(catalogBaseUrl);
        return apiClient;
    }

    @Bean
    public ProductControllerApi productControllerApi(ApiClient catalogApiClient) {
        return new ProductControllerApi(catalogApiClient);
    }

    private ClientHttpRequestInterceptor catalogJwtInterceptor() {
        return (request, body, execution) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getCredentials() != null) {
                String token = authentication.getCredentials().toString();
                request.getHeaders().setBearerAuth(token);
            }
            return execution.execute(request, body);
        };
    }
}