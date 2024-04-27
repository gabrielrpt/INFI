package Application;

/*import service.OpcuaClient;
import service.TcpClient;
import service.jsonServices;*/

import java.io.IOException;
import Comms.*;
import java.net.Socket;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        /** Inicializa comunicação OPCUA */
        opcUa initializeOPCUA = new opcUa();
        Thread initializeOPCUAthread = new Thread(initializeOPCUA);
        initializeOPCUAthread.start();
    }

}
