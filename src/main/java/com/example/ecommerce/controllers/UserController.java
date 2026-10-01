package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.UserDto;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.mappers.UserMapper;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.requests.RegisterUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> index(
            @RequestParam(required = false , defaultValue = "" , name = "sort") String sortBy
    ) {
        Sort sorting = sortBy != null
                ? Sort.by(sortBy)
                : Sort.unsorted();
        return userRepository.findAll(sorting)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> show(@PathVariable Integer id) {
       var user = userRepository.findById(id).orElse(null);
       if(user == null) {
           return  ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(userMapper.toDto(user)) ;
    }

    @PostMapping
    public UserDto store(
            @RequestBody RegisterUserRequest request
    ){
        var user= userMapper.toEntity(request) ;
        var storedUser=userRepository.save(user) ;
        var usermap = userMapper.toDto(storedUser);
        return usermap ;
    }
}
