
public class Piece {
    private final String pieceType;
    private final String rawPiece;
    private int currentType = 0;
    private int arrivalDate = 0;
    private int dispatchDate = 0;
    private int productionCost = 0;
    private final double rawCost;
    private double pieceCost = 0;
    private static final double DEPRECIATION_RATE = 0.01; // 1% depreciation rate

    public Piece(String pieceType, String rawPiece){
        this.pieceType = pieceType;
        this.rawPiece = rawPiece;
        this.rawCost = Plans.getBestSupplier(rawPiece, 1);
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

    public void setArrivalDate(int arrivalDate){
        this.arrivalDate = arrivalDate;
    }

    public void setDispatchDate(int dispatchDate){
        this.dispatchDate = dispatchDate;
    }

    public void setProductionCost(int productionCost){
        this.productionCost = productionCost;
    }

    public void setCurrentType(int currentType){
        this.currentType = currentType;
    }

    public String getPieceType(){
        return pieceType;
    }

    public String getRawPiece(){
        return rawPiece;
    }

    public int getCurrentType(){
        return currentType;
    }

    public double getPieceCost(){
        return pieceCost;
    }
}
