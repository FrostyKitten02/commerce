package si.afridau.commerce.catalog.response;

import lombok.Getter;
import lombok.Setter;
import si.afridau.commerce.catalog.dto.ProductDto;

@Getter
@Setter
public class GetProductRes {
    private ProductDto product;
}
