package Logic;

import database.javaDatabase;
import org.OPC_UA.OPCUAClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderHandling {

    private final OPCUAClient client;

    public OrderHandling(OPCUAClient client) {
        this.client= client;

    }


    public void getOrdersByProdDay(int prodDay, List<Orders> orderList) {
        try {
            ResultSet rs = javaDatabase.getOrdersByProdDay(prodDay);
            while (rs.next()) {
                boolean flag= true;
                String orderNumber = rs.getString("ordernumber");
                String workPiece = rs.getString("workpiece");
                int quantity = rs.getInt("quantity");
                int dueDate = rs.getInt("duedate");
                double latePenalty = rs.getDouble("latepen");
                double earlyPenalty = rs.getDouble("earlypen");
                int productionDay = rs.getInt("productionday");

                ResultSet prs = javaDatabase.getPieceByOrderNumber(orderNumber);
                prs.next();
                String rawPiece = prs.getString("rawpiece");

                //print the orders
                System.out.println("Order Number: " + orderNumber + " Work Piece: " + workPiece + " Quantity: " + quantity + " Due Date: " + dueDate + " Late Penalty: " + latePenalty + " Early Penalty: " + earlyPenalty + " Production Day: " + productionDay);

                //iterate over the orderList and check if the order is already in the list
                for (Orders order : orderList) {
                    if (order.getOrderNumber().equals(orderNumber)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    Orders order = new Orders(orderNumber, workPiece, rawPiece, quantity, dueDate, latePenalty, earlyPenalty, productionDay);
                    orderList.add(order);
                    for (int i=0; i<11; i++){
                        if (String.valueOf(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId[" + i + "]", 4)).equals(order.getOrderNumber())){
                            break;
                        }
                        else if(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId["+i+"]", 4)==0){
                            client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId["+i+"]", 4, order.getOrderNumber());
                            client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.PendingPieces["+i+"]", 4, String.valueOf(order.getQuantity()));
                            client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.ProductionDay["+i+"]", 4, String.valueOf(order.getProductionDay()));
                            break;
                        }
                    }
                }
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
    // this method will check if all pieces from an order are available in the warehouse pode ser redundante?
    public boolean areAllPiecesAvailable(Orders order) {
        // Calculate the number of pieces needed for the order
        int piecesNeeded = calculatePiecesNeeded(order);

        // If no pieces are needed, then all pieces are available
        if (piecesNeeded == 0) {
            return true;
        } else {
            return false;
        }
    }

}