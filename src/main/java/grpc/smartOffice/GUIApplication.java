package grpc.smartOffice;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmdns.ServiceInfo;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import grpc.smartOffice.printerGrpc.printerBlockingStub;
import grpc.smartOffice.suppliesGrpc.suppliesStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import simpleJMDNS.SimpleServiceDiscovery;

public class GUIApplication implements ItemListener {
	private static printerBlockingStub bstub;
	private static suppliesStub asyncStub;
	private static final Logger logger1 = Logger.getLogger(PrinterServer.class.getName());
	
	private ServiceInfo serviceInfo1;
	private ServiceInfo serviceInfo2;
	
	private JFrame frame;
	JPanel cards; //a panel using CardLayout
	private JTextArea textName1;
	private JTextArea textResponse, textResponse2, textResponse3, textResponse4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			//overide the run method of runnable
			public void run() {
				try {
					GUIApplication window = new GUIApplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		 /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
           // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	public GUIApplication() {

		String service_type1 = "_printergrpc._tcp.local.";
		String service_type2 = "_suppliesgrpc._tcp.local.";
		//Now retrieve the service info - all we are supplying is the service type
		serviceInfo1 = SimpleServiceDiscovery.run(service_type1);
		serviceInfo2 = SimpleServiceDiscovery.run(service_type2);
		//Use the serviceInfo to retrieve the port
		int port1 = serviceInfo1.getPort();
		int port2 = serviceInfo2.getPort();
		String host = "localhost";
		
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
	}
	
    public void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); 
        String[] ops = new String[] {"Print Out", "Printer Status", "Calculate Order Total", "Send Order"};
		//Create combobox and add to panel with service method options
        JComboBox cb = new JComboBox(ops);
        cb.setModel(new DefaultComboBoxModel(ops));
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
        
