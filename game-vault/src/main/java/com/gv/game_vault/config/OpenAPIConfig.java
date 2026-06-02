package com.gv.game_vault.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI gameVaultOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GameVault API")
                        .version("1.0.0")
                        .description("REST API for managing a video game catalog with genres, prices, release years, and filtering capabilities.")
                        .contact(new Contact()
                                .name("Máximo Flores García")));
    }
}