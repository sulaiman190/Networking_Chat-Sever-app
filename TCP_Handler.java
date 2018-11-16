package project4_;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class TCP_Handler implements Runnable {
	
        private boolean disconnect;
	private ServerSocket welcomeSocket;
        private Socket connectionSocket;
        private String threadName;
        private int portNumber;
        private int clientPort;
        private Thread thread;
	
    TCP_Handler(String serverName, int SERVER_PORT) {
        if(serverName.isEmpty())
            System.out.println("Server name not entered.");
        else{
            threadName = serverName;
            this.portNumber = SERVER_PORT;
            this.start();
        }
    }

    TCP_Handler(String serverName, int CLIENT_PORT, int SERVER_PORT) {
         if(serverName.isEmpty())
            System.out.println("Server name not entered.");
        else{
            this.threadName = serverName;
            this.portNumber = SERVER_PORT;
            this.clientPort = CLIENT_PORT;
            this.start();
        }
    }
	
    @Override
    public void run() {
        System.out.println("EchoServer: Listening on TCP port: " +portNumber);
        try {
            ServerSocket server = new ServerSocket(portNumber);
            boolean connected = true;
            while(true){
                try {
                    Socket client = server.accept();
                    this.handler(client);
                } catch (IOException ex) {
                    System.out.println("TCPSERVER:NON_FATAL SOCKET ERROR");
                }
            }
        } catch (IOException ex) {
            System.out.println("TCPSERVER:SERVER COULD NOT BE STARTED");
        }
    }

    private void start() {
        try{
            thread = new Thread(this, this.threadName);
        }catch(Exception e){
            System.out.println("Could not start tcp thread");
        }
        this.thread.start();
    }

    private void handler(Socket client) {
        		// TODO Auto-generated method stub
        try{
            BufferedReader buffer = new BufferedReader( 
                new InputStreamReader(client.getInputStream()));
			
            PrintWriter out = new PrintWriter(client.getOutputStream(), 
                true);
           		
            String inputLine = buffer.readLine(); 
            System.out.println("EchoServer: received stream: " + inputLine);
            
            out.println ("EchoServer: " + inputLine + '\n'); 
            System.out.println("EchoServer: sending TCP stream: "+inputLine);
                            
        }catch(IOException e) {
            System.out.println("TCP_Handler: SOCKET ERROR");
        }
    }
    
}
