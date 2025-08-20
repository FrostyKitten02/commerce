package si.afridau.commerce.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import si.afridau.commerce.catalog.client.storage.api.DefaultApi;
import si.afridau.commerce.catalog.dto.ProductDto;
import si.afridau.commerce.catalog.mapper.ProductMapper;
import si.afridau.commerce.catalog.model.Product;
import si.afridau.commerce.catalog.request.CreateProductReq;
import si.afridau.commerce.catalog.request.UpdateProductReq;
import si.afridau.commerce.catalog.response.GetProductRes;
import si.afridau.commerce.catalog.response.ProductListRes;
import si.afridau.commerce.catalog.response.ResourceCreatedRes;
import si.afridau.commerce.catalog.service.ProductService;

import java.util.List;
import java.util.UUID;

//TODO add permissions for actions!!! don't only look for ADMIN role

@Validated
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@Tag(name = "Product Catalog", description = "Product management endpoints for catalog service")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final DefaultApi storageApi;

    @Operation(
        summary = "Create new product",
        description = "Create a new product with details and optional image upload. Requires admin privileges."
    )
    @ApiResponse(
        responseCode = "201",
        description = "Product created successfully",
        content = @Content(schema = @Schema(implementation = ResourceCreatedRes.class))
    )
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @ApiResponse(responseCode = "403", description = "Access denied - admin role required")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResourceCreatedRes createProduct(
            @ModelAttribute @Valid CreateProductReq body, HttpServletResponse servletResponse
    ) {
        Product product = productService.createProduct(body);

        servletResponse.setStatus(HttpServletResponse.SC_CREATED);

        ResourceCreatedRes res = new ResourceCreatedRes();
        res.setId(product.getId());
        return res;
    }

    @Operation(
        summary = "Get product by ID",
        description = "Retrieve detailed information about a specific product by its unique identifier"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Product found and returned",
        content = @Content(schema = @Schema(implementation = GetProductRes.class))
    )
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("{productId}")
    public GetProductRes getProduct(
            @PathVariable @NotNull UUID productId
    ) {
        Product product = productService.getProduct(productId);
        GetProductRes res = new GetProductRes();
        res.setProduct(productMapper.toProductDto(product));
        return res;
    }

    @Operation(
        summary = "Get products by IDs",
        description = "Retrieve multiple products by providing a list of product IDs as query parameters"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Products found and returned",
        content = @Content(schema = @Schema(implementation = GetProductRes.class))
    )
    @GetMapping
    public GetProductRes getProducts(
            @NotNull @RequestParam List<UUID> ids
    ) {
        List<ProductDto> dtos = productService.getProducts(ids);
        GetProductRes res = new GetProductRes();
        res.setProducts(dtos);
        return res;
    }

    @Operation(
        summary = "Update product",
        description = "Update existing product with new data and optional image. Requires admin privileges."
    )
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "403", description = "Access denied - admin role required")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "{productId}", consumes = {"multipart/form-data"})
    public void updateProduct(
            @ModelAttribute @Valid UpdateProductReq body,
            @PathVariable @NotNull UUID productId
    ) {
        productService.updateProduct(body, productId);
    }

    @Operation(
        summary = "Replace product",
        description = "Completely replace existing product with new data. Requires admin privileges."
    )
    @ApiResponse(responseCode = "200", description = "Product replaced successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "403", description = "Access denied - admin role required")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{productId}")
    public void replaceProduct(
            @RequestBody @Valid CreateProductReq body,
            @PathVariable @NotNull UUID productId
    ) {
        productService.replaceProduct(body, productId);
    }

    @Operation(
        summary = "Delete product",
        description = "Permanently delete a product from the catalog. Requires admin privileges."
    )
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @ApiResponse(responseCode = "403", description = "Access denied - admin role required")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{productId}")
    public void deleteProduct(
            @PathVariable @NotNull UUID productId
    ) {
        productService.deleteProduct(productId);
    }

    @Operation(
        summary = "Search all products",
        description = "Retrieve list of all available products in the catalog with pagination support"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Product list retrieved successfully",
        content = @Content(schema = @Schema(implementation = ProductListRes.class))
    )
    @GetMapping("list")
    public ProductListRes searchProducts() {
        return productService.searchProducts();
    }

}
