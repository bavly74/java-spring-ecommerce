package com.example.ecommerce.requests;

import lombok.Data;

@Data
public class UpdateUserPasswordRequest {
    private String oldPassword ;
    private String newPassword ;

}
