package Logic;

import GUI.MainMenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Logic.Plans.*;
import static database.javaDatabase.insertOrder;
import static database.javaDatabase.insertOrderCost;


public class OrderManagement {
    private static final int PORT = 24680;
    private static final int BUFFER_SIZE = 1024;
    ArrayList<Order> orderList = new ArrayList<>();
    public MainMenu mainMenu;
    public ServerERP_MES server;

    public void orderManagement() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("OrderReceiver is running...");
            byte[] buffer = new byte[BUFFER_SIZE];
            //checkOrderCompletion();
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String orderXML = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received order XML: " + orderXML);

                // Process the order XML
                processOrder(orderXML);

                // Send acknowledgment
                //sendAcknowledgment(packet.getAddress(), packet.getPort());
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    //Create a method that continuously checks if any order in the arraylist is completed and if so, sends it to the database
    public void checkOrderCompletion() throws SQLException {
        boolean flag = true;
        //Currently, the method will only check once and then stop
        while (flag) {
            if (orderList.isEmpty()) {
                //System.out.println("No orders to check.");
            } else {
                Iterator<Order> iterator = orderList.iterator();
                while (iterator.hasNext()) {
                    Order order = iterator.next();
                    System.out.println("Checking order " + order.getOrderNumber() + "...");
                    if (order.isComplete()) {
                        System.out.println("Order " + order.getOrderNumber() + " is completed.");
                        order.calculateTotalCost();
                        // Send order to database
                        insertOrderCost(order.getTotalCost(), order.getOrderNumber());
                        iterator.remove();
                        flag = false;
                    }
                }
            }
        }
    }

    private void processOrder(String orderXML) throws SQLException {
        // Define regular expressions to match attributes
        String orderNumberRegex = "Number=\"(\\d+)\"";
        String workPieceRegex = "WorkPiece=\"(P[5679])\"";
        String quantityRegex = "Quantity=\"(\\d+)\"";
        String dueDateRegex = "DueDate=\"(\\d+)\"";
        String latePenaltyRegex = "LatePen=\"(\\d+(\\.\\d+)?)\"";
        String earlyPenaltyRegex = "EarlyPen=\"(\\d+(\\.\\d+)?)\"";

        // Compile regular expressions
        Pattern orderNumberPattern = Pattern.compile(orderNumberRegex);
        Pattern workPiecePattern = Pattern.compile(workPieceRegex);
        Pattern quantityPattern = Pattern.compile(quantityRegex);
        Pattern dueDatePattern = Pattern.compile(dueDateRegex);
        Pattern latePenaltyPattern = Pattern.compile(latePenaltyRegex);
        Pattern earlyPenaltyPattern = Pattern.compile(earlyPenaltyRegex);

        // Match patterns in the XML string
        Matcher orderNumberMatcher = orderNumberPattern.matcher(orderXML);
        Matcher workPieceMatcher = workPiecePattern.matcher(orderXML);
        Matcher quantityMatcher = quantityPattern.matcher(orderXML);
        Matcher dueDateMatcher = dueDatePattern.matcher(orderXML);
        Matcher latePenaltyMatcher = latePenaltyPattern.matcher(orderXML);
        Matcher earlyPenaltyMatcher = earlyPenaltyPattern.matcher(orderXML);

        // Extract matched values
        if (orderNumberMatcher.find() && workPieceMatcher.find() && quantityMatcher.find() &&
                dueDateMatcher.find() && latePenaltyMatcher.find() && earlyPenaltyMatcher.find()) {
            String orderNumber = orderNumberMatcher.group(1);
            String workPiece = workPieceMatcher.group(1);
            int quantity = Integer.parseInt(quantityMatcher.group(1));
            int dueDate = Integer.parseInt(dueDateMatcher.group(1));
            double latePenalty = Double.parseDouble(latePenaltyMatcher.group(1));
            double earlyPenalty = Double.parseDouble(earlyPenaltyMatcher.group(1));

            // Create an Order object
            Order order = new Order(orderNumber, workPiece, quantity, dueDate, latePenalty, earlyPenalty);
            orderList.add(order);
            insertOrder(order.getOrderNumber(), order.getWorkPiece(), order.getQuantity(), order.getDueDate(), order.getLatePenalty(), order.getEarlyPenalty(), order.getProductionDay());
            server.order = order;
            mainMenu.productionPlan.updateTableDay(order.getProductionDay(), Integer.parseInt(orderNumber));
            System.out.println("supplier: " + order.getSupplier()[0] + order.getSupplier()[1] + order.getSupplier()[2] + order.getSupplier()[3] + order.getSupplier()[4] + order.getSupplier()[5] + order.getSupplier()[6]);
            mainMenu.purchasingPlan.updateTable(order);
            mainMenu.mps.updateTable(calculateProductionTime(getFastestPathFromAll(getAllPaths(workPiece)), quantity, Integer.parseInt(order.getSupplier()[6])));

            // Process the order
            System.out.println("Order Number: " + orderNumber);
            System.out.println("Work Piece: " + workPiece);
            System.out.println("Quantity: " + quantity);
            System.out.println("Due Date: " + dueDate);
            System.out.println("Late Penalty: €" + latePenalty);
            System.out.println("Early Penalty: €" + earlyPenalty);
            System.out.println("Order List Size:" + orderList.size());

        } else {
            System.out.println("Error: Failed to extract order details from XML.");
        }
    }

    private static boolean isValidWorkPiece(String workPiece) {
        return workPiece.equals("P5") || workPiece.equals("P6") || workPiece.equals("P7") || workPiece.equals("P9");
    }

    private static void sendAcknowledgment(InetAddress address, int port) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        String acknowledgment = "Order received successfully";
        byte[] ackBytes = acknowledgment.getBytes();
        DatagramPacket ackPacket = new DatagramPacket(ackBytes, ackBytes.length, address, port);
        socket.send(ackPacket);
        socket.close();
    }
}
