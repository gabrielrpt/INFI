import Logic.OrderHandling;
import Logic.Orders;
import org.OPC_UA.OpcuaClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        // This is the main class for the MES
        // It creates the orderHandling object and every 60 seconds calls the getOrdersByProdDay method
        // to get the orders for the current production day
        OpcuaClient opcuaClient = new OpcuaClient();
        opcuaClient.connect("opc.tcp://localhost:4840");
        OrderHandling orderHandling = new OrderHandling(opcuaClient);

        //Create a list to store the orders
        List<Orders> orderList = new ArrayList<>();
        //Create a current production day variable
        AtomicInteger prodDay = new AtomicInteger(1);
        //Create a new thread that runs every 60 seconds
        Thread orderHandlingThread = new Thread(() -> {
            while (true) {
                orderHandling.getOrdersByProdDay(prodDay.get(), orderList);
                prodDay.getAndIncrement();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        orderHandlingThread.start();
    }
}