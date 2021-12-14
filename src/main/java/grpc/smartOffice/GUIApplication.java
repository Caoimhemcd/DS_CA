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
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
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
	private JTextArea textName1;
	//private JTextField textName2;
	private JTextArea textResponse, textResponse2, textResponse3;

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
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
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
		
		//ServiceInfo serviceInfo2;
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

		bstub = printerGrpc.newBlockingStub(printChannel);
		asyncStub = suppliesGrpc.newStub(suppliesChannel);
		System.out.println("Have gotten to initialise");
		//initialize(); 
		System.out.println("Have gotten after initialise");
		//getPrinterUpdate();
		//print();
		//orderSupplies();
		//calculateTotal();
		/*
		try {
			printChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Error closing down printer channel");
			e.printStackTrace();
		}*/
		
		/*try {
			suppliesChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Error closing down supplies channel");
			e.printStackTrace();
		}*/
		
	}
	
	JPanel cards; //a panel that uses CardLayout
    
    
	    public void addComponentToPane(Container pane) {
	        //Put the JComboBox in a JPanel to get a nicer look.
	        JPanel comboBoxPane = new JPanel(); //use FlowLayout
	        String[] ops = new String[] {"Print Out", "Printer Status", "Calculate Order Total", "Send Order"};
			//Create combobox and add to panel
	        
	        JComboBox cb = new JComboBox(ops);
	        cb.setEditable(false);
	        cb.addItemListener(this);
	        comboBoxPane.add(cb);
	        
	        //Create the "cards".
	        JPanel card1 = new JPanel();
	        card1.setLayout(new GridLayout(2,3, 10, 0));
	        card1.add(new JLabel("Text to Print: "));
	        card1.add(new JLabel("Quantity: "));
	        JButton btnPrint = new JButton("Print");
	        card1.add(btnPrint);
	        JTextArea printBox = new JTextArea(6, 30); 
	        card1.add(new JScrollPane(printBox));
	        JTextField printQuantityBox = new JTextField("1",5);
	        card1.add(printQuantityBox);
	        
	        btnPrint.addActionListener(new ActionListener() {	
				//implement action performed method
				//This will happen when the button is clicked
				public void actionPerformed(ActionEvent e) {
					//Retrieve data from GUI
					String printJobContent = printBox.getText();
					int printQuantity = Integer.parseInt(printQuantityBox.getText());
					
					//* This is the only gRPC in this method*/
					// we need to call the server from inside the button code
					containsPrintJob printJob = containsPrintJob.newBuilder().setContent(printJobContent).setQuantity(printQuantity).build();
					confirmationMessage confirmation = bstub.print(printJob);
					System.out.println(confirmation.getConfirmation());				
					//populate the JTextArea in the panel
					textResponse.append("reply:"+ confirmation.getConfirmation() +"\n");				
					System.out.println("res: " + confirmation.getConfirmation());	
				}
			}); //End of setup button
	        
	        textResponse = new JTextArea(6, 30);
			//textResponse .setLineWrap(true);
			//textResponse.setWrapStyleWord(true);
			JScrollPane scrollPane = new JScrollPane(textResponse);
			//textResponse.setSize(new Dimension(15, 30));
			card1.add(scrollPane);
	        
	        JPanel card2 = new JPanel();
	        card2.setLayout(new GridLayout(2,1));
	        JButton btnPrintUpdate = new JButton("Request Printer Status");
	        card2.add(btnPrintUpdate);
	        textResponse2 = new JTextArea(6,30);
	        card2.add(textResponse2);
	        
	        btnPrintUpdate.addActionListener(new ActionListener() {	
				//implement action performed method
				//This will happen when the button is clicked
				public void actionPerformed(ActionEvent e) {
					containsRequest request = containsRequest.newBuilder().setRequest("Y").build();
					//Retrieve data from GUI				
					//* This is the only gRPC in this method*/
					// we need to call the server from inside the button code
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
					//populate the JTextArea in the panel
					//textResponse.append("reply:"+ confirmation.getConfirmation() +"\n");				
					//System.out.println("res: " + confirmation.getConfirmation());
				}
			}); //End of setup button
	        
	        JPanel card3 = new JPanel();
	        card3.add(new JLabel("Item Code: "));
	        JTextField codeBox = new JTextField(15);
	        card3.add(codeBox);
	        card3.add(new JLabel("Quantity: "));
	        JTextField quantityBox = new JTextField("1", 5);
	        card3.add(quantityBox);
	        JButton btnCalculateOrderTotal = new JButton("Add item to calculated total");
	        card3.add(btnCalculateOrderTotal);
	        textResponse3 = new JTextArea(4,20);
	        card3.add(textResponse3);
	        
	        btnCalculateOrderTotal.addActionListener(new ActionListener() {	
				//implement action performed method
				//This will happen when the button is clicked
				public void actionPerformed(ActionEvent e) {
					System.out.println("Supplies Order");
					String code = codeBox.getText();
					int quantity = Integer.parseInt(quantityBox.getText());
					StreamObserver<orderTotal> responseObserver = new StreamObserver<orderTotal>() {
						@Override
						public void onNext(orderTotal value) {
							System.out.println(value.getTotal());
							
						}

						@Override
						public void onError(Throwable t) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onCompleted() {
							// TODO Auto-generated method stub
							
						}};
						
						//grpc library returns a StreamObserver to us requestObserver
						//we use this to send our outgoing messages
						StreamObserver<containsOfficeSupplies> requestObserver = asyncStub.calculateTotal(responseObserver);
						containsOfficeSupplies item = containsOfficeSupplies.newBuilder().setSupplyId(code).setQuantity(quantity).build();
						requestObserver.onNext(item);
						//textResponse3.append(.getTotal());
						//System.out.println("Client has now sent its messages");
						requestObserver.onCompleted();
						
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					//Retrieve data from GUI				
					//* This is the only gRPC in this method*/
									
					//populate the JTextArea in the panel
					//textResponse.append("reply:"+ confirmation.getConfirmation() +"\n");				
					//System.out.println("res: " + confirmation.getConfirmation());
				}
			}); //End of setup button
	        
	        JPanel card4 = new JPanel();
	        card4.add(new JLabel("Item Code: "));
	        card4.add(new JTextField(15));
	        card4.add(new JLabel("Quantity: "));
	        card4.add(new JTextField("1", 5));
	        card4.add(new JTextField(15));
	        
	        
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
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event dispatch thread.
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
	
	
	private void initialize() {
		System.out.println("Inside initialize");
		frame = new JFrame();
		//Jframe is a window with a title bar
		frame.setTitle("Client - Service Controller");
		//set bounds can be done for frames panels and buttons
		frame.setBounds(50, 50, 1100, 600);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Box layout determines how components are laid out in the panel
		//Layout vertically in a col - box layout doesn't wrap
		//See: https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
		BoxLayout bl = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
		
		frame.getContentPane().setLayout(bl);
		
		//Create JPanel
		JPanel panel_service_1 = new JPanel();
		frame.getContentPane().add(panel_service_1);
		//Flow layout - items retain their size, are laid out horizontally and wrap

		panel_service_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		//Create new label and add it to the panel
		JLabel lblNewLabel_1 = new JLabel("Printer Service");
		panel_service_1.add(lblNewLabel_1);
		
		//Input textbox
		textName1 = new JTextArea(5,40);
		// add it to the panel
		panel_service_1.add(textName1);
		//How wide should input box be? - doesn't effect number that can be entered
		//textName1.setColumns(30);
		
		/*JLabel lblNewLabel_2 = new JLabel(" Name 2 ");
		panel_service_1.add(lblNewLabel_2);
		
		textName2 = new JTextField();
		panel_service_1.add(textName2);
		textName2.setColumns(10);*/
		
		String[] ops = new String[] {"Service chosen", "Print Out", "Prtiner Status", "Calculate Order Total", "Send Order"};
		//Create combobox and add to panel
		JComboBox comboOperation = new JComboBox();
		comboOperation.setModel(new DefaultComboBoxModel(ops));
		panel_service_1.add(comboOperation);
	
		//Set Up Button ....
		JButton btnPrint = new JButton("Print");
		textResponse = new JTextArea(5, 40);
		textResponse .setLineWrap(true);
		textResponse.setWrapStyleWord(true);
		
		//Add an action listener to our button
		btnPrint.addActionListener(new ActionListener() {
			
			//implement action performed method
			//This will happen when the button is clicked
			public void actionPerformed(ActionEvent e) {

				//Retrieve data from GUI
				String printJobContent = textName1.getText();
				//String name2 = textName2.getText();

				//int index = comboOperation.getSelectedIndex();
				//Do some logic with our index or send it over
				
				//String data = name1 + " and " + name2;
				
				
				//* This is the only gRPC in this method*/
				// we need to call the server from inside the button code

				//String name = "Joe and Ann";
				containsPrintJob printJob = containsPrintJob.newBuilder().setContent(printJobContent).build();
				confirmationMessage confirmation = bstub.print(printJob);
				System.out.println(confirmation.getConfirmation());

				
				//populate the JTextArea in the panel
				textResponse.append("reply:"+ confirmation.getConfirmation() +"\n");
				
				System.out.println("res: " + confirmation.getConfirmation());		
				

			}
		}); //End of setup button
		System.out.println("Have gotten outside text set up");
		//Add button to the panel
		panel_service_1.add(btnPrint);
		
		JScrollPane scrollPane = new JScrollPane(textResponse);
		
		//textResponse.setSize(new Dimension(15, 30));
		panel_service_1.add(scrollPane);
		
		
		/*JPanel panel_service_2 = new JPanel();
		frame.getContentPane().add(panel_service_2);
		
		JPanel panel_service_3 = new JPanel();
		frame.getContentPane().add(panel_service_3);*/
			
		
	}

}
