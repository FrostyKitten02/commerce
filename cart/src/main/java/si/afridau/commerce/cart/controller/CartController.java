package si.afridau.commerce.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.afridau.commerce.auth.model.JwtUser;
import si.afridau.commerce.cart.dto.CartDto;
import si.afridau.commerce.cart.request.AddToCartReq;
import si.afridau.commerce.cart.response.GetCartRes;
import si.afridau.commerce.cart.service.CartService;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

@Validated
@CrossOrigin
@RestController
@RequestMapping("carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public GetCartRes getCart(@AuthenticationPrincipal JwtUser caller) throws IOException {
        CartDto cart = cartService.getCart(caller);
        GetCartRes res = new GetCartRes();
        res.setCart(cart);
        return res;
    }

    @DeleteMapping
    public void deleteCart(@AuthenticationPrincipal JwtUser caller) {
        cartService.deleteCart(caller);
    }

    @PostMapping("{cartId}")
    public void addToCart(@AuthenticationPrincipal JwtUser caller, @Valid @NotNull @RequestBody AddToCartReq body, @PathVariable UUID cartId) {
        cartService.addToCart(caller, cartId, body);
    }

    @PatchMapping("product/{cartProductId}/{quantity}")
    public void updateQuantity(
            @AuthenticationPrincipal JwtUser caller,
            @Valid @PathVariable @NotNull UUID cartProductId,
            @Valid @PathVariable @NotNull @Min(0)
            @Digits(integer = 8, fraction = 4, message = "Quantity ca have up to 8 digits and 4 decimals.")
            BigDecimal quantity
    ) {
        cartService.updateQuantity(caller, cartProductId, quantity);
    }

    @DeleteMapping("{cartId}")
    public void removeFromCart(@AuthenticationPrincipal JwtUser caller, @PathVariable UUID cartId) {
        cartService.removeFromCart(caller, cartId);
    }


}
