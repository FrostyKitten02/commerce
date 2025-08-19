package si.afridau.commerce.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import si.afridau.commerce.cart.model.CartProduct;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, UUID> {
    @Query("SELECT cp FROM CartProduct cp WHERE cp.cart.id = :cartId AND cp.productId = :productId")
    Optional<CartProduct> findByCartIdAndProductId(UUID cartId, UUID productId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM CartProduct cp WHERE cp.cart.id = :cartId")
    void deleteByCartId(UUID cartId);
}
