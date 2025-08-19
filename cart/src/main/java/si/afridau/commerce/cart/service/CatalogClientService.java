package si.afridau.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import si.afridau.commerce.cart.client.catalog.api.ProductControllerApi;
import si.afridau.commerce.cart.client.catalog.model.GetProductRes;
import si.afridau.commerce.exception.implementation.InternalServerException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogClientService {

    private final ProductControllerApi productControllerApi;

    public GetProductRes getProducts(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return null;
        }

        try {
            return productControllerApi.getProducts(ids);
        } catch (Exception e) {
            throw new InternalServerException("Failed to fetch products from catalog service: " + e.getMessage());
        }
    }
}
