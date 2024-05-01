package com.librarymicroservices.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @RestController
    public static class TestController {

        private final RestTemplate restTemplate;

        public TestController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @GetMapping("/test")
        public String testApiGateway() {
            // Отправляем GET-запрос через API Gateway к микросервису UserService
            String response = restTemplate.getForObject("http://localhost:8080/api/userService/test", String.class);
            return "Response from UserService via API Gateway: " + response;
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
