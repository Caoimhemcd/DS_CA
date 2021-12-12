package grpc.smartOffice;

import java.io.IOException;
import java.util.logging.Logger;

import grpc.smartOffice.suppliesGrpc.suppliesImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import simpleJMDNS.SimpleServiceRegistration;

public class SuppliesServer extends suppliesImplBase{

	private static final Logger logger = Logger.getLogger(PrinterServer.class.getName());
	
	public static void main(String [] args) {
		SuppliesServer supplies = new SuppliesServer();
		
		int port = 50052;
		String service_type = "_suppliesgrpc._tcp.local.";
		String service_name = "SuppliesServer";
		SimpleServiceRegistration ssr = new SimpleServiceRegistration();
		ssr.run(port, service_type, service_name);
		
		try {
			Server server = ServerBuilder.forPort(port)
			    .addService(supplies)
			    //.addService(greeterserver1)
			    .build()
			    .start();
			System.out.println("\nSupplies Server Started");
			
			 server.awaitTermination();

			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    logger.info("Server started, listening on " + port);
		
	}
	
	@Override
	public StreamObserver<containsOfficeSupplies> orderSupplies(StreamObserver<containsOrderConfirmation> responseObserver) {
		//System.out.println("On server; inside streaming method");
		
		return new StreamObserver<containsOfficeSupplies>(){

			@Override
			public void onNext(containsOfficeSupplies value) {
				System.out.println("On server; message received from client: " + value.getQuantity() + " x " +value.getSupplyId());	
			}

			@Override
			public void onError(Throwable t) {
				
			}

			@Override
			public void onCompleted() {
				//Now build response c&p from unary method
				//builder
				containsOrderConfirmation.Builder confirmation = containsOrderConfirmation.newBuilder();
				confirmation.setConfirmation("Server for your stream message");
				responseObserver.onNext(confirmation.build());
				responseObserver.onCompleted();
			}};
	}
	
	@Override
	public StreamObserver<containsOfficeSupplies> calculateTotal(StreamObserver<orderTotal> responseObserver){
		
		return new StreamObserver<containsOfficeSupplies>() {
		int runningTotal = 0;
		@Override
		public void onNext(containsOfficeSupplies value) {
			System.out.println("On server; message received from client: " + value.getQuantity() + " x " +value.getSupplyId());	
			runningTotal += value.getQuantity()*10;
			orderTotal.Builder total = orderTotal.newBuilder();
			total.setTotal("Your total is: " +runningTotal);
			responseObserver.onNext(total.build());
			
		}

		@Override
		public void onError(Throwable t) {
			
		}

		@Override
		public void onCompleted() {
			//Now build response c&p from unary method
			//builder
			System.out.println();
			responseObserver.onCompleted();
		}};
		
	}
	
	
}

