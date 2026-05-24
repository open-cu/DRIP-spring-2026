package edu.course.provider.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PreDestroy;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notifications", description = "Поток событий платформы")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // SSE нужен для части лекции про WebClient: поток событий естественно читается
    // как Flux, а не как одиночный blocking response.
    @Operation(summary = "Получить короткий stream событий")
    @ApiResponse(responseCode = "200", description = "SSE stream событий")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(15_000L);
        executor.execute(() -> emitEvents(emitter));
        return emitter;
    }

    private void emitEvents(SseEmitter emitter) {
        try {
            for (int i = 1; i <= 5; i++) {
                NotificationEvent event = new NotificationEvent(
                        "COURSE_UPDATED",
                        "Demo course event #" + i,
                        Instant.now()
                );
                emitter.send(SseEmitter.event()
                        .name("course-event")
                        .data(event));
                Thread.sleep(700);
            }
            emitter.complete();
        } catch (Exception ex) {
            emitter.completeWithError(ex);
        }
    }

    @PreDestroy
    void shutdown() {
        executor.shutdownNow();
    }
}
