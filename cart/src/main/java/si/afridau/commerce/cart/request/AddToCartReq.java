package si.afridau.commerce.cart.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class AddToCartReq {
    @NotNull
    private UUID productId;
    @NotNull
    @Min(0)
    @Digits(integer = 8, fraction = 4, message = "Quantity ca have up to 8 digits and 4 decimals.")
    private BigDecimal quantity;
}
