package si.afridau.commerce.catalog.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import si.afridau.commerce.catalog.dto.UpdateProductDto;
import si.afridau.commerce.catalog.model.Product;
import si.afridau.commerce.catalog.request.CreateProductReq;
import si.afridau.commerce.catalog.request.UpdateProductReq;
import si.afridau.commerce.catalog.response.ProductListResponse;
import si.afridau.commerce.catalog.response.ResourceCreatedResponse;
import si.afridau.commerce.catalog.service.ProductService;

import java.util.UUID;

//TODO add permissions for actions!!! don't only look for ADMIN role

@Validated
@CrossOrigin
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResourceCreatedResponse createProduct(
            @RequestBody @Valid CreateProductReq body, HttpServletResponse servletResponse
    ) {
        Product product = productService.createProduct(body);

        servletResponse.setStatus(HttpServletResponse.SC_CREATED);

        ResourceCreatedResponse res = new ResourceCreatedResponse();
        res.setId(product.getId());
        return res;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{productId}")
    public void updateProduct(
            @RequestBody @Valid UpdateProductReq body,
            @RequestParam @NotNull UUID productId
    ) {
        productService.updateProduct(body, productId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{productId}")
    public void replaceProduct(
            @RequestBody @Valid CreateProductReq body,
            @RequestParam @NotNull UUID productId
    ) {
        productService.replaceProduct(body, productId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{productId}")
    public void deleteProduct(
            @RequestParam @NotNull UUID productId
    ) {
        productService.deleteProduct(productId);
    }

    @PermitAll
    @GetMapping("list")
    public ProductListResponse searchProducts() {
        return productService.searchProducts();
    }

}
