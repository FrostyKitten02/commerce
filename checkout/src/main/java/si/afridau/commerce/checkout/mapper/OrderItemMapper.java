package si.afridau.commerce.checkout.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import si.afridau.commerce.checkout.dto.OrderItemDto;
import si.afridau.commerce.checkout.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    
    @Mapping(source = "order.id", target = "orderId")
    OrderItemDto toOrderItemDto(OrderItem orderItem);
    
    @Mapping(target = "order", ignore = true)
    OrderItem toModel(OrderItemDto dto);
}