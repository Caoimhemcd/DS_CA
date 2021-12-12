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
    comments = "Source: printer.proto")
public final class printerGrpc {

  private printerGrpc() {}

  public static final String SERVICE_NAME = "printer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.smartOffice.containsPrintJob,
      grpc.smartOffice.confirmationMessage> getPrintMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "print",
      requestType = grpc.smartOffice.containsPrintJob.class,
      responseType = grpc.smartOffice.confirmationMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.smartOffice.containsPrintJob,
      grpc.smartOffice.confirmationMessage> getPrintMethod() {
    io.grpc.MethodDescriptor<grpc.smartOffice.containsPrintJob, grpc.smartOffice.confirmationMessage> getPrintMethod;
    if ((getPrintMethod = printerGrpc.getPrintMethod) == null) {
      synchronized (printerGrpc.class) {
        if ((getPrintMethod = printerGrpc.getPrintMethod) == null) {
          printerGrpc.getPrintMethod = getPrintMethod = 
              io.grpc.MethodDescriptor.<grpc.smartOffice.containsPrintJob, grpc.smartOffice.confirmationMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "printer", "print"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.containsPrintJob.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.confirmationMessage.getDefaultInstance()))
                  .setSchemaDescriptor(new printerMethodDescriptorSupplier("print"))
                  .build();
          }
        }
     }
     return getPrintMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.smartOffice.containsRequest,
      grpc.smartOffice.printerStatus> getGetPrinterUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getPrinterUpdate",
      requestType = grpc.smartOffice.containsRequest.class,
      responseType = grpc.smartOffice.printerStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.smartOffice.containsRequest,
      grpc.smartOffice.printerStatus> getGetPrinterUpdateMethod() {
    io.grpc.MethodDescriptor<grpc.smartOffice.containsRequest, grpc.smartOffice.printerStatus> getGetPrinterUpdateMethod;
    if ((getGetPrinterUpdateMethod = printerGrpc.getGetPrinterUpdateMethod) == null) {
      synchronized (printerGrpc.class) {
        if ((getGetPrinterUpdateMethod = printerGrpc.getGetPrinterUpdateMethod) == null) {
          printerGrpc.getGetPrinterUpdateMethod = getGetPrinterUpdateMethod = 
              io.grpc.MethodDescriptor.<grpc.smartOffice.containsRequest, grpc.smartOffice.printerStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "printer", "getPrinterUpdate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.containsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.smartOffice.printerStatus.getDefaultInstance()))
                  .setSchemaDescriptor(new printerMethodDescriptorSupplier("getPrinterUpdate"))
                  .build();
          }
        }
     }
     return getGetPrinterUpdateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static printerStub newStub(io.grpc.Channel channel) {
    return new printerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static printerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new printerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static printerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new printerFutureStub(channel);
  }

  /**
   */
  public static abstract class printerImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *unary rpc
     * </pre>
     */
    public void print(grpc.smartOffice.containsPrintJob request,
        io.grpc.stub.StreamObserver<grpc.smartOffice.confirmationMessage> responseObserver) {
      asyncUnimplementedUnaryCall(getPrintMethod(), responseObserver);
    }

    /**
     * <pre>
     *server-side streaming rpc
     * </pre>
     */
    public void getPrinterUpdate(grpc.smartOffice.containsRequest request,
        io.grpc.stub.StreamObserver<grpc.smartOffice.printerStatus> responseObserver) {
      asyncUnimplementedUnaryCall(getGetPrinterUpdateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPrintMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.smartOffice.containsPrintJob,
                grpc.smartOffice.confirmationMessage>(
                  this, METHODID_PRINT)))
          .addMethod(
            getGetPrinterUpdateMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                grpc.smartOffice.containsRequest,
                grpc.smartOffice.printerStatus>(
                  this, METHODID_GET_PRINTER_UPDATE)))
          .build();
    }
  }

  /**
   */
  public static final class printerStub extends io.grpc.stub.AbstractStub<printerStub> {
    private printerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private printerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected printerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new printerStub(channel, callOptions);
    }

    /**
     * <pre>
     *unary rpc
     * </pre>
     */
    public void print(grpc.smartOffice.containsPrintJob request,
        io.grpc.stub.StreamObserver<grpc.smartOffice.confirmationMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPrintMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *server-side streaming rpc
     * </pre>
     */
    public void getPrinterUpdate(grpc.smartOffice.containsRequest request,
        io.grpc.stub.StreamObserver<grpc.smartOffice.printerStatus> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetPrinterUpdateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class printerBlockingStub extends io.grpc.stub.AbstractStub<printerBlockingStub> {
    private printerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private printerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected printerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new printerBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *unary rpc
     * </pre>
     */
    public grpc.smartOffice.confirmationMessage print(grpc.smartOffice.containsPrintJob request) {
      return blockingUnaryCall(
          getChannel(), getPrintMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     *server-side streaming rpc
     * </pre>
     */
    public java.util.Iterator<grpc.smartOffice.printerStatus> getPrinterUpdate(
        grpc.smartOffice.containsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetPrinterUpdateMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class printerFutureStub extends io.grpc.stub.AbstractStub<printerFutureStub> {
    private printerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private printerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected printerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new printerFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *unary rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.smartOffice.confirmationMessage> print(
        grpc.smartOffice.containsPrintJob request) {
      return futureUnaryCall(
          getChannel().newCall(getPrintMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PRINT = 0;
  private static final int METHODID_GET_PRINTER_UPDATE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final printerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(printerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PRINT:
          serviceImpl.print((grpc.smartOffice.containsPrintJob) request,
              (io.grpc.stub.StreamObserver<grpc.smartOffice.confirmationMessage>) responseObserver);
          break;
        case METHODID_GET_PRINTER_UPDATE:
          serviceImpl.getPrinterUpdate((grpc.smartOffice.containsRequest) request,
              (io.grpc.stub.StreamObserver<grpc.smartOffice.printerStatus>) responseObserver);
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

  private static abstract class printerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    printerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.smartOffice.PrinterProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("printer");
    }
  }

  private static final class printerFileDescriptorSupplier
      extends printerBaseDescriptorSupplier {
    printerFileDescriptorSupplier() {}
  }

  private static final class printerMethodDescriptorSupplier
      extends printerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    printerMethodDescriptorSupplier(String methodName) {
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
      synchronized (printerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new printerFileDescriptorSupplier())
              .addMethod(getPrintMethod())
              .addMethod(getGetPrinterUpdateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
