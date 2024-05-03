package no.ntnu.microservice;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that sets up the OpenAPI documentation for the Sparesti
 * API.
 */
@Configuration

public class OpenApiConfig {
    /**
     * Sets up the custom OpenAPI object with title, description, and version
     * information.
     * <p>
     * Also configures the security scheme to use a Bearer token for authentication.
     *
     * @return the custom OpenAPI object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();

        Info info = new Info()
                .title("AuthService API")
                .description(
                        "This is the microservice for the authentification and user creation for the Sparesti webapp. Remember to add a Bearer token when using endpoints")
                .version("1.0.0");
        openAPI.setInfo(info);

        Components components = new Components();
        components.addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Please enter a valid Bearer token to authenticate."));
        openAPI.setComponents(components);

        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth");
        openAPI.addSecurityItem(securityRequirement);

        return openAPI;
    }
}
