package com.example.ecommerce.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {
    private String name ;
    private String description ;
    private Double price ;
    @JsonProperty("category_id")
    private Long categoryId ;
}
