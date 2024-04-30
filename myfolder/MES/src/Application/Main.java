package Application;
import Comms.opcua;
import GUI.MainMenu;
import Service.TimerCount;


public class Main {
    public volatile static int day = 0;

    // static boolean newOrdersArrived = false;

    public static void main(String[] args) throws Exception {

        opcua opClient = new opcua();
        opClient.connect("opc.tcp://DESKTOP-152F154:4840");

        TimerCount timer = new TimerCount();
        timer.NewTimer();


        //Thread startOPCua = new Thread(new OPCUAReader());
        //Thread dayCounter = new Thread(new DayCounter());

    }
}
