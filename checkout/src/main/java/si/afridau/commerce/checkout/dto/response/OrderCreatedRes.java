package si.afridau.commerce.checkout.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderCreatedRes {
    private UUID orderId;
}