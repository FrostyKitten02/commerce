package si.afridau.commerce.cart.mapper;

import org.mapstruct.Mapper;
import si.afridau.commerce.cart.dto.CartProductDto;
import si.afridau.commerce.cart.model.CartProduct;

@Mapper(componentModel = "spring")
public interface CartProductMapper {
    CartProductDto toDto(CartProduct cartProduct);
}
