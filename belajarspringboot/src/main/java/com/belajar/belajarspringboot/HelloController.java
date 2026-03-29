package com.belajar.belajarspringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/pateeen")
    public String sayHello() {
        return "<div style='font-family: Arial, sans-serif; text-align: center; margin-top: 50px;'>" +
               "  <h1 style='color: #4CAF50;'>Hai, selamat datang di Spring Boot! 😎</h1>" +
               "  <p style='color: #555;'>Tampilan ini sudah dipercantik dengan HTML dan CSS dasar.</p>" +
               "</div>";
    }
}
