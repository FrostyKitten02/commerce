package si.afridau.commerce.catalog.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import si.afridau.commerce.catalog.dto.UpdateProductDto;

@Getter
@Setter
public class UpdateProductReq {
    @Valid
    @NotNull
    private UpdateProductDto product;
}
