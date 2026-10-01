package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.UserDto;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.mappers.UserMapper;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.requests.RegisterUserRequest;
import com.example.ecommerce.requests.UpdateUserPasswordRequest;
import com.example.ecommerce.requests.UpdateUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<UserDto> store(
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ){
        var user= userMapper.toEntity(request) ;
        var storedUser=userRepository.save(user) ;
        var userMapDto = userMapper.toDto(storedUser);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userMapDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userMapDto) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @RequestBody UpdateUserRequest request ,
            @PathVariable(name = "id") Integer id
    ) {
        var user = userRepository.findById(id).orElse(null) ;
        if (user == null){
            ResponseEntity.notFound().build();
        }
        userMapper.update(request,user);
        userRepository.save(user) ;
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id
    )
    {
        var user = userRepository.findById(id).orElse(null) ;
        if (user == null){
            ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return  ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> updatePassword(
        @PathVariable Integer id ,
        @RequestBody UpdateUserPasswordRequest request
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        if(!user.getPassword().equals(request.getOldPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }
}
