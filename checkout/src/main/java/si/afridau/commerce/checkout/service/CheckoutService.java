package si.afridau.commerce.checkout.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import si.afridau.commerce.checkout.client.cart.api.CartControllerApi;
import si.afridau.commerce.checkout.client.cart.model.GetCartRes;
import si.afridau.commerce.checkout.client.cart.model.CartDto;
import si.afridau.commerce.checkout.client.cart.model.CartProductDto;
import si.afridau.commerce.checkout.dto.request.SubmitCartReq;
import si.afridau.commerce.checkout.dto.response.OrderCreatedRes;
import si.afridau.commerce.checkout.dto.OrderDto;
import si.afridau.commerce.checkout.mapper.OrderMapper;
import si.afridau.commerce.checkout.model.Order;
import si.afridau.commerce.checkout.model.OrderItem;
import si.afridau.commerce.checkout.repository.OrderRepository;
import si.afridau.commerce.checkout.repository.OrderItemRepository;
import si.afridau.commerce.auth.model.JwtUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutService {

    private final CartControllerApi cartControllerApi;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderCreatedRes submitCart(SubmitCartReq request, JwtUser user) {
        log.info("Processing checkout for cart: {} and user: {}", request.getCartId(), user.getId());
        
        GetCartRes cartResponse = cartControllerApi.getCart();
        CartDto cart = cartResponse.getCart();
        
        if (cart == null || cart.getCartProducts() == null || cart.getCartProducts().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        Order order = Order.builder()
                .userId(user.getId())
                .cartId(request.getCartId())
                .shippingAddress(request.getShippingAddress())
                .shippingCity(request.getShippingCity())
                .shippingPostalCode(request.getShippingPostalCode())
                .shippingCountry(request.getShippingCountry())
                .totalAmount(cart.getTotal())
                .orderItems(new ArrayList<>())
                .build();
        
        Order savedOrder = orderRepository.save(order);
        
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartProductDto cartProduct : cart.getCartProducts()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .productId(cartProduct.getProductId())
                    .productName(cartProduct.getName())
                    .productSku(cartProduct.getSku())
                    .unitPrice(cartProduct.getPrice())
                    .quantity(cartProduct.getQuantity().intValue())
                    .lineTotal(cartProduct.getLineTotal())
                    .build();
            orderItems.add(orderItem);
        }
        
        orderItemRepository.saveAll(orderItems);
        
        cartControllerApi.deleteCart();
        
        log.info("Order created with ID: {}", savedOrder.getId());
        
        OrderCreatedRes response = new OrderCreatedRes();
        response.setOrderId(savedOrder.getId());
        return response;
    }
    
    public List<OrderDto> getUserOrders(JwtUser user) {
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }
}