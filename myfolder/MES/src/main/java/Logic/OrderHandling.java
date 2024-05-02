package Logic;

import database.javaDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderHandling {
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
}