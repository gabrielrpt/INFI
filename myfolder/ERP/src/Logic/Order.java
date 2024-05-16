package Logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import static database.javaDatabase.getPieceByOrderNumber;
import static database.javaDatabase.getResultSetSize;

// Define the Order class
public class Order {
    private final String orderNumber;
    private final String workPiece;
    private final int quantity;
    private final int dueDate;
    private final double latePenalty;
    private final double earlyPenalty;
    private final Piece[] pieces;
    private double totalCost = 0;
    private int purchasingDay = 0;
    private int productionDay = 0;
    private String[] supplier;



    // Constructor
    public Order(String orderNumber, String workPiece, int quantity, int dueDate, double latePenalty, double earlyPenalty) throws SQLException {
        this.orderNumber = orderNumber;
        this.workPiece = workPiece;
        this.quantity = quantity;
        this.dueDate = dueDate;
        this.latePenalty = latePenalty;
        this.earlyPenalty = earlyPenalty;
        this.pieces = new Piece[quantity];
        createPieces();
    }

    //Create a piece object for each piece in the order
    public void createPieces() throws SQLException {
        String rawPiece = Plans.getRawPiece(Plans.getFastestPathFromAll(Plans.getAllPaths(workPiece)));
        String[] supplier = Plans.getBestSupplier(rawPiece, quantity, dueDate, latePenalty, earlyPenalty, workPiece, quantity);
        this.supplier = supplier;
        double rawCost = Double.parseDouble(supplier[3]);
        this.purchasingDay = Integer.parseInt(supplier[5]);
        this.productionDay = Integer.parseInt(supplier[6]);
        for(int i = 0; i < quantity; i++){
            pieces[i] = new Piece(workPiece, rawPiece, Integer.parseInt(orderNumber), rawCost);
        }
    }

    // Check if all pieces are complete
    public boolean isComplete() throws SQLException {
        ResultSet resultSet = getPieceByOrderNumber(orderNumber);
        if(resultSet == null) return false;
        if (getResultSetSize(resultSet) == quantity) {
            for (Piece piece : pieces) {
                piece.setFromResultSet(resultSet);
                piece.calculatePieceCost(piece.calculateDepreciationCost());
            }
            return true;
        }
        return false;
    }

    // Calculate the total cost of the order by summing the costs of all pieces when complete
    public void calculateTotalCost(){
        for(Piece piece : pieces){
            totalCost += piece.getPieceCost();
            System.out.println("Piece cost: " + piece.getPieceCost());
        }
    }

    // Getters
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getWorkPiece() {
        return workPiece;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDueDate() {
        return dueDate;
    }

    public double getLatePenalty() {
        return latePenalty;
    }

    public double getEarlyPenalty() {
        return earlyPenalty;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getPurchasingDay() {
        return purchasingDay;
    }

    public int getProductionDay() {
        return productionDay;
    }

    public String[] getSupplier() {
        return supplier;
    }

    public String getRawPiece(){
        return pieces[0].getRawPiece();
    }

    public void setProductionDay(int productionDay) {
        this.purchasingDay = productionDay;
    }
}
