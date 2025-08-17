package si.afridau.commerce.checkout.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDto extends BaseDto {
    private UUID userId;
    private UUID cartId;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;
    private BigDecimal totalAmount;
    private List<OrderItemDto> orderItems;
}