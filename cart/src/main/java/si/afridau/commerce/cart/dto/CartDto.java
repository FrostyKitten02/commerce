package si.afridau.commerce.cart.dto;

import lombok.Getter;
import lombok.Setter;
import si.afridau.commerce.cart.repository.CartProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartDto {
    private List<CartProductDto> cartProducts;
    private BigDecimal total;
}
