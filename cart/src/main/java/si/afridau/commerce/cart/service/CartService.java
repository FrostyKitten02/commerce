package si.afridau.commerce.cart.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import si.afridau.commerce.auth.model.JwtUser;
import si.afridau.commerce.cart.client.catalog.model.GetProductRes;
import si.afridau.commerce.cart.client.catalog.model.ProductDto;
import si.afridau.commerce.cart.dto.CartDto;
import si.afridau.commerce.cart.dto.CartProductDto;
import si.afridau.commerce.cart.mapper.CartMapper;
import si.afridau.commerce.cart.model.Cart;
import si.afridau.commerce.cart.model.CartProduct;
import si.afridau.commerce.cart.repository.CartProductRepository;
import si.afridau.commerce.cart.repository.CartRepository;
import si.afridau.commerce.cart.request.AddToCartReq;
import si.afridau.commerce.exception.implementation.IllegalResourceAccess;
import si.afridau.commerce.exception.implementation.InternalServerException;
import si.afridau.commerce.exception.implementation.ItemNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final CartMapper cartMapper;
    private final CatalogClientService catalogClientService;
    public CartDto getCart(@NotNull JwtUser caller) throws IOException {
        UUID userId = caller.getId();
        if (userId == null) {
            throw new InternalServerException("User id is null");
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        //if no cart found user doesn't have current cart and we just create one
        if (cart.getId() == null) {
            cart.setUserId(userId);
            cart = cartRepository.save(cart);
        }
        CartDto cartDto = cartMapper.toDto(cart);

        if (cartDto.getCartProducts() == null) {
            return cartDto;
        }

        BigDecimal total = BigDecimal.ZERO;
        GetProductRes res = catalogClientService.getProducts(cartDto.getCartProducts().stream().map(CartProductDto::getProductId).toList());
        if (res == null) {
            return cartDto;
        }

        List<ProductDto> products = res.getProducts();
        List<CartProductDto> validCartProducts = new ArrayList<>();
        List<UUID> productsToRemove = new ArrayList<>();
        
        for (CartProductDto cp : cartDto.getCartProducts()) {
            Optional<ProductDto> prod = products.stream().filter(p -> p.getId().equals(cp.getProductId())).findFirst();
            if (prod.isEmpty()) {
                // Product not found in catalog - mark for removal
                productsToRemove.add(cp.getId());
            } else {
                cp.setPictureId(prod.get().getPictureId());
                cp.setSku(prod.get().getSku());
                cp.setName(prod.get().getName());
                // Product exists - calculate totals and keep it
                BigDecimal lineTotal = cp.getQuantity().multiply(prod.get().getPrice());
                cp.setLineTotal(lineTotal);
                cp.setPrice(prod.get().getPrice());
                total = total.add(lineTotal);
                validCartProducts.add(cp);
            }
        }
        
        // Remove invalid products from database
        if (!productsToRemove.isEmpty()) {
            cartProductRepository.deleteAllById(productsToRemove);
        }
        
        // Update cart DTO with only valid products
        cartDto.setCartProducts(validCartProducts);

        cartDto.setTotal(total);
        return cartDto;
    }

    public void deleteCart(@NotNull JwtUser caller) {
        UUID userId = caller.getId();
        if (userId == null) {
            throw new InternalServerException("User id is null");
        }
        cartRepository
                .findByUserId(userId)
                .ifPresentOrElse(cartRepository::delete, () -> {throw new ItemNotFoundException("Cart not found");});
    }

    public void addToCart(JwtUser caller, UUID cartId, @Valid @NotNull AddToCartReq req) {
        UUID userId = caller.getId();
        if (userId == null) {
            throw new InternalServerException("User id is null");
        }

        Cart cart = cartRepository.findByUserIdAndCartId(userId, cartId)
                .orElseThrow(() -> new ItemNotFoundException("Cart not found"));

        cartProductRepository
                .findByCartIdAndProductId(cart.getId(), req.getProductId()).ifPresentOrElse(
                cp -> {
                    cp.setQuantity(cp.getQuantity().add(req.getQuantity()));
                    cartProductRepository.save(cp);
                },
                () -> {
                    CartProduct cp = new CartProduct();
                    cp.setProductId(req.getProductId());
                    cp.setCart(cart);
                    cp.setQuantity(req.getQuantity());
                    cartProductRepository.save(cp);
                });
    }

    public void updateQuantity(JwtUser caller, @NotNull UUID cartProductId, @NotNull BigDecimal quantity) {
        CartProduct cp = getCartProductForUser(caller, cartProductId);

        if (quantity.equals(BigDecimal.ZERO)) {
            cartProductRepository.delete(cp);
            return;
        }

        cp.setQuantity(quantity);
        cartProductRepository.save(cp);
    }

    public void removeFromCart(JwtUser caller,  @NotNull UUID cartProductId) {
        CartProduct cp = getCartProductForUser(caller, cartProductId);
        cartProductRepository.delete(cp);
    }

    private CartProduct getCartProductForUser(JwtUser caller, UUID cartProductId) {
        UUID userId = caller.getId();
        if (userId == null) {
            throw new InternalServerException("User id is null");
        }

        CartProduct cp = cartProductRepository.findById(cartProductId).orElseThrow(() -> new ItemNotFoundException("Cart product not found"));
        if (!cp.getCart().getUserId().equals(userId)) {
            throw new IllegalResourceAccess("Cart product does not belong to user");
        }

        return cp;
    }

}
