package si.afridau.commerce.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.afridau.commerce.cart.model.CartProduct;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, UUID> {
    Optional<CartProduct> findByCartIdAndProductId(UUID cartId, UUID productId);
}
