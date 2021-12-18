package grpc.smartOffice;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import grpc.smartOffice.printerGrpc.printerImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import simpleJMDNS.SimpleServiceRegistration;

public class PrinterServer extends printerImplBase{
	
	private static final DecimalFormat df = new DecimalFormat("#.##");
	private static final Logger logger = Logger.getLogger(PrinterServer.class.getName());
	int staples = 200;
	int paper = 500;
	double inkLevels = 100;
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
		//average characters on a page is assumed to be 1800
		//using 1800 to approximate number of pages used in a printJob
		int pagesNeeded = (int) Math.ceil(characters/1800); //pages needed for one quantity
		pagesNeeded = pagesNeeded*printJob.getQuantity(); //pages needed for requested quantity
		
		//Approximate average number of pages the ink cartridge can print is 300
		//calculation to reduce inkLevels by relevant percentage
		if((inkLevels - ((pagesNeeded/300.0)*100.0)) > 0 ) {
			//enter here if there is enough ink
			if((paper - pagesNeeded) > 0) {
				//enough paper in printer
				if(printJob.getStaples().equals("Yes")) {
					//client wants staples
					if(staples - printJob.getQuantity() >= 0) {
						//enough staples (each copy of printJob gets stapled
						String msg = "Print job accepted.\nContent: "+ printJob.getContent() + "\nWord Count: " + wordCount +"\nQuantity: " +printJob.getQuantity() + "\nStaples: Yes \nPrinting ...";
						staples = (staples - printJob.getQuantity()); //reduce staples level by used staples
						inkLevels = (inkLevels - ((pagesNeeded/300.0)*100.0)); //reduce ink levels percentage by used ink
						paper = (paper - pagesNeeded); //reduce paper by pagesNeeded
						//build confirmation message
						confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
						responseObserver.onNext(confirmation);
					} else {
						//not enough staples
						String msg = "Not enough Staples. Refilling staples. Send print job again.";
						staples = 200; //automatically refill/reset staples
						//build confirmation message
						confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
						responseObserver.onNext(confirmation);
					}
				} else {
					//client does not need staples so no need to check staples
					String msg = "Print job accepted.\nContent: \""+ printJob.getContent() + "\nWord Count: " + wordCount +"\nQuantity: " +printJob.getQuantity() + "\nStaples: No \nPrinting ...";
					inkLevels = (inkLevels - ((pagesNeeded/300.0)*100.0)); //reduce ink levels percentage by used ink
					paper = paper - pagesNeeded; //reduce paper by pagesNeeded
					//build confirmation message
					confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
					responseObserver.onNext(confirmation);
				}
			} else {
				//not enough paper in printer
				paper = 500; //automatically reset/refill paper
				String msg = "Not enough paper. Refilling paper. Send print job again.";
				//build confirmation message
				confirmationMessage confirmation = confirmationMessage.newBuilder().setConfirmation(msg).build();
				responseObserver.onNext(confirmation);
			}
		} else {
			//not enough ink
			inkLevels = 100.0; //automatically refill/reset ink
			String msg = "Not enough ink. Replacing ink cartidge. Send print job again.";
			//build confirmation message
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
		    
			status.setResponseMessage("Ink Levels: " + df.format(inkLevels) + "%"); 
			responseObserver.onNext(status.build());
			
			status.setResponseMessage("Paper Levels: " + paper); 
			responseObserver.onNext(status.build());
			
			status.setResponseMessage("Staples Level: " + staples); 
			responseObserver.onNext(status.build());
			
		    responseObserver.onCompleted();
		}
	}
}
