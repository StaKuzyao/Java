package com.example.openweather.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lab_Java API")
                        .description("Документация API с использованием SpringDoc")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Stanislav Kuzmichev")
                                .url("https://soundcloud.com/stas-kuzmichev")
                                .email("staska9988@gmail.com"))
                        .license(new License()
                                .name("Лицензия API")
                                .url("https://copilot.microsoft.com/chats/q9BvphcopEcVCefyXkZqo")));

    }
}
