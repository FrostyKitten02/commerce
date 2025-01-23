package si.afridau.commerce.cart.catalogclient;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetProductResDto {
    private CatalogProductDto product;
    private List<CatalogProductDto> products;
}
