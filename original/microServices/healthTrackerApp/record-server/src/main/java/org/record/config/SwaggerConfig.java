package org.record.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Record API",
        version = "1.0",
        description = "This API allows clients to interact with the Record application. It provides operations to get all records, get a record by ID, create a new record, delete a record, create a storage, and remove a storage.",
        contact = @Contact(
            name = "Record API Support",
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