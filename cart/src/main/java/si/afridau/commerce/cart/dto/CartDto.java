package si.afridau.commerce.cart.dto;

import lombok.Getter;
import lombok.Setter;
import si.afridau.commerce.cart.repository.CartProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartDto {
    private UUID id;
    private List<CartProductDto> cartProducts;
    private BigDecimal total;
}
