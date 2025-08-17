package si.afridau.commerce.checkout.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import si.afridau.commerce.checkout.dto.OrderDto;
import si.afridau.commerce.checkout.model.Order;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    OrderDto toOrderDto(Order order);
    
    Order toModel(OrderDto dto);
}