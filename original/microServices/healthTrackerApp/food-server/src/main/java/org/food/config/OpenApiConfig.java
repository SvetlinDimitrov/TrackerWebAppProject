package org.food.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Food API",
        version = "1.0",
        description = "This API allows clients to interact with the Food application. It provides operations to add, retrieve, and delete custom foods. It also provides operations to get all custom foods and get a custom food by name.",
        contact = @Contact(
            name = "Food API Support",
            email = "prolama6@gmail.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    )
)
public class OpenApiConfig {

}