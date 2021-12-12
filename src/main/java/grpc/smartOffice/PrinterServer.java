package grpc.smartOffice;

import java.io.IOException;
import java.util.logging.Logger;

import grpc.smartOffice.printerGrpc.printerImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import simpleJMDNS.SimpleServiceRegistration;

public class PrinterServer extends printerImplBase{
	
	private static final Logger logger = Logger.getLogger(PrinterServer.class.getName());
	
	public static void main(String [] args) {
		PrinterServer printerUpdate = new PrinterServer();
		
		int port = 50051;
		String service_type = "_printergrpc._tcp.local.";
		String service_name = "PrinterServer";
		SimpleServiceRegistration ssr = new SimpleServiceRegistration();
		ssr.run(port, service_type, service_name);
		
		try {
			Server server = ServerBuilder.forPort(port)
			    .addService(printerUpdate)
			    //.addService(greeterserver1)
			    .build()
			    .start();
			System.out.println("\nPrinter Server Started");
			
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
	public void print(containsPrintJob printJob, StreamObserver<confirmationMessage> responseObserver ) {
		int wordCount = wordCount(printJob.getContent());
		String msg = "Print job accepted. Word Count: " + wordCount;
		confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
	     
		responseObserver.onNext(confirmation);
	     
	    responseObserver.onCompleted();
		
	}
	
	//word count method
	public static int wordCount(String str) {
		if (str == null || str.isEmpty()) {
			return 0;
		} 
		
		String[] wordArray = str.split("\\s+");
		return wordArray.length;
	}

	@Override
	public void getPrinterUpdate(containsRequest request, StreamObserver<printerStatus> responseObserver ) {
		//build the response stream message
		printerStatus.Builder status = printerStatus.newBuilder();
	    status.setResponseMessage("Activity Status: busy"); 
		responseObserver.onNext(status.build());
	    
		status.setResponseMessage("Ink Levels: Low"); 
		responseObserver.onNext(status.build());
		
		status.setResponseMessage("Paper Levels: High"); 
		responseObserver.onNext(status.build());
		
		status.setResponseMessage("Staples Level: High"); 
		responseObserver.onNext(status.build());
		
	    responseObserver.onCompleted();
		
	}
	
}
