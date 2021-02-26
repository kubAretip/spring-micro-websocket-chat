package pl.kubaretip.cloudgateway.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    private RouteLocator locator;

    public OpenAPIConfig(RouteLocator locator) {
        this.locator = locator;
    }

    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        locator.getRoutes()
                .subscribe(route -> {
                    if (route.getId().toLowerCase().startsWith("ReactiveCompositeDiscoveryClient_".toLowerCase())) {
                        String name = route.getId().replaceAll("ReactiveCompositeDiscoveryClient_", "");
                        swaggerUiConfigParameters.addGroup(name);
                        groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/v3/api-docs").group(name).build());
                    }
                });
        return groups;

    }

}
