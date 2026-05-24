package ru.cu.controller.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import ru.cu.dto.JournalDto;
import ru.cu.service.JournalService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

@SuppressWarnings("unused")
@RestController
public class JournalRestController {

    private final TaskExecutor executor;
    private final JournalService journalService;

    public JournalRestController(@Qualifier("asyncExecutor") TaskExecutor executor,
                                 JournalService journalService) {
        this.executor = executor;
        this.journalService = journalService;
    }

    @GetMapping("/api/journals")
    public DeferredResult<List<JournalDto>> getAllJournals() {
        DeferredResult<List<JournalDto>> dr = new DeferredResult<>(30000L);
        try {
            executor.execute(() -> dr.setResult(journalService.findAll()));
            //throw new RejectedExecutionException();
        } catch (RejectedExecutionException e) {
            dr.setErrorResult(
                    ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Слишком много запросов", "retryAfter", 30))
            );
        }
        return dr;
    }
}
