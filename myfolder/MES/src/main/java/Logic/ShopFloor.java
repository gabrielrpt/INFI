package Logic;
import org.OPC_UA.OPCUAClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.*;

public class ShopFloor {
    private final Map<String, List<String>> cellMachineMap;
    private final Map<String, List<String>> machineToolMap;
    private final Map<String, List<String>> toolTransformationMap;
    private final OPCUAClient client;


    public ShopFloor(OPCUAClient client) {
        this.client = client;
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

    public void processOrder(Orders order) {
        String rawPiece = order.getRawPiece();
        String workPiece = order.getWorkPiece();
        int quantity = order.getQuantity();
        int current = 0;
        int  outPiece2 = 0;

        if (rawPiece.equals("P1")) {
            // Start a new thread for transformP1toP4
            new Thread(() -> transformP1toP4(quantity, current, 1, 3, 3, 4)).start();
            // Start a new thread for checkAndExtractP4
            new Thread(() -> checkAndExtractP4(quantity)).start();
            while (current<quantity){
                //DO NOTHING
            }
            // Call transformP4toFinal
            //transformP4toFinal(quantity, workPiece);
        } else {
            outPiece2 = workPiece.equals("P7") ? 7 : 9;
            transformP2toFinal(quantity, 2, 8, 8, outPiece2);
        }
    }

    private void checkAndExtractP4(int quantity) {
        // Implement this method to check the warehouse for P4 pieces and extract them
        int remainingQuantity = quantity;
        while (remainingQuantity > 0) {
            int P4Quantity = client.getPieceQuantity(4, 2);
            if (P4Quantity > 0) {
                while(P4Quantity > 0) {
                    client.writeWarehouseArray(2, 4, P4Quantity - 1);
                    client.writeWOutPiece(4, 6);
                    P4Quantity--;
                    remainingQuantity--;
                }
            }
        }
    }

    public void transformP2toFinal(int quantity, int inPiece1, int outPiece1, int inPiece2, int outPiece2){
        int unfinishedPieces = quantity;
        while(unfinishedPieces > 0) {

            //NOTE: conveyorNumber offsets go from 5 to 10
            if(isEntryConveyorFree(8)){
                client.writeMInPiece(inPiece1, 6);
                client.writeMOutPiece(outPiece1, 6);
                client.writeWOutPiece(inPiece1, 3);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 7);
            client.writeMOutPiece(outPiece2, 7);

            if(isEntryConveyorFree(9) && quantity > 0){
                client.writeMInPiece(inPiece1, 8);
                client.writeMOutPiece(outPiece1, 8);
                client.writeWOutPiece(inPiece1, 4);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 9);
            client.writeMOutPiece(outPiece2, 9);

            if(isEntryConveyorFree(10) && quantity > 0){
                client.writeMInPiece(inPiece1, 10);
                client.writeMOutPiece(outPiece1, 10);
                client.writeWOutPiece(inPiece1, 5);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 11);
            client.writeMOutPiece(outPiece2, 11);

        }
    }

    public void transformP4toFinal(){
        // Implement this method to transform P4 pieces to the final piece
    }

    public void transformP1toP4(int quantity, int current, int inPiece1, int outPiece1, int inPiece2, int outPiece2){
        while(quantity > 0) {

            //NOTE: conveyorNumber offsets go from 5 to 10
            if(isEntryConveyorFree(5)){
                client.writeMInPiece(inPiece1, 0);
                client.writeMOutPiece(outPiece1, 0);
                client.writeWOutPiece(inPiece1, 0);
                quantity--;
            }

            client.writeMInPiece(inPiece2, 1);
            client.writeMOutPiece(outPiece2, 1);

            if(isEntryConveyorFree(6) && quantity > 0){
                client.writeMInPiece(inPiece1, 2);
                client.writeMOutPiece(outPiece1, 2);
                client.writeWOutPiece(inPiece1, 1);
                quantity--;
            }

            client.writeMInPiece(inPiece2, 3);
            client.writeMOutPiece(outPiece2, 3);

            if(isEntryConveyorFree(7) && quantity > 0){
                client.writeMInPiece(inPiece1, 4);
                client.writeMOutPiece(outPiece1, 4);
                client.writeWOutPiece(inPiece1, 2);
                quantity--;
            }

            client.writeMInPiece(inPiece2, 5);
            client.writeMOutPiece(outPiece2, 5);
        }
    }

    public boolean isEntryConveyorFree(int conveyorNumber) {
        String variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C" + conveyorNumber + ".recv_I";
        DataValue value = client.read(variable, 4);
        if (value != null && value.getValue().getValue() != null) {
            return value.getValue().getValue().equals("0");
        }
        return false;
    }
}


