package com.librarymicroservices.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/userService/**")
                        .uri("http://user-service"))
                .route("order-service", r -> r.path("/api/orderService/**")
                        .uri("http://order-service"))
                .route("storage-service", r -> r.path("/api/storageService/**")
                        .uri("http://storage-service"))
                .build();
    }
}
