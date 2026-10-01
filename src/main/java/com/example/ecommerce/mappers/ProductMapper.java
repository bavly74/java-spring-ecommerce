package com.example.ecommerce.mappers;

import com.example.ecommerce.dtos.ProductDto;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.requests.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto (Product product);
    @Mapping(target = "category", ignore = true)
    Product toEntity (ProductRequest request) ;
}