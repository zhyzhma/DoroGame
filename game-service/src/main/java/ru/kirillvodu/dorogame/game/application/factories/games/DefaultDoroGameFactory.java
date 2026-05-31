package ru.kirillvodu.dorogame.game.application.factories.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.factories.fields.FieldFactory;
import ru.kirillvodu.dorogame.game.application.factories.winCheckers.WinCheckerFactory;
import ru.kirillvodu.dorogame.game.application.properties.StartingCoordsProperties;
import ru.kirillvodu.dorogame.game.domain.model.Chip;
import ru.kirillvodu.dorogame.game.domain.model.DoroGame;
import ru.kirillvodu.dorogame.game.domain.model.Invitation;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;
import ru.kirillvodu.dorogame.game.domain.model.enums.FieldVariant;
import ru.kirillvodu.dorogame.game.domain.model.enums.WinCheckerVariant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultDoroGameFactory implements DoroGameFactory {

    private final Map<FieldVariant, FieldFactory> fieldFactoryMap;
    private final Map<WinCheckerVariant, WinCheckerFactory> winCheckerFactoryMap;

    @Autowired
    private StartingCoordsProperties startingCoordsProperties;

    public DefaultDoroGameFactory(List<FieldFactory> fieldFactories,
                                  List<WinCheckerFactory> winCheckerFactories) {
        this.fieldFactoryMap = fieldFactories.stream()
                .collect(Collectors.toUnmodifiableMap(FieldFactory::fieldVariant, f -> f));
        this.winCheckerFactoryMap = winCheckerFactories.stream()
                .collect(Collectors.toUnmodifiableMap(WinCheckerFactory::winCheckerVariant, f -> f));
    }

    @Override
    public DoroGame createDoroGame(UserReadModel acceptingUser, Invitation invitation) {
        return new DoroGame(
                invitation.user(),
                acceptingUser,
                fieldFactoryMap.get(invitation.gameConfig().field()).createField(),
                winCheckerFactoryMap.get(invitation.gameConfig().winChecker()).createWinChecker(),
                startingCoordsProperties.getPlayer1().stream().map(Chip::new).toList(),
                startingCoordsProperties.getPlayer2().stream().map(Chip::new).toList(),
                invitation.gameConfig().turn()
        );
    }
}
