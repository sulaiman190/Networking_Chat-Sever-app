package project4_;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JButton;

	

	public class Connection extends JPanel implements ActionListener{
		
		JComboBox 		connectionsBox;
		JButton			connectButton;
		
		static String[] connections 		= new String[3];
		static String 	connectionSelection = "";
		boolean			connected 			= false;
        
		
		
		public Connection(){

			setBorder(BorderFactory.createBevelBorder(0));
			
			
			connections[0] = "Select Connection";
			connections[1] = "TCP";
			connections[2] = "UDP";
			
//			Connection Panel components
			connectionsBox		= new JComboBox(connections);
			connectButton		= new JButton("Connect");
			
//			action Listeners
			connectionsBox	.addActionListener(this);
			connectButton	.addActionListener(this);
			
//			add components
			add(connectionsBox);
			add(connectButton);	
		}

                @Override
		public void actionPerformed(ActionEvent e) {
//			If comboBox item changes, record change
			if(e.getActionCommand().equals("comboBoxChanged")){
				
				String choice = getChoice(e);
				
				if(choice.equals("Select Connection"))
					connectionSelection = "Select Connection";
				if(choice.equals("TCP"))
					connectionSelection = "TCP";
				if(choice.equals("UDP"))
					connectionSelection = "UDP";
			}//end if
			
//			When connect button pressed, connect with selected connection type
			if(e.getActionCommand().equals("Connect")){
				
				if(connectionSelection.equals("TCP"))
					connect("TCP");
				if(connectionSelection.equals("UDP"))
					connect("UDP");					
			}//end if
			
//			When disconnect button is pressed, disconnect
			if(e.getActionCommand().equals("Disconnect") && connected == true)
				disconnect();
		}

		public void connect(String connection){
			connectionsBox		.setEnabled(false);
			connectButton		.setEnabled(false);
			Details.connection	.setText("Connecting With: " + connection);
                        
                        boolean tcp = false;
                        if( connection.equals("TCP") )
                            tcp = true;   
                        try{
                            EchoServer server = new EchoServer(tcp);
                        }catch(Exception e){
                            System.out.println("Connection: something went wrong with the EchoServer");
                        }
                        try{
                            Client client = new Client("Client",tcp);
                        }catch(Exception e){
                            System.out.println("Connection : something went wrong with the Client");
                        }
                        
                        
			connected = true;
			Details.connection	.setText("Connected With: " + connection);
		}

		public void disconnect(){
			Details.connection	.setText("Diconnecting");
			/***DISCONNECT FROM TCP/UDP HERE***/
			connected = false;
			connectionsBox		.setEnabled(true);
			connectButton		.setEnabled(true);
			Details.connection	.setText("Not Connected");
		}

		public String getChoice(ActionEvent e){
			JComboBox	box		= (JComboBox) e.getSource();
			Object		item	= box.getSelectedItem();
			String		choice	= item.toString();
			return 		choice;
		}
		
		
}
