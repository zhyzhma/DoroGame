package ru.kirillvodu.dorogame.game.presentation.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.kirillvodu.dorogame.game.GameServiceApp;
import ru.kirillvodu.dorogame.game.IntegrationTestBase;
import ru.kirillvodu.dorogame.game.application.services.GameAccessChecker;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GameServiceApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketRabbitMqIT extends IntegrationTestBase {

    @LocalServerPort
    private int port;

    @MockitoBean
    private GameAccessChecker gameAccessChecker;

    @BeforeEach
    void allowAnyGameAccess() {
        when(gameAccessChecker.isUserInGame(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(true);
    }

    @Test
    void subscribe_receivesRelayedMessage_whenRealRabbitMqConnected() throws Exception {
        // Arrange
        UUID gameId = UUID.randomUUID();

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer " + authToken);

        CompletableFuture<StompSession> sessionFuture = new CompletableFuture<>();
        stompClient.connectAsync("ws://localhost:" + port + "/ws/websocket",
                (WebSocketHttpHeaders) null, connectHeaders,
                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        sessionFuture.complete(session);
                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                        sessionFuture.completeExceptionally(exception);
                    }

                    @Override
                    public void handleTransportError(StompSession session, Throwable exception) {
                        sessionFuture.completeExceptionally(exception);
                    }
                }).exceptionally(ex -> {
                    sessionFuture.completeExceptionally(ex);
                    return null;
                });
        StompSession session = sessionFuture.get(10, TimeUnit.SECONDS);

        CompletableFuture<String> messageFuture = new CompletableFuture<>();
        session.subscribe("/topic/game." + gameId, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageFuture.complete((String) payload);
            }
        });

        Thread.sleep(1000);

        // Act
        session.send("/topic/game." + gameId, "ping-from-real-rabbitmq");

        // Assert
        String received = messageFuture.get(10, TimeUnit.SECONDS);
        assertThat(received).isEqualTo("ping-from-real-rabbitmq");

        session.disconnect();
    }
}
