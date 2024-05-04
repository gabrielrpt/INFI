package Logic;

public class Orders {
    private String orderNumber;
    private String workPiece;
    private int quantity;
    private int dueDate;
    private double latePenalty;
    private double earlyPenalty;
    private int productionDay;

    public Orders(String orderNumber, String workPiece, int quantity, int dueDate, double latePenalty, double earlyPenalty, int productionDay) {
        this.orderNumber = orderNumber;
        this.workPiece = workPiece;
        this.quantity = quantity;
        this.dueDate = dueDate;
        this.latePenalty = latePenalty;
        this.earlyPenalty = earlyPenalty;
        this.productionDay = productionDay;
    }

    // getters and setters for each variable can be added here if needed

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getWorkPiece() {
        return workPiece;
    }

    public int getWorkPieceNumber() {
        if (workPiece != null && workPiece.length() > 1) {
            return Integer.parseInt(workPiece.substring(1));
        } else {
            throw new IllegalArgumentException("Work piece is not in the expected format (Px)");
        }
    }

    public void setWorkPiece(String workPiece) {
        this.workPiece = workPiece;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public double getLatePenalty() {
        return latePenalty;
    }

    public void setLatePenalty(double latePenalty) {
        this.latePenalty = latePenalty;
    }

    public double getEarlyPenalty() {
        return earlyPenalty;
    }

    public void setEarlyPenalty(double earlyPenalty) {
        this.earlyPenalty = earlyPenalty;
    }

    public int getProductionDay() {
        return productionDay;
    }

    public void setProductionDay(int productionDay) {
        this.productionDay = productionDay;
    }
}