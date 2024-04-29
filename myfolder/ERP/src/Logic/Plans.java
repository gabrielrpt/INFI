package Logic;

import java.util.*;

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

    // Define the production hierarchy information
    static String[][] productionInfo = {
            {"P1", "P3", "T1", "45"},
            {"P3", "P4", "T2", "15"},
            {"P3", "P4", "T3", "25"},
            {"P4", "P5", "T4", "25"},
            {"P4", "P6", "T2", "25"},
            {"P4", "P7", "T3", "15"},
            {"P2", "P8", "T1", "45"},
            {"P8", "P7", "T6", "15"},
            {"P8", "P9", "T5", "45"}
    };
    public static String[] getBestSupplier(String pieceType, int requiredQuantity, int dueDate, double latePenalty, double earlyPenalty) {
        String[] bestSupplier = null;
        double minCost = Double.MAX_VALUE;
        int transformationTime = 0;
        int purchasingDay = 0;
        int deliveryTime = 0;

        for (String[] supplier : supplierInfo) {
            if (supplier[1].equals(pieceType)) {
                int supplierMinOrder = Integer.parseInt(supplier[2]);
                int supplierDeliveryTime = Integer.parseInt(supplier[4].split(" ")[0]);
                double pieceCost = Double.parseDouble(supplier[3]);

                int orderQuantity = Math.max(requiredQuantity, supplierMinOrder);
                double totalPieceCost = orderQuantity * pieceCost;

                double penalty = 0;
                if (supplierDeliveryTime + transformationTime > dueDate) {
                    penalty = (supplierDeliveryTime + transformationTime - dueDate) * latePenalty;
                    purchasingDay = 0; // Set production day to current day
                } else if (supplierDeliveryTime + transformationTime < dueDate) {
                    penalty = (dueDate - supplierDeliveryTime - transformationTime) * earlyPenalty;
                    purchasingDay = dueDate - supplierDeliveryTime - transformationTime; // Set production day x days after receiving the order
                }

                double totalCost = totalPieceCost + penalty;

                if (totalCost < minCost) {
                    minCost = totalCost;
                    deliveryTime = supplierDeliveryTime;
                    bestSupplier = supplier;
                }
            }
        }
        if(bestSupplier==null){
            System.out.println("No supplier found for piece type: " + pieceType);
            if(pieceType.equals("P1")) bestSupplier = supplierInfo[4];
            else bestSupplier = supplierInfo[5];
        }
        // Add production day to the end of the bestSupplier array
        String[] bestSupplierWithProductionDay = Arrays.copyOf(bestSupplier, bestSupplier.length + 2);
        bestSupplierWithProductionDay[bestSupplierWithProductionDay.length - 2] = String.valueOf(purchasingDay);
        bestSupplierWithProductionDay[bestSupplierWithProductionDay.length - 1] = String.valueOf(purchasingDay+deliveryTime);
        return bestSupplierWithProductionDay;
    }
    public static List<String[]> getAllPaths(String finalPiece) {
        List<String[]> allPaths = new ArrayList<>();
        dfs("P1", finalPiece, "", 0, allPaths);
        dfs("P2", finalPiece, "", 0, allPaths);
        return allPaths;
    }

    public static String getFastestPathFromAll(List<String[]> allPaths) {
        String[] fastestPath = null;
        int minTime = Integer.MAX_VALUE;

        for (String[] path : allPaths) {
            int time = Integer.parseInt(path[1]);
            if (time < minTime) {
                minTime = time;
                fastestPath = path;
            }
        }

        return fastestPath[0];
    }

    public static String getRawPiece(String path) {
        return path.split(" ")[0];
    }

    private static void dfs(String currentPiece, String finalPiece, String currentPath, int currentTime, List<String[]> allPaths) {
        currentPath += currentPiece + " ";
        if (currentPiece.equals(finalPiece)) {
            allPaths.add(new String[]{currentPath.trim(), String.valueOf(currentTime)});
            return;
        }
        for (String[] info : productionInfo) {
            if (info[0].equals(currentPiece)) {
                dfs(info[1], finalPiece, currentPath, currentTime + Integer.parseInt(info[3]), allPaths);
            }
        }
    }

    public static Map<Integer, String> calculateProductionTime(String path, int quantity, int startDay) {
        final int conveyorTime = 5;
        final int returnTime = 10;
        String[] pieceTypes = path.split(" ");
        int totalTime = 0;
        Map<Integer, String> productionSchedule = new HashMap<>();

        for (int i = 0; i < pieceTypes.length; i++) {
            String pieceType = pieceTypes[i];
            for (String[] info : productionInfo) {
                if (info[0].equals(pieceType)) {
                    // Check if the next piece in the path corresponds to info[1]
                    if (i < pieceTypes.length - 1 && info[1].equals(pieceTypes[i + 1])) {
                        int transformationTime = Integer.parseInt(info[3]);
                        int productionTime = transformationTime + conveyorTime;
                        if (i != pieceTypes.length - 2 && i!=0) {
                            productionTime += returnTime;
                        }
                        totalTime += productionTime;
                        System.out.println("Time:" + totalTime);
                        int day = totalTime / 60 + startDay;
                        String expectedPiece = pieceTypes[i+1] + " " + quantity;
                        productionSchedule.put(day, expectedPiece);
                        break;
                    }
                }
            }
            // If we're at the last piece in the path, we can end the loop
            if (i == pieceTypes.length - 2) {
                break;
            }
        }
        return productionSchedule;
    }
}
