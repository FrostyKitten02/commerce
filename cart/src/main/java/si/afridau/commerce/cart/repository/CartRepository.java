package si.afridau.commerce.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.afridau.commerce.cart.model.Cart;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);

    Optional<Cart> findByUserIdAndCartId(UUID userId, UUID cartId);
}
