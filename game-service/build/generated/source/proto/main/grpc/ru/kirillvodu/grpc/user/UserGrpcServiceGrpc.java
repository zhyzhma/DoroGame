package ru.kirillvodu.grpc.user;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: user_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UserGrpcServiceGrpc {

  private UserGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "UserGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ru.kirillvodu.grpc.user.GetUserByIdRequest,
      ru.kirillvodu.grpc.user.GetUserByIdResponse> getGetUserByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUserById",
      requestType = ru.kirillvodu.grpc.user.GetUserByIdRequest.class,
      responseType = ru.kirillvodu.grpc.user.GetUserByIdResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ru.kirillvodu.grpc.user.GetUserByIdRequest,
      ru.kirillvodu.grpc.user.GetUserByIdResponse> getGetUserByIdMethod() {
    io.grpc.MethodDescriptor<ru.kirillvodu.grpc.user.GetUserByIdRequest, ru.kirillvodu.grpc.user.GetUserByIdResponse> getGetUserByIdMethod;
    if ((getGetUserByIdMethod = UserGrpcServiceGrpc.getGetUserByIdMethod) == null) {
      synchronized (UserGrpcServiceGrpc.class) {
        if ((getGetUserByIdMethod = UserGrpcServiceGrpc.getGetUserByIdMethod) == null) {
          UserGrpcServiceGrpc.getGetUserByIdMethod = getGetUserByIdMethod =
              io.grpc.MethodDescriptor.<ru.kirillvodu.grpc.user.GetUserByIdRequest, ru.kirillvodu.grpc.user.GetUserByIdResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUserById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.kirillvodu.grpc.user.GetUserByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.kirillvodu.grpc.user.GetUserByIdResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserGrpcServiceMethodDescriptorSupplier("GetUserById"))
              .build();
        }
      }
    }
    return getGetUserByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ru.kirillvodu.grpc.user.GetUsersByIdsRequest,
      ru.kirillvodu.grpc.user.GetUsersByIdsResponse> getGetUsersByIdsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUsersByIds",
      requestType = ru.kirillvodu.grpc.user.GetUsersByIdsRequest.class,
      responseType = ru.kirillvodu.grpc.user.GetUsersByIdsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ru.kirillvodu.grpc.user.GetUsersByIdsRequest,
      ru.kirillvodu.grpc.user.GetUsersByIdsResponse> getGetUsersByIdsMethod() {
    io.grpc.MethodDescriptor<ru.kirillvodu.grpc.user.GetUsersByIdsRequest, ru.kirillvodu.grpc.user.GetUsersByIdsResponse> getGetUsersByIdsMethod;
    if ((getGetUsersByIdsMethod = UserGrpcServiceGrpc.getGetUsersByIdsMethod) == null) {
      synchronized (UserGrpcServiceGrpc.class) {
        if ((getGetUsersByIdsMethod = UserGrpcServiceGrpc.getGetUsersByIdsMethod) == null) {
          UserGrpcServiceGrpc.getGetUsersByIdsMethod = getGetUsersByIdsMethod =
              io.grpc.MethodDescriptor.<ru.kirillvodu.grpc.user.GetUsersByIdsRequest, ru.kirillvodu.grpc.user.GetUsersByIdsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUsersByIds"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.kirillvodu.grpc.user.GetUsersByIdsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ru.kirillvodu.grpc.user.GetUsersByIdsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserGrpcServiceMethodDescriptorSupplier("GetUsersByIds"))
              .build();
        }
      }
    }
    return getGetUsersByIdsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserGrpcServiceStub>() {
        @java.lang.Override
        public UserGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserGrpcServiceStub(channel, callOptions);
        }
      };
    return UserGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserGrpcServiceBlockingStub>() {
        @java.lang.Override
        public UserGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return UserGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserGrpcServiceFutureStub>() {
        @java.lang.Override
        public UserGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return UserGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getUserById(ru.kirillvodu.grpc.user.GetUserByIdRequest request,
        io.grpc.stub.StreamObserver<ru.kirillvodu.grpc.user.GetUserByIdResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserByIdMethod(), responseObserver);
    }

    /**
     */
    default void getUsersByIds(ru.kirillvodu.grpc.user.GetUsersByIdsRequest request,
        io.grpc.stub.StreamObserver<ru.kirillvodu.grpc.user.GetUsersByIdsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUsersByIdsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UserGrpcService.
   */
  public static abstract class UserGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UserGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UserGrpcService.
   */
  public static final class UserGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UserGrpcServiceStub> {
    private UserGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getUserById(ru.kirillvodu.grpc.user.GetUserByIdRequest request,
        io.grpc.stub.StreamObserver<ru.kirillvodu.grpc.user.GetUserByIdResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUsersByIds(ru.kirillvodu.grpc.user.GetUsersByIdsRequest request,
        io.grpc.stub.StreamObserver<ru.kirillvodu.grpc.user.GetUsersByIdsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUsersByIdsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UserGrpcService.
   */
  public static final class UserGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UserGrpcServiceBlockingStub> {
    private UserGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ru.kirillvodu.grpc.user.GetUserByIdResponse getUserById(ru.kirillvodu.grpc.user.GetUserByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public ru.kirillvodu.grpc.user.GetUsersByIdsResponse getUsersByIds(ru.kirillvodu.grpc.user.GetUsersByIdsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUsersByIdsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UserGrpcService.
   */
  public static final class UserGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UserGrpcServiceFutureStub> {
    private UserGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ru.kirillvodu.grpc.user.GetUserByIdResponse> getUserById(
        ru.kirillvodu.grpc.user.GetUserByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ru.kirillvodu.grpc.user.GetUsersByIdsResponse> getUsersByIds(
        ru.kirillvodu.grpc.user.GetUsersByIdsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUsersByIdsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_USER_BY_ID = 0;
  private static final int METHODID_GET_USERS_BY_IDS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_USER_BY_ID:
          serviceImpl.getUserById((ru.kirillvodu.grpc.user.GetUserByIdRequest) request,
              (io.grpc.stub.StreamObserver<ru.kirillvodu.grpc.user.GetUserByIdResponse>) responseObserver);
          break;
        case METHODID_GET_USERS_BY_IDS:
          serviceImpl.getUsersByIds((ru.kirillvodu.grpc.user.GetUsersByIdsRequest) request,
              (io.grpc.stub.StreamObserver<ru.kirillvodu.grpc.user.GetUsersByIdsResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetUserByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ru.kirillvodu.grpc.user.GetUserByIdRequest,
              ru.kirillvodu.grpc.user.GetUserByIdResponse>(
                service, METHODID_GET_USER_BY_ID)))
        .addMethod(
          getGetUsersByIdsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ru.kirillvodu.grpc.user.GetUsersByIdsRequest,
              ru.kirillvodu.grpc.user.GetUsersByIdsResponse>(
                service, METHODID_GET_USERS_BY_IDS)))
        .build();
  }

  private static abstract class UserGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ru.kirillvodu.grpc.user.UserServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserGrpcService");
    }
  }

  private static final class UserGrpcServiceFileDescriptorSupplier
      extends UserGrpcServiceBaseDescriptorSupplier {
    UserGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class UserGrpcServiceMethodDescriptorSupplier
      extends UserGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UserGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (UserGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserGrpcServiceFileDescriptorSupplier())
              .addMethod(getGetUserByIdMethod())
              .addMethod(getGetUsersByIdsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
