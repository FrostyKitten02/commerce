package si.afridau.commerce.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import si.afridau.commerce.catalog.client.storage.ApiClient;
import si.afridau.commerce.catalog.client.storage.api.DefaultApi;

import java.util.List;

@Configuration
public class StorageClientConfig {

    @Value("${storage-ws.base-path}")
    private String storageBasePath;

    @Bean
    public RestTemplate storageRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Add JWT interceptor
        restTemplate.setInterceptors(List.of(jwtInterceptor()));
        
        return restTemplate;
    }

    @Bean
    public ApiClient storageApiClient(RestTemplate storageRestTemplate) {
        ApiClient apiClient = new ApiClient(storageRestTemplate);
        apiClient.setBasePath(storageBasePath);
        return apiClient;
    }

    @Bean
    public DefaultApi storageApi(ApiClient storageApiClient) {
        return new DefaultApi(storageApiClient);
    }

    private ClientHttpRequestInterceptor jwtInterceptor() {
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