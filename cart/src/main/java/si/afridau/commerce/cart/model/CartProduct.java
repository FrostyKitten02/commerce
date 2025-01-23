package si.afridau.commerce.cart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import si.afridau.commerce.cart.model.base.BaseModel;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class CartProduct extends BaseModel {
    @NotNull
    private UUID productId;
    @NotNull
    @Min(0)
    @Digits(integer = 8, fraction = 4, message = "Quantity ca have up to 8 digits and 4 decimals.")
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cartId", nullable=false)
    private Cart cart;
}
