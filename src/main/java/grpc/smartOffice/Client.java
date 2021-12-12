package grpc.smartOffice;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmdns.ServiceInfo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import simpleJMDNS.SimpleServiceDiscovery;


public class Client {
	private static final Logger logger1 = Logger.getLogger(PrinterServer.class.getName());
	private static final Logger logger2 = Logger.getLogger(SuppliesServer.class.getName());
	
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
		
	}//close main
}
