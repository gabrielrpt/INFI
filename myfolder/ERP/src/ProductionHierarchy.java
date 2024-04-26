import java.util.HashMap;
import java.util.Map;

public class ProductionHierarchy {
    // Define the production hierarchy map
    private static Map<String, String> productionHierarchy = new HashMap<>();

    // Initialize the production hierarchy
    static {
        productionHierarchy.put("P5", "P1");
        productionHierarchy.put("P6", "P1");
        productionHierarchy.put("P7", "P2");
        productionHierarchy.put("P8", "P2");
        productionHierarchy.put("P9", "P2");
    }

    // Function to determine the raw piece required to produce a final piece
    public static String getRawPiece(String finalPiece) {
        return productionHierarchy.get(finalPiece);
    }
}
