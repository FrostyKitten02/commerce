package si.afridau.commerce.checkout.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import si.afridau.commerce.auth.model.JwtUser;
import si.afridau.commerce.checkout.dto.OrderDto;
import si.afridau.commerce.checkout.dto.request.SubmitCartReq;
import si.afridau.commerce.checkout.dto.response.OrderCreatedRes;
import si.afridau.commerce.checkout.service.CheckoutService;

import java.util.List;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/submit-cart")
    public ResponseEntity<OrderCreatedRes> submitCart(
            @Valid @RequestBody SubmitCartReq request,
            @AuthenticationPrincipal JwtUser user) {
        
        OrderCreatedRes response = checkoutService.submitCart(request, user);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getUserOrders(
            @AuthenticationPrincipal JwtUser user) {
        
        List<OrderDto> orders = checkoutService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }
}