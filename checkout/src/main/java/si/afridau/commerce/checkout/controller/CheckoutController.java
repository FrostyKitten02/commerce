package si.afridau.commerce.checkout.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Checkout", description = "Order checkout and management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Operation(
        summary = "Submit cart for checkout",
        description = "Process the user's shopping cart and create an order with billing and shipping information"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Order created successfully",
        content = @Content(schema = @Schema(implementation = OrderCreatedRes.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid checkout data or empty cart")
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @PostMapping("/submit-cart")
    public ResponseEntity<OrderCreatedRes> submitCart(
            @Valid @RequestBody SubmitCartReq request,
            @AuthenticationPrincipal JwtUser user) {
        
        OrderCreatedRes response = checkoutService.submitCart(request, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Get user orders",
        description = "Retrieve all orders placed by the current authenticated user with order details and status"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Orders retrieved successfully",
        content = @Content(schema = @Schema(implementation = OrderDto.class))
    )
    @ApiResponse(responseCode = "401", description = "Authentication required")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getUserOrders(
            @AuthenticationPrincipal JwtUser user) {
        
        List<OrderDto> orders = checkoutService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }
}