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
import simpleJMDNS.SimpleServiceDiscovery;


public class Client {
	private static final Logger logger1 = Logger.getLogger(PrinterServer.class.getName());
	private static final Logger logger2 = Logger.getLogger(SuppliesServer.class.getName());
	private static printerBlockingStub bstub;
	private static suppliesStub asyncStub;
	
	public static void main(String[] args) throws Exception {
		
		ServiceInfo serviceInfo1;
		//ServiceInfo serviceInfo2;
		String service_type1 = "_printergrpc._tcp.local.";
		//String service_type2 = "_suppliesgrpc._tcp.local.";
		//Now retrieve the service info - all we are supplying is the service type
		serviceInfo1 = SimpleServiceDiscovery.run(service_type1);
		//serviceInfo2 = SimpleServiceDiscovery.run(service_type2);
		//Use the serviceInfo to retrieve the port
		int port1 = serviceInfo1.getPort();
		//int port2 = serviceInfo2.getPort();
		String host = "localhost";
		//int port = 50051;
		
		ManagedChannel printChannel = ManagedChannelBuilder
				.forAddress(host, port1)
				.usePlaintext()
				.build();
		
		/*ManagedChannel suppliesChannel = ManagedChannelBuilder
				.forAddress(host, port2)
				.usePlaintext()
				.build();
		*/
		bstub = printerGrpc.newBlockingStub(printChannel);
		//asyncStub = suppliesGrpc.newStub(suppliesChannel);
		
		getPrinterUpdate();
		print();
		
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
	
	
}
