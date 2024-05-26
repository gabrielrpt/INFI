package Logic;

import GUI.MainMenu;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.SocketException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import database.javaDatabase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


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
        try (DatagramSocket socket = new DatagramSocket(PORT, null)) {
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
        ResultSet rs = javaDatabase.getCompletedOrders();
        if (rs != null) {
            try {
                while (rs.next()) {
                    String orderNumber = rs.getString("ordernumber");
                    System.out.println("Checking order " + orderNumber + "...");
                    for (Order order : orderList) {
                        if (order.getOrderNumber().equals(orderNumber)) {
                            try {
                                System.out.println("Checking");
                                order.isComplete();
                                order.calculateTotalCost();
                                insertOrderCost(order.getTotalCost(), orderNumber);
                                break;
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void processOrder(String orderXML) throws SQLException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(orderXML.getBytes()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return;
        }

        document.getDocumentElement().normalize();
        NodeList orderNodeList = ( document).getElementsByTagName("Order");

        for (int i = 0; i < orderNodeList.getLength(); i++) {
            Node orderNode = orderNodeList.item(i);
            if (orderNode.getNodeType() == Node.ELEMENT_NODE) {
                Element orderElement = (Element) orderNode;
                String orderNumber = orderElement.getAttribute("Number");
                String workPiece = orderElement.getAttribute("WorkPiece");
                int quantity = Integer.parseInt(orderElement.getAttribute("Quantity"));
                int dueDate = Integer.parseInt(orderElement.getAttribute("DueDate"));
                double latePenalty = Double.parseDouble(orderElement.getAttribute("LatePen"));
                double earlyPenalty = Double.parseDouble(orderElement.getAttribute("EarlyPen"));

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