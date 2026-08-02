package ru.kirillvodu.dorogame.game.presentation.websocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.kirillvodu.dorogame.game.application.services.GameAccessChecker;

import java.security.Principal;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${rabbitmq.user}")
    private String user;

    @Value("${rabbitmq.password}")
    private String password;

    private GameAccessChecker gameAccessChecker;

    public WebSocketConfig(GameAccessChecker gameAccessChecker) {
        this.gameAccessChecker = gameAccessChecker;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("rabbitmq")
                .setRelayPort(61613)
                .setClientLogin(user)
                .setClientPasscode(password)
                .setSystemLogin(user)
                .setSystemPasscode(password);
    }

    @Bean
    public ChannelInterceptor authChannelInterceptor(JwtDecoder jwtDecoder) {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");
                    if (token == null) {
                        throw new MessagingException("Missing Authorization header");
                    }
                    Jwt jwt = jwtDecoder.decode(token.replace("Bearer ", ""));
                    accessor.setUser(new JwtAuthenticationToken(jwt));
                }

                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    String destination = accessor.getDestination();
                    if (destination == null || destination.isEmpty()) {
                        throw new MessagingException("Missing Destination header");
                    }
                    UUID gameId = extractGameId(destination);
                    Principal user = accessor.getUser();
                    if (user == null) {
                        throw new MessagingException("Missing User header");
                    }
                    UUID userId = UUID.fromString(user.getName());
                    if (!gameAccessChecker.isUserInGame(userId, gameId)) {
                        throw new MessagingException("User is not in game");
                    }
                }
                return message;
            }
        };
    }

    private UUID extractGameId(String destination) {
        String[] parts = destination.split("/");
        return UUID.fromString(parts[parts.length - 1]);
    }
}
