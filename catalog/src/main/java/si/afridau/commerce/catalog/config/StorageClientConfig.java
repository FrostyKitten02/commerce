package si.afridau.commerce.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import si.afridau.commerce.catalog.client.storage.ApiClient;
import si.afridau.commerce.catalog.client.storage.api.DefaultApi;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class StorageClientConfig {

    @Value("${storage-ws.base-path}")
    private String storageBasePath;

    @Bean
    public DefaultApi storageApi() {
        // Create RestTemplate with JWT interceptor
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new JwtTokenInterceptor()));
        
        // Create and configure ApiClient
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(storageBasePath);
        
        // Return configured API instance
        return new DefaultApi(apiClient);
    }

    /**
     * Interceptor that automatically adds JWT token from current HTTP request Authorization header
     */
    private static class JwtTokenInterceptor implements ClientHttpRequestInterceptor {
        
        @Override
        public ClientHttpResponse intercept(
                HttpRequest request, 
                byte[] body, 
                ClientHttpRequestExecution execution) throws IOException {
            
            // Get JWT token from current HTTP request Authorization header
            String jwtToken = getCurrentJwtToken();
            
            // Add Authorization header if token is available
            if (jwtToken != null && !jwtToken.trim().isEmpty()) {
                request.getHeaders().add("Authorization", "Bearer " + jwtToken);
            }
            
            // Continue with the request
            return execution.execute(request, body);
        }
        
        /**
         * Extract JWT token from current HTTP request Authorization header
         */
        private String getCurrentJwtToken() {
            try {
                ServletRequestAttributes requestAttributes = 
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpServletRequest httpRequest = requestAttributes.getRequest();
                
                String authHeader = httpRequest.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    return authHeader.substring(7);
                }
            } catch (IllegalStateException e) {
                // No request context available (e.g., not in HTTP request thread)
            }
            
            return null;
        }
    }
}