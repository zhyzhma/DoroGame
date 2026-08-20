package ru.kirillvodu.dorogame.game.application.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.kirillvodu.dorogame.game.domain.model.game.Coords;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "game.starting")
public class StartingCoordsProperties {
    private List<Coords> player1 = new ArrayList<>();
    private List<Coords> player2 = new ArrayList<>();
}
