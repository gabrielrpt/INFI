package Logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import static database.javaDatabase.insertPiece;

public class Piece {
    private final String pieceType;
    private final String rawPiece;
    private String currentType;
    private int arrivalDate = 0;
    private int dispatchDate = 0;
    private int productionCost = 0;
    private int orderId = 0;
    private final double rawCost;
    private double pieceCost = 0;
    private static final double DEPRECIATION_RATE = 0.01; // 1% depreciation rate

    public Piece(String pieceType, String rawPiece, int orderId, double rawCost, int pid) throws SQLException {
        this.pieceType = pieceType;
        this.rawPiece = rawPiece;
        this.orderId = orderId;
        this.rawCost = rawCost;
        insertPiece(pieceType, rawPiece, orderId, rawCost, pid);
    }

    public double calculatePieceCost(double depreciationCost){
        return rawCost + productionCost + depreciationCost;
    }

    public double calculateDepreciationCost(){
        return rawCost * DEPRECIATION_RATE * (dispatchDate - arrivalDate);
    }

    public boolean isComplete(){
        pieceCost = calculatePieceCost(calculateDepreciationCost());
        return dispatchDate != 0;
    }

    public void setFromResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            currentType = resultSet.getString("currenttype");
            arrivalDate = resultSet.getInt("arrivaldate");
            dispatchDate = resultSet.getInt("dispatchdate");
            productionCost = resultSet.getInt("productioncost");
        }
    }

    public void setArrivalDate(int arrivalDate){
        this.arrivalDate = arrivalDate;
    }

    public void setDispatchDate(int dispatchDate){
        this.dispatchDate = dispatchDate;
    }

    public void setProductionCost(int productionCost){
        this.productionCost = productionCost;
    }

    public void setCurrentType(String currentType){
        this.currentType = currentType;
    }

    public String getPieceType(){
        return pieceType;
    }

    public String getRawPiece(){
        return rawPiece;
    }

    public String getCurrentType(){
        return currentType;
    }

    public double getPieceCost(){
        pieceCost = calculatePieceCost(calculateDepreciationCost());
        return pieceCost;
    }

    public int getOrderId(){
        return orderId;
    }
}
