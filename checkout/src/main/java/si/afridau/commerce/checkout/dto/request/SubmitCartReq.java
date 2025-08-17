package si.afridau.commerce.checkout.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubmitCartReq {
    
    @NotNull(message = "Cart ID is required")
    private UUID cartId;
    
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;
    
    @NotBlank(message = "Shipping city is required")
    private String shippingCity;
    
    @NotBlank(message = "Shipping postal code is required")
    private String shippingPostalCode;
    
    @NotBlank(message = "Shipping country is required")
    private String shippingCountry;
}