package com.example.ecommerce.mappers;

import com.example.ecommerce.dtos.UserDto;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.requests.RegisterUserRequest;
import com.example.ecommerce.requests.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt" , expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request) ;

    void update(UpdateUserRequest request, @MappingTarget User user) ;
}
