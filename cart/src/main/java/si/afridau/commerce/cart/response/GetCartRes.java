package si.afridau.commerce.cart.response;

import lombok.Getter;
import lombok.Setter;
import si.afridau.commerce.cart.dto.CartDto;

@Getter
@Setter
public class GetCartRes {
    private CartDto cart;
}
