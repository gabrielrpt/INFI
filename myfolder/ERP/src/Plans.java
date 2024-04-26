public class Plans {
    // Define the supplier and product information
    static String[][] supplierInfo = {
            {"SupplierA", "P1", "16", "30", "4 days"},
            {"SupplierA", "P2", "16", "10", "4 days"},
            {"SupplierB", "P1", "8", "45", "2 days"},
            {"SupplierB", "P2", "8", "15", "2 days"},
            {"SupplierC", "P1", "4", "55", "1 day"},
            {"SupplierC", "P2", "4", "18", "1 day"}
    };

    // Function to determine the best supplier based on required quantity
    public static int getBestSupplier(String pieceType, int requiredQuantity) {
        String bestSupplier = null;
        int minOrderQuantity = 0;

        // Iterate through the supplier information
        for (String[] supplier : supplierInfo) {
            String supplierPiece = supplier[1];
            if (supplierPiece.equals(pieceType)) {
                int supplierMinOrder = Integer.parseInt(supplier[2]);
                // Check if this supplier's minimum order is less than the current minimum order
                if (supplierMinOrder <= requiredQuantity && supplierMinOrder > minOrderQuantity) {
                    minOrderQuantity = supplierMinOrder;
                    bestSupplier = supplier[3]; // Store the best supplier
                }
            }
        }
        if(bestSupplier==null){
            if(pieceType.equals("P1")) return 55;
            else return 18;
        }
        return Integer.parseInt(bestSupplier);
    }
}
