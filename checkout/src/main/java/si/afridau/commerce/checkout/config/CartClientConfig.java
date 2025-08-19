package si.afridau.commerce.checkout.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import si.afridau.commerce.checkout.client.cart.ApiClient;
import si.afridau.commerce.checkout.client.cart.api.CartControllerApi;

import java.util.List;

@Configuration
public class CartClientConfig {

    @Value("${cart-ws.base-path}")
    private String cartBaseUrl;

    @Bean
    public RestTemplate cartRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        restTemplate.setInterceptors(List.of(jwtInterceptor()));
        
        return restTemplate;
    }

    @Bean
    public ApiClient cartApiClient(RestTemplate cartRestTemplate) {
        ApiClient apiClient = new ApiClient(cartRestTemplate);
        apiClient.setBasePath(cartBaseUrl);
        return apiClient;
    }

    @Bean
    public CartControllerApi cartControllerApi(ApiClient cartApiClient) {
        return new CartControllerApi(cartApiClient);
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