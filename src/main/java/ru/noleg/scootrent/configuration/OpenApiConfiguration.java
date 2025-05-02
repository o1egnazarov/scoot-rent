package ru.noleg.scootrent.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Scootrent Api",
                description = "API системы управления прокатом самокатов",
                version = "1.0.0",
                contact = @Contact(
                        name = "Nazarov Oleg",
                        email = "noleg867@gmail.com"
                )
        )
)
public class OpenApiConfiguration {
}