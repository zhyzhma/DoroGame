package ru.kirillvodu.dorogame.game.infrastructure.grpc;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.kirillvodu.dorogame.game.application.abstractions.services.UserServiceAbstraction;
import ru.kirillvodu.dorogame.game.domain.model.UserReadModel;
import ru.kirillvodu.grpc.user.GetUserByIdRequest;
import ru.kirillvodu.grpc.user.GetUserByIdResponse;
import ru.kirillvodu.grpc.user.GetUsersByIdsRequest;
import ru.kirillvodu.grpc.user.GetUsersByIdsResponse;
import ru.kirillvodu.grpc.user.UserGrpcServiceGrpc;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Primary
@Component
public class UserServiceGrpcClient implements UserServiceAbstraction {

    @GrpcClient("user-service")
    private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub stub;

    @Override
    public UserReadModel getById(UUID userId) {
        try {
            GetUserByIdResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getUserById(GetUserByIdRequest.newBuilder().setId(userId.toString()).build());
            if (!response.getFound()) {
                throw new RuntimeException("User not found: " + userId);
            }
            return UserGrpcMapper.toReadModel(response.getUser());
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("user-service unavailable: " + e.getStatus().getDescription(), e);
        }
    }

    @Override
    public List<UserReadModel> getByIds(List<UUID> userIds) {
        try {
            GetUsersByIdsRequest request = GetUsersByIdsRequest.newBuilder()
                    .addAllIds(userIds.stream().map(UUID::toString).toList())
                    .build();
            GetUsersByIdsResponse response = stub.withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getUsersByIds(request);
            return response.getUsersList().stream()
                    .map(UserGrpcMapper::toReadModel)
                    .toList();
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("user-service unavailable: " + e.getStatus().getDescription(), e);
        }
    }
}
