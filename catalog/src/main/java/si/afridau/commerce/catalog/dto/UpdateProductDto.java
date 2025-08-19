package si.afridau.commerce.catalog.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductDto {
    private String sku;
    @NotNull(message = "Name is required")
    private String name;
    private String description;
    @Nullable
    @Digits(integer = 6, fraction = 2, message = "Price can have up to 6 digits and 2 decimal places")
    private BigDecimal price;
}
