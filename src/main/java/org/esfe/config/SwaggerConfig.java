package org.esfe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "MarketLocalShirts API",
                description = "Api rest que permite el registro y mantenimiento de camisas, categorias, marcas, usuarios y pedidos",
                version = "1.0.0"
        )
)
public class SwaggerConfig {
}
