package edu.course.resttemplate.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;

public class CourseApiErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        // Тело ошибки читается до выбрасывания exception, иначе клиент потеряет
        // ProblemDetail, который provider специально вернул как часть контракта.
        String body = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
        throw new CourseApiException(response.getStatusCode().value(), body);
    }
}
