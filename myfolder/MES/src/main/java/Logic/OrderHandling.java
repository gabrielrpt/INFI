package Logic;

import org.OPC_UA.OpcuaClient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import Logic.Order;
public class OrderHandling {

    static OpcuaClient opcuaclient = new OpcuaClient();
    ArrayList<Order> orderList = new ArrayList<>();
    int currentDate = 0;
    public void run() {
        try {
            opcuaclient.connect("opc.tcp://localhost:4840");
        } catch (Exception e) {
            System.out.println("Failed to connect to OPC UA server: " + e.getMessage());
            // Handle the error appropriately for your application
        }
    }

    public void decomposeOrders(List<Order> sortedOrders) {
        for (Order order : sortedOrders) {
            String rawPiece = order.getRawPiece();
            String finalPiece = order.getWorkPiece();
            int quantity = order.getQuantity();

            // Now you can use rawPiece, finalPiece, and quantity as needed
        }
    }

    public List<Order> sortOrdersByCost(List<Order> orders) {
        // Calculate the total cost for each order
        for (Order order : orders) {
            order.calculateTotalCost();
        }

        // Sort the orders by total cost
        Collections.sort(orders, (Order o1, Order o2) -> Double.compare(o1.getTotalCost(), o2.getTotalCost()));
        return orders;
    }


    public List<Order> getIncompleteOrders() {
        List<Order> incompleteOrders = new ArrayList<>();
        Iterator<Order> iterator = orderList.iterator();

        while (iterator.hasNext()) {
            Order order = iterator.next();
            try {
                if (!order.isComplete()) {
                    incompleteOrders.add(order);
                    iterator.remove();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the error appropriately for your application
            }
        }

        return incompleteOrders;
    }

    public void addIncompleteOrdersBack(List<Order> incompleteOrders) {
        orderList.addAll(incompleteOrders);
    }

}