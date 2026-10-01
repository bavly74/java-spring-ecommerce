package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @GetMapping("/sayHello")
    public Message sayHello(){
        return new Message("Hello") ;

    }
}
