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
	int staples = 200;
	int paper = 500;
	int inkLevels = 100;
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
		int characters = printJob.getContent().length();
		//average characters on a page is 1800
		//using 1800 to approximate number of pages used in a printJob
		int pagesNeeded = characters/1800;
		//average number of pages the ink cartridge can print is 600
		//calculation to reduce inkLevels by relevant percentage
		inkLevels = inkLevels - ((pagesNeeded/600)*100);
		staples = staples - printJob.getQuantity();
		if(inkLevels > 0 ) {
			if(paper - pagesNeeded > 0) {
				if(printJob.getStaples().equals("Yes")) {
					if(staples >= 0) {
						String msg = "Print job accepted.\nContent: \""+ printJob.getContent() + "\nWord Count: " + wordCount +"\nQuantity: " +printJob.getQuantity() + "\nStaples: Yes \nPrinting ...";
						staples = staples - printJob.getQuantity();
						confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
						paper = paper - pagesNeeded;
						responseObserver.onNext(confirmation);
					} else {
						String msg = "Not enough Staples. Refilling staples. Send print job again.";
						confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
						responseObserver.onNext(confirmation);
					}
				} else {
					String msg = "Print job accepted.\nContent: \""+ printJob.getContent() + "\nWord Count: " + wordCount +"\nQuantity: " +printJob.getQuantity() + "\nStaples: No \nPrinting ...";
					confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
					responseObserver.onNext(confirmation);
					paper = paper - pagesNeeded;
				}
			} else {
				paper = 500;
				String msg = "Not enough paper. Refilling paper. Send print job again.";
				confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
				responseObserver.onNext(confirmation);
			}
		} else {
			inkLevels = 100;
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

	@Override
	public void getPrinterUpdate(containsRequest request, StreamObserver<printerStatus> responseObserver ) {
		//build the response stream message
		printerStatus.Builder status = printerStatus.newBuilder();
	    status.setResponseMessage("Activity Status: Active"); //active since server is switched on
		responseObserver.onNext(status.build());
	    
		status.setResponseMessage("Ink Levels: " +inkLevels+ "%"); 
		responseObserver.onNext(status.build());
		
		status.setResponseMessage("Paper Levels: "+ checkPaper()); 
		responseObserver.onNext(status.build());
		
		status.setResponseMessage("Staples Level: "+checkStaples()); 
		responseObserver.onNext(status.build());
		
	    responseObserver.onCompleted();
		
	}
	//method to return High, moderate or low depending on staple quantity left in printer
	public String checkStaples() {
		if (staples > 75) {
			return "High";
		} else if (staples > 40) {
			return "Moderate";
		} else if (staples > 0){
			return "Low";
		} else {
			return "Empty";
		}
	}
	
	//method to return High, moderate or low depending on paper quantity in printer
	public String checkPaper() {
		if (paper > 100) {
			return "High";
		} else if (paper > 40) {
			return "Moderate";
		} else if (paper > 0){
			return "Low";
		} else {
			return "Empty";
		}
	}
}
