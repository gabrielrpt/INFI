package Logic;

import database.javaDatabase;
import org.OPC_UA.OpcuaClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderHandling {

    private final OpcuaClient client;

    public OrderHandling(OpcuaClient client) {
        this.client= client;

    }


    public void getOrdersByProdDay(int prodDay, List<Orders> orderList) {
        try {
            ResultSet rs = javaDatabase.getOrdersByProdDay(prodDay);
            while (rs.next()) {
                String orderNumber = rs.getString("ordernumber");
                String workPiece = rs.getString("workpiece");
                int quantity = rs.getInt("quantity");
                int dueDate = rs.getInt("duedate");
                double latePenalty = rs.getDouble("latepen");
                double earlyPenalty = rs.getDouble("earlypen");
                int productionDay = rs.getInt("productionday");

                //print the orders
                System.out.println("Order Number: " + orderNumber + " Work Piece: " + workPiece + " Quantity: " + quantity + " Due Date: " + dueDate + " Late Penalty: " + latePenalty + " Early Penalty: " + earlyPenalty + " Production Day: " + productionDay);

                Orders order = new Orders(orderNumber, workPiece, quantity, dueDate, latePenalty, earlyPenalty, productionDay);
                orderList.add(order);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int calculatePiecesNeeded(Orders order) {    // a cena do integer.parseint e suposto pegar na string (P1) e so deixar um mas é preciso criar um metodo
        // Get the type of the piece the order requires
        int pieceType = order.getWorkPieceNumber();

        // Get the quantity of that piece type currently available in the warehouse
        int[] warehouseArray = client.readWarehouseArray("|var|CODESYS Control Win V3 x64.Application.GVL.test", 4);

        // Get the quantity required by the order
        int quantityRequired = order.getQuantity();
        int quantityAvailable = warehouseArray[pieceType];

        // Calculate how many more pieces are needed
        int piecesNeeded = quantityRequired - quantityAvailable;

        return piecesNeeded;
    }


}