package pl.kubaretip.cloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(predicateSpec -> predicateSpec.path("/auth-service/**")
                        .filters(f -> f.rewritePath("/auth-service/(?<remaining>.*)", "/${remaining}"))
                        .uri("lb://AUTH")
                        .id("AUTH")
                )
                .route(predicateSpec -> predicateSpec.path("/chat-service/**")
                        .filters(f -> f.rewritePath("/chat-service/(?<remaining>.*)", "/${remaining}"))
                        .uri("lb://CHAT")
                        .id("CHAT")
                )
                .route(predicateSpec -> predicateSpec.path("/chat-messages-service/**")
                        .filters(f -> f.rewritePath("/chat-messages-service/(?<remaining>.*)", "/${remaining}"))
                        .uri("lb://CHAT-MESSAGES")
                        .id("CHAT-MESSAGES")
                )
                .route(predicateSpec -> predicateSpec.path("/messages-websocket-service/**")
                        .filters(f -> f.rewritePath("/messages-websocket-service/(?<remaining>.*)", "/${remaining}"))
                        .uri("lb://MESSAGES-WEBSOCKET")
                        .id("MESSAGES-WEBSOCKET")
                )
                .route(predicateSpec -> predicateSpec.path("/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v3/api-docs"))
                        .uri("http://localhost:8080")
                        .id("openapi")
                )
                .build();
    }


}
