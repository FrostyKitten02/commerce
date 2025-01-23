package si.afridau.commerce.catalog.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import si.afridau.commerce.catalog.dto.CreateProductDto;
import si.afridau.commerce.catalog.dto.ProductDto;
import si.afridau.commerce.catalog.dto.UpdateProductDto;
import si.afridau.commerce.catalog.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toModel(CreateProductDto dto);
    ProductDto toProductDto(Product product);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProduct(@MappingTarget Product product, UpdateProductDto dto);

    void replaceProduct(@MappingTarget Product product, CreateProductDto dto);
}
