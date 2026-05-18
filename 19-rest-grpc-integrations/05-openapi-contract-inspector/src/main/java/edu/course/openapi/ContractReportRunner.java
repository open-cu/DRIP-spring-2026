package edu.course.openapi;

import edu.course.openapi.config.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class ContractReportRunner implements CommandLineRunner {

    private final OpenApiProperties properties;

    public ContractReportRunner(OpenApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public void run(String... args) {
        ParseOptions options = new ParseOptions();
        // Resolve раскрывает ссылки внутри OpenAPI, чтобы отчет был ближе к тому,
        // что увидят генераторы клиентов и contract-check инструменты.
        options.setResolve(true);

        SwaggerParseResult result = parse(properties.getSource(), options);

        if (result.getMessages() != null && !result.getMessages().isEmpty()) {
            result.getMessages().forEach(message -> System.out.println("[parser] " + message));
        }

        OpenAPI openAPI = result.getOpenAPI();
        if (openAPI == null) {
            throw new IllegalStateException("OpenAPI contract could not be parsed: " + properties.getSource());
        }

        System.out.println("\n--- OpenAPI contract report ---");
        System.out.println("Source: " + properties.getSource());
        System.out.println("Title: " + openAPI.getInfo().getTitle());
        System.out.println("Version: " + openAPI.getInfo().getVersion());
        System.out.println("Paths:");

        openAPI.getPaths().forEach((path, pathItem) ->
                pathItem.readOperationsMap().forEach((method, operation) ->
                        printOperation(method, path, operation)
                )
        );

        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            System.out.println("Schemas:");
            openAPI.getComponents().getSchemas().keySet().stream()
                    .sorted()
                    .forEach(schemaName -> System.out.println("  - " + schemaName));
        }
    }

    private SwaggerParseResult parse(String source, ParseOptions options) {
        // Один runner умеет читать и live /v3/api-docs, и локальный schema-first YAML.
        if (source.startsWith("classpath:")) {
            String path = source.substring("classpath:".length());
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            try {
                ClassPathResource resource = new ClassPathResource(path);
                String contents = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                return new OpenAPIV3Parser().readContents(contents, null, options);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not read OpenAPI resource: " + source, ex);
            }
        }
        return new OpenAPIV3Parser().readLocation(source, null, options);
    }

    private void printOperation(PathItem.HttpMethod method, String path, Operation operation) {
        String operationId = operation.getOperationId() == null ? "<no operationId>" : operation.getOperationId();
        String summary = operation.getSummary() == null ? "" : " - " + operation.getSummary();
        System.out.println("  - " + method + " " + path + " (" + operationId + ")" + summary);

        if (operation.getResponses() != null) {
            for (Map.Entry<String, ?> response : operation.getResponses().entrySet()) {
                System.out.println("      response " + response.getKey());
            }
        }
    }
}
