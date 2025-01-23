package si.afridau.commerce.catalog.response;

import lombok.Getter;
import lombok.Setter;
import si.afridau.commerce.catalog.dto.ProductDto;

import java.util.List;

@Getter
@Setter
public class GetProductRes {
    private ProductDto product;
    private List<ProductDto> products;
}
