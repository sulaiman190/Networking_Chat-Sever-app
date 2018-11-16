/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project4_;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Spazz
 */
    public class UDP_Handler implements Runnable{
      
    private DatagramSocket socket;
    private DatagramSocket udpSocket;
    private int clientPort;
    private int portNumber;
    private int udp_port;
    private String threadName;
    private Thread thread;
   /**
 * EchoServer constructor which creates a DatagramSocket on SERVER_PORT
 *
 * @param  serverName  the name of the server being created
 */
    UDP_Handler(String serverName,int serverPort){
        if(serverName.isEmpty())
            System.out.println("Server name not entered.");
        else{
            threadName = serverName;
            this.portNumber = serverPort;
            try {
                this.socket = new DatagramSocket(this.portNumber);
                this.start();
            } catch (SocketException ex) {
                System.out.println("Could not create EchoServer");
            }
        }
    }
    
     UDP_Handler(String serverName, DatagramSocket socket){
        if(serverName.isEmpty())
            System.out.println("Server name not entered.");
        else{
            threadName = serverName;
            System.out.println(this.getThreadName() +": Creating new thread and listening on port: " +portNumber);
            this.socket = socket;
            this.start();
        }
    }

    private UDP_Handler(String serverName,DatagramSocket socket, int udp_port, int clientPort) {
         if(serverName.isEmpty())
            System.out.println("Server name not entered.");
        else{
            threadName = serverName;
            this.udp_port = udp_port;
            this.clientPort = clientPort;
            System.out.println("Creating new thread for client");
            this.udpSocket = socket;
            this.start();
        }
    }
        
    private void checkPacketMessage(String message) throws SocketException{
        if( message.isEmpty() ){
            System.out.println("The packet contained no data");
            
        }else if(message.equals("Server, I am your Client") && !this.isUDPPort()){
            this.sendWelcomeMessage();
            
        }else if( message.equals("KILL") ){
            System.out.println(this.threadName +(char)27 + "[31m: The Packet contained a KILL message. The Server will now stop." + (char)27 + "[0m");
            try{
                this.socket.close();
                this.thread.interrupt(); 
            }catch(Exception e){
                System.out.println("Socket closed");
            }
        }else{
            /*int ranNum = ThreadLocalRandom.current().nextInt(3);
            switch(ranNum){
                case 0:
                            System.out.println(this.getThreadName() +": "+message);
                            break;
                case 1:
                            System.out.println(this.getThreadName() +": "+message);
                            break;
                case 2:
                            System.out.println(this.threadName +(char)27 + "[31m: message dropped sending REPEAT request" + (char)27 + "[0m");
                            this.sendRepeatMessage();  
                            break;
            }*/
            System.out.println(this.threadName +": " +message);
        }
    }
    public DatagramSocket getSocket(){
        return this.socket;
    }
    
    private String getThreadName() {
        return this.threadName;
    }
    
    private void sendRepeatMessage(){
        try{
            String repeat = "REPEAT";
            byte[] buffer = repeat.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),clientPort);
            this.getSocket().send(packet);
            
        }catch(SocketException e){
            System.out.println("SOCKET ERROR");
        }catch(UnknownHostException e){
            System.out.println("UNKNOWN HOST ERROR");
        }catch(IOException e){
            System.out.println("PACKET ERROR");
        }
    }
        
        private void setThreadName(String name){
            if(!name.isEmpty()){
                threadName = "name";
            }else
                System.out.println("Could not name the server");
        }
   
    @Override
    public void run() {
        
        try {
            if(!this.isUDPPort()){
                System.out.println("EchoServer: Listening on UDP port: " +  this.portNumber);
                //this.socket.setSoTimeout(1000);

                while (true){
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    this.getSocket().receive(packet);
                    this.clientPort = packet.getPort();
                    String message = new String(packet.getData());

                    message = message.trim();
                    this.checkPacketMessage(message);
                }
            }else 
                this.listenOnUDPPort();
        } catch (SocketException ex) {
            System.out.println("EchoServer: SOCKET ERROR");
        } catch (IOException ex) {
            System.out.println("EchoServer: PACKET ERROR");
        }
    }    

    public void start () {
        if (thread == null) {
            thread = new Thread (this, this.getThreadName());
            thread.start ();
        }
    }

    private void start(boolean b) {
        if (thread == null) {
            thread = new Thread (this, this.getThreadName());
            thread.start ();
        }
    }

    private void sendWelcomeMessage() {
        try{
            this.udp_port = this.getUDPPort();
            String message = "WELCOME, USE PORT " +this.udp_port +" to continue";
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),clientPort);
            System.out.println("EchoServer: sending UDP packet: " +message);
            DatagramSocket udpSocket = new DatagramSocket(this.udp_port);
            udpSocket.send(packet);
            UDP_Handler udpPort = new UDP_Handler("UDP_EchoServer:",udpSocket,this.udp_port, this.clientPort);
              
        }catch(SocketException e){
            System.out.println("SOCKET ERROR");
        }catch(UnknownHostException e){
            System.out.println("UNKNOWN HOST ERROR");
        }catch(IOException e){
            System.out.println("PACKET ERROR");
        }
    }
    
    private int getUDPPort(){
        int ranInt;
        ranInt = ThreadLocalRandom.current().nextInt(4100, 4200 + 1);
        return ranInt;
    }
    
    private void listenOnUDPPort(){
         try {
            System.out.println(this.threadName +" Listening on UDP port: " +  udp_port);
            while (true){
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                this.udpSocket.receive(packet);
                String message = new String(packet.getData());
                
                message = message.trim();
                this.checkPacketMessage(message);        
            }
        } catch (SocketException ex) {
            System.out.println("EchoServer: SOCKET ERROR");
        } catch (IOException ex) {
            System.out.println("EchoServer: PACKET ERROR");
        }
         
        }
        
    private boolean isUDPPort(){
        return ( this.udp_port >= 4100 && this.udp_port <= 4200 );
        }

}

