package grpc.smartOffice;

import java.io.IOException;
import java.util.logging.Logger;

import grpc.smartOffice.suppliesGrpc.suppliesImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import simpleJMDNS.SimpleServiceRegistration;

public class SuppliesServer extends suppliesImplBase{

	private static final Logger logger = Logger.getLogger(PrinterServer.class.getName());
	
	public static void main(String [] args) {
		PrinterServer printerUpdate = new PrinterServer();
		
		int port = 50052;
		String service_type = "_suppliesgrpc._tcp.local.";
		String service_name = "SuppliesServer";
		SimpleServiceRegistration ssr = new SimpleServiceRegistration();
		ssr.run(port, service_type, service_name);
		
		try {
			Server server = ServerBuilder.forPort(port)
			    .addService(printerUpdate)
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

}

