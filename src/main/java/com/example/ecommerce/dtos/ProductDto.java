package com.example.ecommerce.dtos;

import com.example.ecommerce.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
    private Long id ;
    private String name ;
    private String description ;
    private Double price ;
    private CategoryDto category;
}