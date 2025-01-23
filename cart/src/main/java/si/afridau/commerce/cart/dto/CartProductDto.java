package si.afridau.commerce.cart.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CartProductDto {
    private UUID id;
    private UUID productId;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal lineTotal;
}
