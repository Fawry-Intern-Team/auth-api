package com.example.auth_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/auth")
public class TestController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public String test() {
        String productServiceResponse = restTemplate.getForObject("http://product-service/api/products/test", String.class);
        return "Auth Service is running! Product Service says: " + productServiceResponse;
    }
}
