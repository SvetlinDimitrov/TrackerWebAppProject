package org.storage.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Storage API",
        version = "1.0",
        description = "This API allows clients to interact with the Storage application. It provides operations to get all storages, get a storage by ID, create a new storage, delete a storage, create a storage for the first time, delete all storages by record ID, add food to a storage, change food in a storage, and remove food from a storage.",
        contact = @Contact(
            name = "Storage API Support",
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