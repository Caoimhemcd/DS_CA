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
	static int staples = 200;
	static int paper = 500;
	static double inkLevels = 100;
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
	
	//unary rpc
	@Override
	public void print(containsPrintJob printJob, StreamObserver<confirmationMessage> responseObserver ) {
		int wordCount = wordCount(printJob.getContent());
		
		double characters = printJob.getContent().length(); //stored as double for division calculation later
		//average characters on a page is 1500
		//using 1500 to approximate number of pages used in a printJob
		int pagesNeeded = (int) Math.ceil(characters/1500); //pages needed for one quantity
		pagesNeeded = pagesNeeded*printJob.getQuantity(); //pages needed for requested quantity
		//average number of pages the ink cartridge can print is 300
		//calculation to reduce inkLevels by relevant percentage
		
		
		if((inkLevels - ((pagesNeeded/300.0)*100.0)) > 0 ) {
			if((paper - pagesNeeded) > 0) {
				if(printJob.getStaples().equals("Yes")) {
					if(staples - printJob.getQuantity() >= 0) {
						String msg = "Print job accepted.\nContent: \""+ printJob.getContent() + "\nWord Count: " + wordCount +"\nQuantity: " +printJob.getQuantity() + "\nStaples: Yes \nPrinting ...";
						staples = (staples - printJob.getQuantity());
						inkLevels = (inkLevels - ((pagesNeeded/300.0)*100.0));
						paper = (paper - pagesNeeded);
						confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
						responseObserver.onNext(confirmation);
					} else {
						String msg = "Not enough Staples. Refilling staples. Send print job again.";
						staples = 200; //automatically refill/reset staples
						confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
						responseObserver.onNext(confirmation);
					}
				} else {
					String msg = "Print job accepted.\nContent: \""+ printJob.getContent() + "\nWord Count: " + wordCount +"\nQuantity: " +printJob.getQuantity() + "\nStaples: No \nPrinting ...";
					inkLevels = (inkLevels - ((pagesNeeded/300.0)*100.0));
					paper = paper - pagesNeeded;
					confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
					responseObserver.onNext(confirmation);
				}
			} else {
				paper = 500; //automatically reset/refill paper
				String msg = "Not enough paper. Refilling paper. Send print job again.";
				confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
				responseObserver.onNext(confirmation);
			}
		} else {
			inkLevels = 100; //automatically refill/reset ink
			String msg = "Not enough ink. Replacing ink cartidge. Send print job again.";
			confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
			responseObserver.onNext(confirmation);
		}
		
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

	//server-streaming rpc
	@Override
	public void getPrinterUpdate(containsRequest request, StreamObserver<printerStatus> responseObserver ) {
		while(request.getRequest().equals("Y")) {
			//build the response stream message
			printerStatus.Builder status = printerStatus.newBuilder();
		    status.setResponseMessage("Activity Status: Active"); //active since server is switched on
			responseObserver.onNext(status.build());
		    
			status.setResponseMessage("Ink Levels: " + inkLevels + "%"); 
			responseObserver.onNext(status.build());
			
			status.setResponseMessage("Paper Levels: "+ checkPaper()); 
			responseObserver.onNext(status.build());
			
			status.setResponseMessage("Staples Level: "+checkStaples()); 
			responseObserver.onNext(status.build());
			
		    responseObserver.onCompleted();
		}
		
	}
	//method to return High, moderate or low depending on staple quantity left in printer
	public int checkStaples() {
		return staples;
		/*if (staples > 75) {
			return "High";
		} else if (staples > 40) {
			return "Moderate";
		} else if (staples > 0){
			return "Low";
		} else {
			return "Empty";
		}*/
	}
	
	//method to return High, moderate or low depending on paper quantity in printer
	public int checkPaper() {
		return paper;
		/*if (paper > 100) {
			return "High";
		} else if (paper > 40) {
			return "Moderate";
		} else if (paper > 0){
			return "Low";
		} else {
			return "Empty";
		}*/
	}
}
