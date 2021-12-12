package grpc.smartOffice;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: supplies.proto")
public final class suppliesGrpc {

  private suppliesGrpc() {}

  public static final String SERVICE_NAME = "supplies";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.smartOffice.containsOfficeSupplies,
      grpc.smartOffice.containsOrderConfirmation> getOrderSuppliesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "orderSupplies",
      requestType = grpc.smartOffice.containsOfficeSupplies.class,
      responseType = grpc.smartOffice.containsOrderConfirmation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.smartOffice.containsOfficeSupplies,
      grpc.smartOffice.containsOrderConfirmation> getOrderSuppliesMethod() {
    io.grpc.MethodDescriptor<grpc.smartOffice.containsOfficeSupplies, grpc.smartOffice.containsOrderConfirmation> getOrderSuppliesMethod;
    if ((getOrderSuppliesMethod = suppliesGrpc.getOrderSuppliesMethod) == null) {
      synchronized (suppliesGrpc.class) {
        if ((getOrderSuppliesMethod = suppliesGrpc.getOrderSuppliesMethod) == null) {
          suppliesGrpc.getOrderSuppliesMethod = getOrderSuppliesMethod = 
              io.grpc.MethodDescriptor.<grpc.smartOffice.containsOfficeSupplies, grpc.smartOffice.containsOrderConfirmation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "supplies", "orderSupplies"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.containsOfficeSupplies.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.containsOrderConfirmation.getDefaultInstance()))
                  .setSchemaDescriptor(new suppliesMethodDescriptorSupplier("orderSupplies"))
                  .build();
          }
        }
     }
     return getOrderSuppliesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.smartOffice.containsOfficeSupplies,
      grpc.smartOffice.orderTotal> getCalculateTotalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "calculateTotal",
      requestType = grpc.smartOffice.containsOfficeSupplies.class,
      responseType = grpc.smartOffice.orderTotal.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.smartOffice.containsOfficeSupplies,
      grpc.smartOffice.orderTotal> getCalculateTotalMethod() {
    io.grpc.MethodDescriptor<grpc.smartOffice.containsOfficeSupplies, grpc.smartOffice.orderTotal> getCalculateTotalMethod;
    if ((getCalculateTotalMethod = suppliesGrpc.getCalculateTotalMethod) == null) {
      synchronized (suppliesGrpc.class) {
        if ((getCalculateTotalMethod = suppliesGrpc.getCalculateTotalMethod) == null) {
          suppliesGrpc.getCalculateTotalMethod = getCalculateTotalMethod = 
              io.grpc.MethodDescriptor.<grpc.smartOffice.containsOfficeSupplies, grpc.smartOffice.orderTotal>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "supplies", "calculateTotal"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.containsOfficeSupplies.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.orderTotal.getDefaultInstance()))
                  .setSchemaDescriptor(new suppliesMethodDescriptorSupplier("calculateTotal"))
                  .build();
          }
        }
     }
     return getCalculateTotalMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static suppliesStub newStub(io.grpc.Channel channel) {
    return new suppliesStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static suppliesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new suppliesBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static suppliesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new suppliesFutureStub(channel);
  }

  /**
   */
  public static abstract class suppliesImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *client streaming rpc
     * </pre>
     */
    public io.grpc.stub.StreamObserver<grpc.smartOffice.containsOfficeSupplies> orderSupplies(
        io.grpc.stub.StreamObserver<grpc.smartOffice.containsOrderConfirmation> responseObserver) {
      return asyncUnimplementedStreamingCall(getOrderSuppliesMethod(), responseObserver);
    }

    /**
     * <pre>
     *bidirectional streaming for calculating total
     * </pre>
     */
    public io.grpc.stub.StreamObserver<grpc.smartOffice.containsOfficeSupplies> calculateTotal(
        io.grpc.stub.StreamObserver<grpc.smartOffice.orderTotal> responseObserver) {
      return asyncUnimplementedStreamingCall(getCalculateTotalMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getOrderSuppliesMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                grpc.smartOffice.containsOfficeSupplies,
                grpc.smartOffice.containsOrderConfirmation>(
                  this, METHODID_ORDER_SUPPLIES)))
          .addMethod(
            getCalculateTotalMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                grpc.smartOffice.containsOfficeSupplies,
                grpc.smartOffice.orderTotal>(
                  this, METHODID_CALCULATE_TOTAL)))
          .build();
    }
  }

  /**
   */
  public static final class suppliesStub extends io.grpc.stub.AbstractStub<suppliesStub> {
    private suppliesStub(io.grpc.Channel channel) {
      super(channel);
    }

    private suppliesStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected suppliesStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new suppliesStub(channel, callOptions);
    }

    /**
     * <pre>
     *client streaming rpc
     * </pre>
     */
    public io.grpc.stub.StreamObserver<grpc.smartOffice.containsOfficeSupplies> orderSupplies(
        io.grpc.stub.StreamObserver<grpc.smartOffice.containsOrderConfirmation> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getOrderSuppliesMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     *bidirectional streaming for calculating total
     * </pre>
     */
    public io.grpc.stub.StreamObserver<grpc.smartOffice.containsOfficeSupplies> calculateTotal(
        io.grpc.stub.StreamObserver<grpc.smartOffice.orderTotal> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getCalculateTotalMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class suppliesBlockingStub extends io.grpc.stub.AbstractStub<suppliesBlockingStub> {
    private suppliesBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private suppliesBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected suppliesBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new suppliesBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class suppliesFutureStub extends io.grpc.stub.AbstractStub<suppliesFutureStub> {
    private suppliesFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private suppliesFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected suppliesFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new suppliesFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_ORDER_SUPPLIES = 0;
  private static final int METHODID_CALCULATE_TOTAL = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final suppliesImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(suppliesImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ORDER_SUPPLIES:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.orderSupplies(
              (io.grpc.stub.StreamObserver<grpc.smartOffice.containsOrderConfirmation>) responseObserver);
        case METHODID_CALCULATE_TOTAL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.calculateTotal(
              (io.grpc.stub.StreamObserver<grpc.smartOffice.orderTotal>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class suppliesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    suppliesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.smartOffice.SuppliesProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("supplies");
    }
  }

  private static final class suppliesFileDescriptorSupplier
      extends suppliesBaseDescriptorSupplier {
    suppliesFileDescriptorSupplier() {}
  }

  private static final class suppliesMethodDescriptorSupplier
      extends suppliesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    suppliesMethodDescriptorSupplier(String methodName) {
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
      synchronized (suppliesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new suppliesFileDescriptorSupplier())
              .addMethod(getOrderSuppliesMethod())
              .addMethod(getCalculateTotalMethod())
              .build();
        }
      }
    }
    return result;
  }
}
