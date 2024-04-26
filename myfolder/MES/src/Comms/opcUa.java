package Comms;

import service.opcUaClient;
//import service.TcpClient;
//import service.jsonServices;   Retirar ambos


public class opcUa implements Runnable{
    //private static int days=
    private opcUaClient opClient;

    @Override
    public void run() {
        opClient = new opcUaClient();  // Create an instance here
        opClient.connect("opc.tcp://DESKTOP-152F154:4840"); //Ver sem os :4840

        while(true) {
            try{
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}





