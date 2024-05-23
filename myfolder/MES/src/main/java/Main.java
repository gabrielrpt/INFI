import Logic.OrderHandling;
import Logic.Orders;
import Logic.ShopFloor;
import org.OPC_UA.OPCUAClient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        // This is the main class for the MES
        // It creates the orderHandling object and every 60 seconds calls the getOrdersByProdDay method
        // to get the orders for the current production day
        OPCUAClient opcuaClient = new OPCUAClient();
        boolean isConnected = false;

        // Keep trying to connect until successful
        while (!isConnected) {
            try {
                opcuaClient.connect("opc.tcp://localhost:4840");
                isConnected = true;
            } catch (Exception e) {
                System.out.println("Failed to connect. Retrying...");
                try {
                    Thread.sleep(5000); // wait for 5 seconds before retrying
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        OrderHandling orderHandling = new OrderHandling(opcuaClient);

        //Create a new shop floor object
        ShopFloor shopFloor = new ShopFloor(opcuaClient);

        //Create a list to store the orders
        List<Orders> orderList = new ArrayList<>();

        //Create a current production day variable
        AtomicInteger prodDay = new AtomicInteger(1);

        //Create a new thread that runs every 60 seconds
        Thread orderUpdatingThread = new Thread(() -> {
            while (true) {
                orderHandling.getOrdersByProdDay(0, orderList);
                try {
                    Thread.sleep(60000); // Sleep for 60 seconds
                    prodDay.getAndIncrement(); // Increment the production day
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        orderUpdatingThread.start();

        Thread orderProcessingThread = new Thread(() -> {
            while (true) {
                if (!orderList.isEmpty()) {
                    // Sort the order list based on the production day
                    orderList.sort(Comparator.comparingInt(Orders::getProductionDay));

                    System.out.println("Orders for production day " + prodDay.get());
                    // Process each order in the order list
                    Iterator<Orders> iterator = orderList.iterator();
                    while (iterator.hasNext()) {
                        Orders order = iterator.next();
                        if (order.getProductionDay() <= prodDay.get()) {
                            // If the order is of type 6, process it in a separate thread
                            if (order.getWorkPieceNumber() == 6) {
                                boolean flag = false;
                                Orders nextOrder = null;
                                // Find the next order of type 7 or 9 and process it simultaneously
                                Iterator<Orders> iterator2 = orderList.iterator();
                                while (iterator2.hasNext()) {
                                    nextOrder = iterator2.next();
                                    if (nextOrder.getWorkPieceNumber() == 7) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag) {
                                    // Process the order in a separate thread
                                    Orders finalNextOrder = nextOrder;
                                    new Thread(() -> {
                                        try {
                                            shopFloor.processOrder(finalNextOrder);
                                            //remove the order from the orderList
                                            System.out.println("Current day: " + prodDay.get() + " Order day: " + order.getProductionDay());
                                            iterator2.remove();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).start();
                                }

                                try {
                                    shopFloor.processOrder(order);
                                    //remove the order from the orderList
                                    System.out.println("Current day: " + prodDay.get() + " Order day: " + order.getProductionDay());
                                    iterator.remove();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                            } else if(order.getWorkPieceNumber() == 7){
                                // If the order is of type 7 or 9, process it in a separate thread
                                boolean flag = false;
                                Orders nextOrder = null;
                                // Find the next order of type 7 or 9 and process it simultaneously
                                Iterator<Orders> iterator2 = orderList.iterator();
                                while (iterator2.hasNext()) {
                                    nextOrder = iterator2.next();
                                    if (nextOrder.getWorkPieceNumber() == 6) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if (flag) {
                                    // Process the order in a separate thread
                                    new Thread(() -> {
                                        try {
                                            shopFloor.processOrder(order);
                                            //remove the order from the orderList
                                            System.out.println("Current day: " + prodDay.get() + " Order day: " + order.getProductionDay());
                                            iterator2.remove();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).start();
                                    try {
                                        shopFloor.processOrder(nextOrder);
                                        //remove the order from the orderList
                                        System.out.println("Current day: " + prodDay.get() + " Order day: " + order.getProductionDay());
                                        iterator.remove();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    // If the order is not of type 6, process it normally
                                    try {
                                        shopFloor.processOrder(order);
                                        //remove the order from the orderList
                                        System.out.println("Current day: " + prodDay.get() + " Order day: " + order.getProductionDay());
                                        iterator.remove();
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                    } else {
                                        // If the order is not of type 6, process it normally
                                        try {
                                            shopFloor.processOrder(order);
                                            //remove the order from the orderList
                                            System.out.println("Current day: " + prodDay.get() + " Order day: " + order.getProductionDay());
                                            iterator.remove();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                    }
                }
                try {
                    Thread.sleep(1000); // Sleep for 1 second before checking the order list again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        orderProcessingThread.start();
    }
}
