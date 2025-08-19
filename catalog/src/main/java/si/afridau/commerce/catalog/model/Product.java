package si.afridau.commerce.catalog.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import si.afridau.commerce.catalog.model.base.BaseModel;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Product extends BaseModel {
    private String sku;
    private String name;
    private String description;
    @Digits(integer = 6, fraction = 2, message = "Price can have up to 6 digits and 2 decimal places")
    private BigDecimal price;
    private UUID pictureId;
}
