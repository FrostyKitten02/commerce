package si.afridau.commerce.cart.catalogclient;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CatalogProductDto {
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
}
