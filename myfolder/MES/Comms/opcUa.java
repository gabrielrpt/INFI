package Comms;

import service.OpcuaClient;
import service.TcpClient;
import service.jsonServices;


public class opcUa implements Runnable{
    //private static int days=
    private opcUaClient opClient;

    @Override
    public void run() {
        opClient = new opcUaClient();  // Create an instance here
        opClient.connect("opc.tcp://DESKTOP-152F154");

        while(1) {
            try{
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}





