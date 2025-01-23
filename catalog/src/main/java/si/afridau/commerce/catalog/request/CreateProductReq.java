package si.afridau.commerce.catalog.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import si.afridau.commerce.catalog.dto.CreateProductDto;

@Getter
@Setter
public class CreateProductReq {
    @Valid
    @NotNull(message = "Product is required")
    private CreateProductDto product;
}
