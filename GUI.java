package project4_;

import java.awt.BorderLayout;
import javax.swing.JApplet;

public class GUI extends JApplet {
	
	
		static final int WIDTH	= 300;
		static final int HEIGHT	= 150;


		public void init(){
//			Set looks
			setSize(WIDTH,HEIGHT);
			
			
//			Create panels
			Connection	connection	= new Connection();
			Details		details		= new Details();
			
//			Add panels
			add(details, 	BorderLayout.SOUTH);
			add(connection, BorderLayout.CENTER);
		}

	}
