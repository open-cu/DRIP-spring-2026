package ru.cu.controller.websocket;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ru.cu.dto.ArticleCommentDto;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleCommentWebSocketControllerTest {

    @LocalServerPort
    private int port;

    @Test
    void loadComments() throws Exception {
        WebSocketStompClient stompClient = prepareWebSocketStompClient();
        StompSessionHandler stompSessionHandler = prepareStompSessionHandler();

        StompSession stompSession = stompClient.connectAsync("http://{host}:{port}/ws", stompSessionHandler,
                        "localhost", port).get(5, TimeUnit.SECONDS);

        BlockingQueue<List<ArticleCommentDto>> resultQueue = new ArrayBlockingQueue<>(10);
        stompSession.subscribe("/topic/comments/1", new StompFrameHandler() {
            @Override
            public @Nonnull Type getPayloadType(@Nonnull StompHeaders headers) {
                return new ParameterizedTypeReference<List<ArticleCommentDto>>() {}.getType();
            }

            @Override
            public void handleFrame(@Nonnull StompHeaders headers, Object payload) {
                //noinspection unchecked
                resultQueue.add((List<ArticleCommentDto>) payload);
            }
        });

        stompSession.send("/app/comments/1/load", "{}");

        List<ArticleCommentDto> receivedComments = resultQueue.poll(5, SECONDS);
        log.info("Received comments: {}", receivedComments);
        stompSession.disconnect();
        assertThat(receivedComments).hasSize(2);
    }

    private static StompSessionHandler prepareStompSessionHandler() {
        return new StompSessionHandlerAdapter() {
            @Override
            public void handleTransportError(@Nonnull StompSession session, @Nonnull Throwable exception) {
                log.error("Transport error", exception);
            }

            @Override
            public void handleException(@Nonnull StompSession session, StompCommand command,
                                        @Nonnull StompHeaders headers, @Nonnull byte[] payload,
                                        @Nonnull Throwable exception) {
                log.error("Handling exception", exception);
            }
        };
    }

    private static WebSocketStompClient prepareWebSocketStompClient() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        SockJsClient sockJsClient = new SockJsClient(List.of(new WebSocketTransport(webSocketClient)));
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

}