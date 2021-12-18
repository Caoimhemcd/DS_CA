package grpc.smartOffice;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import grpc.smartOffice.suppliesGrpc.suppliesImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import simpleJMDNS.SimpleServiceRegistration;

public class SuppliesServer extends suppliesImplBase{
	
	private boolean itemError = false;
	private static boolean itemError1 = false;

	private static final Logger logger = Logger.getLogger(PrinterServer.class.getName());

	private static HashMap<String, Double> items = new HashMap<String, Double>();
	
	public static void main(String [] args) {

		items.put("ITM1", 12.5);
		items.put("ITM2", 10.5);
		items.put("ITM3", 1.6);
		items.put("ITM4", 18.99);
		items.put("ITM5", 9.75);
		items.put("ITM6", 0.99);
		
		SuppliesServer supplies = new SuppliesServer();
		
		int port = 50052;
		String service_type = "_suppliesgrpc._tcp.local.";
		String service_name = "SuppliesServer";
		SimpleServiceRegistration ssr = new SimpleServiceRegistration();
		ssr.run(port, service_type, service_name);
		
		try {
			Server server = ServerBuilder.forPort(port)
			    .addService(supplies)
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
	
	//client-streaming rpc
	@Override
	public StreamObserver<containsOfficeSupplies> orderSupplies(StreamObserver<containsOrderConfirmation> responseObserver) {
		//System.out.println("On server; inside streaming method");
		return new StreamObserver<containsOfficeSupplies>(){
			int runningTotal = 0;
			String itemDetails ="";
			@Override
			public void onNext(containsOfficeSupplies value) {
				//check that quantity inputed is 0 or more	
				if(value.getQuantity() >= 0) {
					//
					if(items.containsKey(value.getSupplyId().toUpperCase())) {
						//if item inputed has a valid item code
						//add the cost for that quantity of the order item to the runningTotal
						runningTotal += value.getQuantity()*items.get(value.getSupplyId().toUpperCase());
						itemDetails += "Item: " + value.getQuantity() + " x " +value.getSupplyId() +"\n";
						System.out.println("Item: " + value.getQuantity() + " x " +value.getSupplyId()); //print out in console 
					} else {
						//item not from item valid item HashMap
						itemError = true;
						System.out.println("Item with code " + value.getSupplyId() + "does not exist.\n Please choose from ITM1, ITM2, ITM3, ITM4, ITM5, ITM6"); //print out on console
					}
				}
			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onCompleted() 
			{
				//Now build response
				containsOrderConfirmation.Builder confirmation = containsOrderConfirmation.newBuilder();
				if(itemError){
					//an item was entered that was incorrect - user asked to start again but input correct item codes
					confirmation.setConfirmation("\nInputted Item Code Not Found.\nPlease choose from ITM1, ITM2, ITM3, ITM4, ITM5, ITM6.\nResend order with correct items");
					itemError = false; //reset to false
				}else{
					//send back confirmation message with total
					confirmation.setConfirmation(itemDetails + "\nOrder Confirmed\nTotal: \u20ac"+runningTotal);
				}
				responseObserver.onNext(confirmation.build());
				responseObserver.onCompleted();
			}};
	}
	
	//bidirectional rpc
	@Override
	public StreamObserver<containsOfficeSupplies> calculateTotal(StreamObserver<orderTotal> responseObserver){
		
		return new StreamObserver<containsOfficeSupplies>() {
		int runningTotal = 0;
		@Override
		public void onNext(containsOfficeSupplies value) {
			System.out.println("On server; message received from client: " + value.getQuantity() + " x " +value.getSupplyId());	
			if(value.getQuantity() >= 0) {
				//valid quantity inputed
				//build response message with item code and quantity sent back
				orderTotal.Builder total = orderTotal.newBuilder();
				total.setTotal("Item: " + value.getQuantity() + " x " +value.getSupplyId() + "\n");
				responseObserver.onNext(total.build());
				if(items.containsKey(value.getSupplyId().toUpperCase())) {
					//if item inputed has a valid item code
					//add the cost for that quantity of the order item to the runningTotal
					runningTotal += value.getQuantity()*items.get(value.getSupplyId().toUpperCase());
					System.out.println("Item: " + value.getQuantity() + " x " +value.getSupplyId()); //print out in console 
				} else {
					//item code is not valid
					itemError1 = true;
					System.out.println("Item with code " + value.getSupplyId() + "does not exist.\n"); //message on console
				}
			}
		}

		@Override
		public void onError(Throwable t) {
		}

		@Override
		public void onCompleted() {
			//Now build response
			orderTotal.Builder total = orderTotal.newBuilder();
			if(itemError1){
				//an item was entered that was incorrect - user asked to start again but input correct item codes
				total.setTotal("\nInputted Item Code Not Found.\nPlease choose from ITM1, ITM2, ITM3, ITM4, ITM5, ITM6.\nRe-enter items with correct codes.");
				itemError1 = false; //reset to false
			} else {
				//send back final response of total price
				total.setTotal("Total: \u20ac" +runningTotal);
			}
			responseObserver.onNext(total.build());
			responseObserver.onCompleted();
		}};
		
	}
	
}

