package org.macronutrient.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Macronutrient API",
        version = "1.0",
        description = "This API allows clients to interact with the Macronutrient application. It provides operations to get all macronutrients, get all macronutrient types, get a macronutrient by name, and get a macronutrient type by name.",
        contact = @Contact(
            name = "Macronutrient API Support",
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