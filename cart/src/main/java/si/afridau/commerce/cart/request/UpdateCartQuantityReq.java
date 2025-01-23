package si.afridau.commerce.cart.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class UpdateCartQuantityReq {
    private UUID cartProductId;
    @Min(0)
    @Digits(integer = 8, fraction = 4, message = "Quantity ca have up to 8 digits and 4 decimals.")
    private BigDecimal quantity;
}
