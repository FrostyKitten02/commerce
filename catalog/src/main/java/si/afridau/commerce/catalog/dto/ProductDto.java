package si.afridau.commerce.catalog.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductDto extends BaseDto {
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private UUID pictureId;
}