        //Create the "cards".
        //Printer service - print method card
        JPanel card1 = new JPanel();
        card1.setLayout(new GridLayout(2, 3, 10, 0));
        card1.add(new JLabel("Text to Print: "));
        card1.add(new JLabel("Quantity: "));
        JButton btnPrint = new JButton("Print");
        card1.add(btnPrint);
        JTextArea printBox = new JTextArea(6, 30); 
        card1.add(new JScrollPane(printBox));
		printBox.setLineWrap(true);
		printBox.setWrapStyleWord(true);
        JTextField printQuantityBox = new JTextField("1",5);
        card1.add(printQuantityBox);
        textResponse = new JTextArea(6, 30);
		textResponse .setLineWrap(true);
		textResponse.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textResponse);
		card1.add(scrollPane);
        
        /*JPanel card1 = new JPanel();
        card1.setLayout(new FlowLayout());
        card1.add(new JLabel("Text to Print: "));
        card1.add(new JLabel("Quantity: "));
        String[] options = new String[] {"Yes", "No"};
		//Create combobox and add to panel with service method options
        JComboBox cbStaples = new JComboBox(options);
        cbStaples.setModel(new DefaultComboBoxModel(options));
        cbStaples.setEditable(false);
        cbStaples.addItemListener(this);
        card1.add(cbStaples);
        JButton btnPrint = new JButton("Print");
        card1.add(btnPrint);
        JTextArea printBox = new JTextArea(6, 30); 
        card1.add(new JScrollPane(printBox));
		printBox.setLineWrap(true);
		printBox.setWrapStyleWord(true);
        JTextField printQuantityBox = new JTextField("1",5);
        card1.add(printQuantityBox);
        textResponse = new JTextArea(6, 30);
		textResponse .setLineWrap(true);
		textResponse.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textResponse);
		card1.add(scrollPane);
        */
        btnPrint.addActionListener(new ActionListener() {	
			//implement action performed method when print button clicked
			public void actionPerformed(ActionEvent e) {
				//Retrieve data from GUI
				String printJobContent = printBox.getText();
				int printQuantity = Integer.parseInt(printQuantityBox.getText());
				//String staples = (String) cbStaples.getSelectedItem();
				//Call the server from inside the button code
				//unary rpc
				containsPrintJob printJob = containsPrintJob.newBuilder().setContent(printJobContent).setQuantity(printQuantity).setStaples("No").build();
				confirmationMessage confirmation = bstub.print(printJob);
				System.out.println(confirmation.getConfirmation());				
				//populate the JTextArea in the panel
				textResponse.append(confirmation.getConfirmation() + "\n");				
			}
		}); //End of setup button
        
        //Printer service - status card
        JPanel card2 = new JPanel();
        card2.setLayout(new GridLayout(2,1));
        JButton btnPrintUpdate = new JButton("Request Printer Status");
        card2.add(btnPrintUpdate);
        textResponse2 = new JTextArea(6,30);
        card2.add(textResponse2);
        
        btnPrintUpdate.addActionListener(new ActionListener() {	
			//This will happen when the request status button is clicked
			public void actionPerformed(ActionEvent e) {
				containsRequest request = containsRequest.newBuilder().setRequest("Y").build();
				//Retrieve data from GUI				

				//Call the server from inside the button code
				//server-streaming rpc
				Iterator<printerStatus> statuses;
				try {
					statuses = bstub.getPrinterUpdate(request);
					for(int i = 1; statuses.hasNext(); i++) {
						printerStatus pStatus = statuses.next();
						System.out.println(pStatus.getResponseMessage());
						textResponse2.append(pStatus.getResponseMessage() + "\n");
					}
				} catch (StatusRuntimeException e1) {
					logger1.log(Level.WARNING, "RPC Streaming failed: {0}", e1.getStatus());
					return;
				}								
			}
		}); //End of setup button
        
        //Supplies service - calculate total cost of order method card
        JPanel card3 = new JPanel();
        card3.add(new JLabel("Item Code: "));
        JTextField codeBox = new JTextField(15);
        card3.add(codeBox);
        card3.add(new JLabel("Quantity: "));
        JTextField quantityBox = new JTextField("1", 5);
        card3.add(quantityBox);
        JButton btnCalculateOrderTotal = new JButton("Add Item to Total");
        card3.add(btnCalculateOrderTotal);
        JButton btnCalculateOrderTotalFinal = new JButton("Calculate Total");
        card3.add(btnCalculateOrderTotalFinal);
        textResponse3 = new JTextArea(4,30);
        card3.add(textResponse3);
        ArrayList<String> codes = new ArrayList<String>();
		ArrayList<Integer> quantities = new ArrayList<Integer>();
        
		btnCalculateOrderTotal.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					quantities.add(Integer.parseInt(quantityBox.getText()));
					codes.add(codeBox.getText());
				}
	        });
		
        btnCalculateOrderTotalFinal.addActionListener(new ActionListener() {	
			//implement action performed method
			//This will happen when the button is clicked
			public void actionPerformed(ActionEvent e) {
				System.out.println("Supplies Order");
				
				StreamObserver<orderTotal> responseObserver = new StreamObserver<orderTotal>() {
					@Override
					public void onNext(orderTotal value) {
						System.out.println(value.getTotal());
						textResponse3.append(value.getTotal());
					}
					@Override
					public void onError(Throwable t) {	
					}
					@Override
					public void onCompleted() {			
					}};
					//bidirectional streaming rpc
					//grpc library returns a StreamObserver to us requestObserver
					//we use this to send our outgoing messages
					StreamObserver<containsOfficeSupplies> requestObserver = asyncStub.calculateTotal(responseObserver);
					for (int i = 0; i< codes.size(); i++) {
						requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId(codes.get(i)).setQuantity(quantities.get(i)).build());
					}
					requestObserver.onCompleted();
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
			}
		}); //End of setup button
        
        //Supplies service - method to order items card
        JPanel card4 = new JPanel();  
        card4.add(new JLabel("Item Code: "));
        JTextField codeBox1 = new JTextField(15);
        card4.add(codeBox1);
        card4.add(new JLabel("Quantity: "));
        JTextField quantityBox1 = new JTextField("1", 5);
        card4.add(quantityBox1);
        JButton btnAddToOrder = new JButton("Add to Order");
        card4.add(btnAddToOrder);
        JButton btnOrderSupplies = new JButton("Complete Order");
        card4.add(btnOrderSupplies);
        textResponse4 = new JTextArea(4,30);
        card4.add(textResponse4);
        ArrayList<String> codes1 = new ArrayList<String>();
		ArrayList<Integer> quantities1 = new ArrayList<Integer>();
        
        btnAddToOrder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				quantities1.add(Integer.parseInt(quantityBox.getText()));
				codes1.add(codeBox.getText());
				
			}
        });
        
        btnOrderSupplies.addActionListener(new ActionListener() {	
			//implement action performed method
			//This will happen when the button is clicked
			public void actionPerformed(ActionEvent e) {
				System.out.println("Supplies Order");
				StreamObserver<containsOrderConfirmation> responseObserver = new StreamObserver<containsOrderConfirmation>() {
					@Override
					public void onNext(containsOrderConfirmation value) {
						System.out.println("Final Response from Supplies Server: " + value.getConfirmation());
						textResponse4.append(value.getConfirmation());
					}

					@Override
					public void onError(Throwable t) {
						
					}

					@Override
					public void onCompleted() {
						// TODO Auto-generated method stub
					}};
					
					//client-side streaming
					//grpc library returns a StreamObserver to us requestObserver
					//we use this to send our outgoing messages
					StreamObserver<containsOfficeSupplies> requestObserver = asyncStub.orderSupplies(responseObserver);
					for (int i = 0; i< codes1.size(); i++) {
						requestObserver.onNext(containsOfficeSupplies.newBuilder().setSupplyId(codes1.get(i)).setQuantity(quantities1.get(i)).build());
					}
					System.out.println("Client has now sent its messages");
					requestObserver.onCompleted();
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		}); //End of setup button
        
        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card1, "Print Out");
        cards.add(card2, "Printer Status");
        cards.add(card3, "Calculate Order Total");
        cards.add(card4, "Send Order");
        
        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }
    
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }
    
	/**
	 * Create the GUI and show it.
	 */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SmartOfficeServices");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        GUIApplication demo = new GUIApplication();
        demo.addComponentToPane(frame.getContentPane());
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

}
