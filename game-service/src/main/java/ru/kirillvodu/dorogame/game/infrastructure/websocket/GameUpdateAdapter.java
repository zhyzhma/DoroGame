package ru.kirillvodu.dorogame.game.infrastructure.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.abstractions.events.GameUpdateNotifier;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.infrastructure.websocket.converters.GameUpdatePayloadConverter;

@Component
public class GameUpdateAdapter implements GameUpdateNotifier {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private GameUpdatePayloadConverter gameUpdatePayloadConverter;

    @Override
    public void notifyGameUpdated(DoroGame game) {
        simpMessagingTemplate.convertAndSend("/topic/game/" + game.getId(),
                gameUpdatePayloadConverter.convert(game));
    }
}
