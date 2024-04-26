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


    // Constructor
    public Order(String orderNumber, String workPiece, int quantity, int dueDate, double latePenalty, double earlyPenalty) {
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
    public void createPieces(){
        for(int i = 0; i < quantity; i++){
            pieces[i] = new Piece(workPiece, ProductionHierarchy.getRawPiece(workPiece));
        }
    }

    // Check if all pieces are complete
    public boolean isComplete(){
        for(Piece piece : pieces){
            if(!piece.isComplete()) return false;
        }
        return true;
    }

    // Calculate the total cost of the order by summing the costs of all pieces when complete
    public void calculateTotalCost(){
        for(Piece piece : pieces){
            totalCost += piece.getPieceCost();
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
}
