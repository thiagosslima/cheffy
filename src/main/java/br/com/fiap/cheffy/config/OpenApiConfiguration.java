package br.com.fiap.cheffy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.PathItem;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cheffy API",
                version = "v1"
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/profiles",
            "/api/v1/addresses"
    );


    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.getPaths()
                .entrySet()
                .removeIf(this::shouldExcludePath);
    }

    private boolean shouldExcludePath(Map.Entry<String, PathItem> entry) {
        return EXCLUDED_PATHS.stream()
                .anyMatch(excludedPath -> entry.getKey().startsWith(excludedPath));
    }
}