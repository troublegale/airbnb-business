package itmo.tg.airbnb_business.business.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OpenAPIConfig {

    @Value("${server.port}")
    private int port;

    @Bean
    public OpenAPI customOpenAPI() {
        log.info("Swagger UI available at http://localhost:{}/swagger-ui/index.html", port);
        return new OpenAPI()
                .info(new Info()
                        .title("Airbnb Fines & Penalties API")
                        .version("1.0")
                        .description("BLPS Lab work at ITMO University"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

}
