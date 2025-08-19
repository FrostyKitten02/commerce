package si.afridau.commerce.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import si.afridau.commerce.cart.dto.CartDto;
import si.afridau.commerce.cart.model.Cart;

@Mapper(componentModel = "spring", uses = CartProductMapper.class)
public interface CartMapper {
    @Mapping(target = "cartProducts", source = "products")
    CartDto toDto(Cart cart);
}
