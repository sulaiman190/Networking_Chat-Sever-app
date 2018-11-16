package project4_;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable{
    private DatagramSocket socket;
    private static int clientPort;
    private static final int SERVER_PORT   = 4000;
    private ThreadLocal<String> myThreadsLastPacket;
    private String threadName;
    private Thread thread;
    private boolean TCP;
    
    Client(String clientName, boolean sendTCP){
        try{
            this.TCP = sendTCP;
            this.setThreadName(clientName);
            myThreadsLastPacket = new ThreadLocal<String>();
            clientPort = this.getClientPort();
            this.socket = new DatagramSocket(clientPort);
            this.start();
        }catch(SocketException e){
            System.out.println("CLIENT: SOCKET ERROR");
        }
    }
    
    Client(String clientName, DatagramSocket socket){
        this.setThreadName(clientName);
        System.out.println(this.getThreadName() +": Restarting client:");
        this.socket = socket;
        this.start();
    }
    
    private String getThreadName() {
        return this.threadName;
    }
     
    private void listenUDP(){
        try {
            System.out.println(this.getThreadName() +": Listening on UDP port: " +clientPort);
            this.sendHandShake();
            
            while (true){
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                this.getSocket().receive(packet);
                String message = new String(packet.getData());
                message = message.trim();
                this.checkUDPPacketMessage(message, packet.getPort());
                System.out.println("Client: sending UDP packet: KILL");
                this.sendUDPPacket("KILL", packet.getPort());
            }
        } catch (SocketException ex) {
            System.out.println("Client: SOCKET ERROR");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Client: PACKET ERROR");
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {  
        
      if(this.TCP){
          this.listenTCP();
      }else
          this.listenUDP();
}
    private void sendHandShake(){
        try{
            String message = "Server, I am your Client";
            myThreadsLastPacket.set(message);
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),SERVER_PORT);
            System.out.println("Client: sending UDP packet: " +message);
            this.getSocket().send(packet);
        }catch(IOException e){
            System.out.println("Client: Could not send UDP packet");
        }
    }
     
    private void setThreadName(String name){
        if(!name.isEmpty()){
            threadName = name;
        }else
            System.out.println("Could not name the Client");
    }

    private void start() {
        if (thread == null) {
            thread = new Thread (this, this.getThreadName());
            thread.start ();
        }
    }

    private void checkUDPPacketMessage(String message, int port) throws InterruptedException {
        if(message.isEmpty()){
            System.out.println("The packet contained no data");
        }else if(message.contains("WELCOME") && port != SERVER_PORT){
            Thread.sleep(2000);
            this.sendUDPPacket("HELLO", port);
        } else if(message.equals("REPEAT")){
            this.sendUDPPacket(myThreadsLastPacket.get(), true);
        } else
            System.out.println("Client: received packet: " +message +"port: " +port);
    }

    private void sendUDPPacket(String message, int port) {

        try{
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), port);
            System.out.println("Client: sending UDP packet: " +message +" to port: " +port);
            this.getSocket().send(packet);
        }catch(UnknownHostException e){
            System.out.println("UNKNOWN HOST ERROR");
        }catch(IOException e){
            System.out.println("PACKET ERROR");
        }

    }
    
    private DatagramSocket getSocket() {
        return this.socket;
    }
    
    private void start(boolean b) {
        if (thread == null) {
            thread = new Thread (this, this.getThreadName());
        thread.start ();
        }    
    }

    private void listenTCP() {
        try {
                String message = "HELLO\n";
                Socket clientSocket = new Socket("localhost", SERVER_PORT);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Client: sending stream: HELLO");
                outToServer.writeBytes(message);
                String response = inFromServer.readLine();
                System.out.println("Client: stream received: " +response);          
                clientSocket.close();
            } catch (IOException ex) {
            	System.out.println("Socket Error");
            }
    }

    private void sendUDPPacket(String message, boolean b) {
        try{
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),SERVER_PORT);
            DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),SERVER_PORT);
            DatagramPacket packet3 = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(),SERVER_PORT);
            this.getSocket().send(packet);
            this.getSocket().send(packet2);
            this.getSocket().send(packet3);
        }catch(UnknownHostException e){
            System.out.println("UNKNOWN HOST ERROR");
            e.printStackTrace();
        }catch(IOException e){
            System.out.println("PACKET ERROR");
            e.printStackTrace();
        }
    } 
    
    private int getClientPort(){
        int ranInt;
        ranInt = ThreadLocalRandom.current().nextInt(4001, 4010 + 1);
        return ranInt;
    }
    
}




