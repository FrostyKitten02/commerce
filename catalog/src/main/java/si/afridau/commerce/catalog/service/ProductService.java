package si.afridau.commerce.catalog.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import si.afridau.commerce.catalog.dto.ProductDto;
import si.afridau.commerce.catalog.mapper.ProductMapper;
import si.afridau.commerce.catalog.model.Product;
import si.afridau.commerce.catalog.repository.ProductRepo;
import si.afridau.commerce.catalog.request.CreateProductReq;
import si.afridau.commerce.catalog.request.UpdateProductReq;
import si.afridau.commerce.catalog.response.ProductListRes;
import si.afridau.commerce.exception.implementation.ItemNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    public Product createProduct(@Valid CreateProductReq body) {
        Product product = productMapper.toModel(body.getProduct());
        return productRepo.save(product);
    }

    public Product getProduct(@NotNull UUID id) {
        return productRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Product not found by id"));
    }

    public List<ProductDto> getProducts(List<UUID> ids) {
        List<Product> products = productRepo.findAllById(ids);
        return products.stream().map(productMapper::toProductDto).toList();
    }


    public void updateProduct(@Valid UpdateProductReq body, @NotNull UUID id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Product not found by id"));
        productMapper.updateProduct(product, body.getProduct());
    }

    public void replaceProduct(@Valid CreateProductReq body, @NotNull UUID id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Object not found by id"));
        productMapper.replaceProduct(product, body.getProduct());
        productRepo.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepo.deleteById(id);
    }

    public ProductListRes searchProducts() {
        List<ProductDto> products = productRepo.findAll().stream().map(productMapper::toProductDto).toList();
        ProductListRes response = new ProductListRes();
        response.setProducts(products);
        return response;
    }

}
