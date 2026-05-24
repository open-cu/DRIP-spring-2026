package edu.course.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class OpenApiCodegenApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiCodegenApplication.class, args);
    }
}

@Component
class GeneratedSourcesReport implements ApplicationRunner {

    private final Path generatedRoot;

    GeneratedSourcesReport(@Value("${codegen.generated-dir:target/generated-sources}") String generatedDir) {
        this.generatedRoot = Path.of(generatedDir);
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        System.out.println("\n--- OpenAPI code generation demo ---");
        System.out.println("Generated root: " + generatedRoot.toAbsolutePath());

        // Приложение намеренно не компилирует generated sources: на лекции важнее
        // увидеть, какие artifacts появились из OpenAPI-схемы и как их читать.
        report(new GeneratedArtifact(
                "REST client API",
                "openapi-client/src/main/java/edu/course/openapi/generated/client/api/CoursesApi.java",
                List.of("listCourses", "getCourse")
        ));
        report(new GeneratedArtifact(
                "REST client model",
                "openapi-client/src/main/java/edu/course/openapi/generated/client/model/Course.java",
                List.of("class Course", "durationHours")
        ));
        report(new GeneratedArtifact(
                "Spring controller interface",
                "openapi-server/src/main/java/edu/course/openapi/generated/server/api/CoursesApi.java",
                List.of("@RequestMapping", "getCourse")
        ));

        System.out.println("\nHow to read this demo:");
        System.out.println("  OpenAPI YAML -> generated client/server contract -> handwritten adapter.");
        System.out.println("  Keep timeout, auth, retry and error mapping in your adapter layer.");
    }

    private void report(GeneratedArtifact artifact) throws IOException {
        Path file = generatedRoot.resolve(artifact.relativePath());
        System.out.println("\n[" + artifact.label() + "]");
        System.out.println("file: " + file);
        if (!Files.exists(file)) {
            System.out.println("status: missing");
            return;
        }
        System.out.println("status: generated");
        List<String> lines = Files.readAllLines(file);
        // Marker-строки связывают YAML operationId/schema fields с generated Java.
        artifact.markers().forEach(marker -> lines.stream()
                .filter(line -> line.contains(marker))
                .findFirst()
                .ifPresent(line -> System.out.println("marker: " + line.strip())));
    }

    private record GeneratedArtifact(String label, String relativePath, List<String> markers) {
    }
}
