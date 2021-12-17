package grpc.smartOffice;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmdns.ServiceInfo;

import grpc.smartOffice.printerGrpc.printerBlockingStub;
import grpc.smartOffice.suppliesGrpc.suppliesStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import simpleJMDNS.SimpleServiceDiscovery;


public class Client {
	private static final Logger logger1 = Logger.getLogger(PrinterServer.class.getName());
	private static printerBlockingStub bstub;
	private static suppliesStub asyncStub;
	
	public static void main(String[] args) throws Exception {
		
		ServiceInfo serviceInfo1;
		ServiceInfo serviceInfo2;
		String service_type1 = "_printergrpc._tcp.local.";
		String service_type2 = "_suppliesgrpc._tcp.local.";
		//Now retrieve the service info - all we are supplying is the service type
		serviceInfo1 = SimpleServiceDiscovery.run(service_type1);
		serviceInfo2 = SimpleServiceDiscovery.run(service_type2);
		//Use the serviceInfo to retrieve the port
		int port1 = serviceInfo1.getPort();
		int port2 = serviceInfo2.getPort();
		String host = "localhost";
		//int port = 50051;
		
		ManagedChannel printChannel = ManagedChannelBuilder
				.forAddress(host, port1)
				.usePlaintext()
				.build();
		
		ManagedChannel suppliesChannel = ManagedChannelBuilder
				.forAddress(host, port2)
				.usePlaintext()
				.build();
		
		bstub = printerGrpc.newBlockingStub(printChannel);
		asyncStub = suppliesGrpc.newStub(suppliesChannel);
		
		getPrinterUpdate();
		print();
		orderSupplies();
		calculateTotal();
		
		try {
			printChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Error closing down printer channel");
			e.printStackTrace();
		}
		
		try {
			suppliesChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Error closing down supplies channel");
			e.printStackTrace();
		}
		
	}//close main
	
	public static void print() {
		containsPrintJob printJob = containsPrintJob.newBuilder().setContent("Hello my name is Caoimhe!").build();
		confirmationMessage confirmation = bstub.print(printJob);
		System.out.println(confirmation.getConfirmation());
	}
	
	public static void getPrinterUpdate() {
		containsRequest request = containsRequest.newBuilder().setRequest("Y").build();
		Iterator<printerStatus> statuses;
		try {
			statuses = bstub.getPrinterUpdate(request);
			for(int i = 1; statuses.hasNext(); i++) {
				printerStatus pStatus = statuses.next();
				System.out.println(pStatus.getResponseMessage());
			}
		} catch (StatusRuntimeException e) {
			logger1.log(Level.WARNING, "RPC Streaming failed: {0}", e.getStatus());
			return;
		}
	}
	
	public static void orderSupplies() {
		StreamObserver<containsOrderConfirmation> responseObserver = new StreamObserver<containsOrderConfirmation>() {
			@Override
			public void onNext(containsOrderConfirmation value) {
				System.out.println("Final Response from Supplies Server: " + value.getConfirmation());	
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}};
			
			//grpc library returns a StreamObserver to us requestObserver
			//we use this to send our outgoing messages
			StreamObserver<containsOfficeSupplies> requestObserver = asyncStub.orderSupplies(responseObserver);
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM4").setQuantity(3).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM2").setQuantity(4).build());
			System.out.println("Client has now sent its messages");
			requestObserver.onCompleted();
		
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}
	
	public static void calculateTotal() {
		System.out.println("Calculating total");
		StreamObserver<orderTotal> responseObserver = new StreamObserver<orderTotal>() {
			@Override
			public void onNext(orderTotal value) {
				System.out.println(value.getTotal());
				
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}};
			
			//grpc library returns a StreamObserver to us requestObserver
			//we use this to send our outgoing messages
			StreamObserver<containsOfficeSupplies> requestObserver = asyncStub.calculateTotal(responseObserver);
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM1").setQuantity(3).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM2").setQuantity(4).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM3").setQuantity(3).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM4").setQuantity(4).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM5").setQuantity(3).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM6").setQuantity(4).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM1").setQuantity(3).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("ITM4").setQuantity(4).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("itm3").setQuantity(3).build());
			requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId("itm2").setQuantity(4).build());
			
			//System.out.println("Client has now sent its messages");
			requestObserver.onCompleted();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}
}
