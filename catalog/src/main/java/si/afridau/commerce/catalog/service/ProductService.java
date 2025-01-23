package si.afridau.commerce.catalog.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import si.afridau.commerce.catalog.dto.ProductDto;
import si.afridau.commerce.catalog.dto.UpdateProductDto;
import si.afridau.commerce.catalog.exceptions.implementation.ItemNotFoundException;
import si.afridau.commerce.catalog.mapper.ProductMapper;
import si.afridau.commerce.catalog.model.Product;
import si.afridau.commerce.catalog.repository.ProductRepo;
import si.afridau.commerce.catalog.request.CreateProductReq;
import si.afridau.commerce.catalog.request.UpdateProductReq;
import si.afridau.commerce.catalog.response.ProductListResponse;

import java.util.List;
import java.util.UUID;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

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

    public ProductListResponse searchProducts() {
        List<ProductDto> products = productRepo.findAll().stream().map(productMapper::toProductDto).toList();
        ProductListResponse response = new ProductListResponse();
        response.setProducts(products);
        return response;
    }

}
