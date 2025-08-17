package si.afridau.commerce.checkout.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderItemDto extends BaseDto {
    
    private UUID orderId;
    private UUID productId;
    private String productName;
    private String productSku;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}