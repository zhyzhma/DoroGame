package ru.kirillvodu.dorogame.game.infrastructure.grpc;

import ru.kirillvodu.dorogame.game.domain.model.user.UserReadModel;
import ru.kirillvodu.grpc.user.UserProto;

import java.util.UUID;

public final class UserGrpcMapper {

    private UserGrpcMapper() {}

    public static UserReadModel toReadModel(UserProto proto) {
        return new UserReadModel(UUID.fromString(proto.getId()), proto.getName());
    }
}
