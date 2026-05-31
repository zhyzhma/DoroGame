package ru.kirillvodu.dorogame.user.presentation.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.kirillvodu.dorogame.user.application.exceptions.ObjectNotFoundException;
import ru.kirillvodu.dorogame.user.application.services.UserService;
import ru.kirillvodu.dorogame.user.domain.model.User;
import ru.kirillvodu.grpc.user.GetUserByIdRequest;
import ru.kirillvodu.grpc.user.GetUserByIdResponse;
import ru.kirillvodu.grpc.user.GetUsersByIdsRequest;
import ru.kirillvodu.grpc.user.GetUsersByIdsResponse;
import ru.kirillvodu.grpc.user.UserGrpcServiceGrpc;
import ru.kirillvodu.grpc.user.UserProto;

import java.util.List;
import java.util.UUID;

@GrpcService
public class UserGrpcServiceImpl extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {

    private final UserService userService;

    public UserGrpcServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<GetUserByIdResponse> observer) {
        GetUserByIdResponse.Builder builder = GetUserByIdResponse.newBuilder();
        try {
            User user = userService.getById(UUID.fromString(request.getId()));
            builder.setUser(toProto(user)).setFound(true);
        } catch (ObjectNotFoundException e) {
            builder.setFound(false);
        }
        observer.onNext(builder.build());
        observer.onCompleted();
    }

    @Override
    public void getUsersByIds(GetUsersByIdsRequest request, StreamObserver<GetUsersByIdsResponse> observer) {
        List<User> users = userService.getByIds(
                request.getIdsList().stream().map(UUID::fromString).toList()
        );
        GetUsersByIdsResponse response = GetUsersByIdsResponse.newBuilder()
                .addAllUsers(users.stream().map(this::toProto).toList())
                .build();
        observer.onNext(response);
        observer.onCompleted();
    }

    private UserProto toProto(User user) {
        return UserProto.newBuilder()
                .setId(user.getId().toString())
                .setName(user.getName())
                .build();
    }
}
