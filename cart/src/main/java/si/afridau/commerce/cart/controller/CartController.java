package si.afridau.commerce.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RestController
@RequestMapping("carts")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Shopping cart management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class CartController {
    private final CartService cartService;

    @Operation(
        summary = "Get user's cart",
        description = "Retrieve the current authenticated user's shopping cart with all items and total price"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Cart retrieved successfully",
        content = @Content(schema = @Schema(implementation = GetCartRes.class))
    )
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @GetMapping
    public GetCartRes getCart(@AuthenticationPrincipal JwtUser caller) throws IOException {
        CartDto cart = cartService.getCart(caller);
        GetCartRes res = new GetCartRes();
        res.setCart(cart);
        return res;
    }

    @Operation(
        summary = "Clear user's cart",
        description = "Delete all items from the current authenticated user's shopping cart"
    )
    @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @DeleteMapping
    public void deleteCart(@AuthenticationPrincipal JwtUser caller) {
        cartService.deleteCart(caller);
    }

    @Operation(
        summary = "Add item to cart",
        description = "Add a product to the user's shopping cart with specified quantity"
    )
    @ApiResponse(responseCode = "200", description = "Item added to cart successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PostMapping("{cartId}")
    public void addToCart(@AuthenticationPrincipal JwtUser caller, @Valid @NotNull @RequestBody AddToCartReq body, @PathVariable UUID cartId) {
        cartService.addToCart(caller, cartId, body);
    }

    @Operation(
        summary = "Update item quantity",
        description = "Update the quantity of a specific item in the user's shopping cart"
    )
    @ApiResponse(responseCode = "200", description = "Quantity updated successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
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

    @Operation(
        summary = "Remove item from cart",
        description = "Remove a specific item completely from the user's shopping cart"
    )
    @ApiResponse(responseCode = "200", description = "Item removed successfully")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @ApiResponse(responseCode = "404", description = "Cart item not found")
    @DeleteMapping("{cartId}")
    public void removeFromCart(@AuthenticationPrincipal JwtUser caller, @PathVariable UUID cartId) {
        cartService.removeFromCart(caller, cartId);
    }


}
