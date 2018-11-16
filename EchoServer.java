package project4_;

public class EchoServer {
	
    private static final int SERVER_PORT = 4000;

    EchoServer(boolean listenTCP){
        System.out.println("Starting EchoServer");
       // if(listenTCP)
            new TCP_Handler("EchoServer", SERVER_PORT);
       // else
            new UDP_Handler("EchoServer",SERVER_PORT);
    }
}
