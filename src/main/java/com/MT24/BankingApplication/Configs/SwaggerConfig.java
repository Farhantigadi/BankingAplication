package com.MT24.BankingApplication.Configs;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server; // ✅ Correct import
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI mycustomconfig() {
        return new OpenAPI()
                .info(new Info().title("Banking Application  APIs").description("by farhan"))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("local"),
                        new Server().url("http://localhost:8082").description("live")
                ))
//                .tags(List.of(
//                        new Tag().name("Public APIs"),
//                        new Tag().name("User APIs"),
//                        new Tag().name("Journal APIs"),
//                        new Tag().name("Admin APIs")
//                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));

    }
}

