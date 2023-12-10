package org.vitamin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Vitamin API",
        version = "1.0",
        description = "This API allows clients to interact with the Vitamin application. It provides operations to get all vitamins and get a vitamin by name.",
        contact = @Contact(
            name = "Vitamin API Support",
            email = "prolama6@gmail.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    )
)
public class SwaggerConfig {

}