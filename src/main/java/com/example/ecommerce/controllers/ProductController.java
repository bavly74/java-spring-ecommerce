package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.ProductDto;
import com.example.ecommerce.entities.Category;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.mappers.ProductMapper;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.requests.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository ;
    private final ProductMapper productMapper ;
    private final CategoryRepository categoryRepository;

    @GetMapping()
    public List<ProductDto> index(
            @RequestParam(required = false,defaultValue = "",name = "category_id") Long categoryId
    ){
        List<Product> products;
        if (categoryId == null){
            products = productRepository
                    .findAll() ;

        }else{
            products = productRepository
                    .findByCategoryId(categoryId) ;
        }

       return products
               .stream()
               .map(productMapper::toDto)
               .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> show(@PathVariable Long id) {
        var product= productRepository.findById(id).orElse(null) ;
        if (product == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product) );
    }

    @PostMapping

    public ResponseEntity<ProductDto> store(@RequestBody ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElse(null);

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        ProductDto productDto = productMapper.toDto(savedProduct);

        return ResponseEntity.ok(productDto);
    }
}
