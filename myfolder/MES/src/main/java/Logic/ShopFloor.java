package Logic;

import java.util.*;

public class ShopFloor {
    private final Map<String, List<String>> cellMachineMap;
    private final Map<String, List<String>> machineToolMap;
    private final Map<String, List<String>> toolTransformationMap;

    public ShopFloor() {
        cellMachineMap = new HashMap<>();
        machineToolMap = new HashMap<>();
        toolTransformationMap = new HashMap<>();

        // Initialize the cellMachineMap
        cellMachineMap.put("C1", Arrays.asList("M1", "M2"));
        cellMachineMap.put("C2", Arrays.asList("M1", "M2"));
        cellMachineMap.put("C3", Arrays.asList("M1", "M2"));
        cellMachineMap.put("C4", Arrays.asList("M3", "M4"));
        cellMachineMap.put("C5", Arrays.asList("M3", "M4"));
        cellMachineMap.put("C6", Arrays.asList("M3", "M4"));

        // Initialize the machineToolMap
        machineToolMap.put("M1", Arrays.asList("T1", "T2", "T3"));
        machineToolMap.put("M2", Arrays.asList("T1", "T2", "T3"));
        machineToolMap.put("M3", Arrays.asList("T1", "T4", "T5"));
        machineToolMap.put("M4", Arrays.asList("T1", "T4", "T6"));

        // Initialize the toolTransformationMap
        toolTransformationMap.put("T1", Arrays.asList("P1", "P3", "P2", "P8"));
        toolTransformationMap.put("T2", Arrays.asList("P3", "P4", "P4", "P6"));
        toolTransformationMap.put("T3", Arrays.asList("P3", "P4", "P4", "P7"));
        toolTransformationMap.put("T4", Arrays.asList("P4", "P5"));
        toolTransformationMap.put("T5", Arrays.asList("P8", "P9"));
        toolTransformationMap.put("T6", Arrays.asList("P8", "P7"));
    }

    public Map<String, Map<String, List<String>>> executeOrder(Orders order) {
        Map<String, Map<String, List<String>>> cellMachinePieceMap = new HashMap<>();
        List<String> pieces = new ArrayList<>(Collections.nCopies(order.getQuantity(), order.getWorkPiece()));
        for (String piece : pieces) {
            for (String cell : cellMachineMap.keySet()) {
                String machine = canProduce(cell, order.getWorkPiece());
                if (machine != null) {
                    cellMachinePieceMap.putIfAbsent(cell, new HashMap<>());
                    cellMachinePieceMap.get(cell).putIfAbsent(machine, new ArrayList<>());
                    cellMachinePieceMap.get(cell).get(machine).add(piece);
                    break;
                }
            }
        }
        return cellMachinePieceMap;
    }

    private String canProduce(String cell, String finalPiece) {
        List<String> machines = cellMachineMap.get(cell);
        // Check the second machine first
        for (int i = machines.size() - 1; i >= 0; i--) {
            String machine = machines.get(i);
            for (String tool : machineToolMap.get(machine)) {
                if (toolTransformationMap.get(tool).contains(finalPiece)) {
                    return machine;
                }
            }
        }
        return null;
    }
}