package Logic;
import org.OPC_UA.OPCUAClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;


public class ShopFloor {
    private final OPCUAClient client;

    public ShopFloor(OPCUAClient client) {
        this.client = client;
    }


    public void processOrder(Orders order) {
        String rawPiece = order.getRawPiece();
        String workPiece = order.getWorkPiece();
        int finalPiece = order.getWorkPieceNumber();
        int quantity = order.getQuantity();
        int current = 0;
        int  outPiece2;

        if (rawPiece.equals("P1")) {
            // Start a new thread for transformP1toP4
            new Thread(() -> transformP1toP4(quantity, 1, 3, 3, 4)).start();
            // Start a new thread for checkAndExtractP4
            new Thread(() -> checkAndExtractPX(quantity, current, 4)).start();
            while (true){
                //DO NOTHING
                if(current == quantity) break;
            }
            transformP4toFinal(quantity, 4, finalPiece);
            checkAndExtractPX(quantity, current, finalPiece);
        } else {
            outPiece2 = workPiece.equals("P7") ? 7 : 9;
            transformP2toFinal(quantity, 2, 8, 8, outPiece2);
            checkAndExtractPX(quantity, current, finalPiece);
        }
    }

    private void checkAndExtractPX(int quantity, int current, int piece) {
        // Implement this method to check the warehouse for P4 pieces and extract them
        int remainingQuantity = quantity;
        if (piece == 4){
            while (remainingQuantity > 0) {
                int P4Quantity = client.getPieceQuantity(4, 2);
                if (P4Quantity > 0) {
                    while(P4Quantity > 0) {
                        client.writeWarehouseArray(2, 4, P4Quantity - 1);
                        client.writeWOutPiece(4, 6);
                        P4Quantity--;
                        current++;
                        remainingQuantity--;
                    }
                }
            }
        } else {
            while (remainingQuantity > 0) {
                int P4Quantity = client.getPieceQuantity(piece, 2);
                if (P4Quantity > 0) {
                    while(P4Quantity > 0) {
                        client.writeWarehouseArray(2, piece, P4Quantity - 1);
                        client.writeWOutPiece(piece, 7);
                        P4Quantity--;
                        current++;
                        remainingQuantity--;
                    }
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

            if(isEntryConveyorFree(9) && unfinishedPieces > 0){
                client.writeMInPiece(inPiece1, 8);
                client.writeMOutPiece(outPiece1, 8);
                client.writeWOutPiece(inPiece1, 4);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 9);
            client.writeMOutPiece(outPiece2, 9);

            if(isEntryConveyorFree(10) && unfinishedPieces > 0){
                client.writeMInPiece(inPiece1, 10);
                client.writeMOutPiece(outPiece1, 10);
                client.writeWOutPiece(inPiece1, 5);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 11);
            client.writeMOutPiece(outPiece2, 11);

        }
    }

    public void transformP4toFinal(int quantity, int inPiece1, int outPiece1){
        // Implement this method to transform P4 pieces to the final piece
        if (outPiece1 ==5){
            while(quantity > 0) {
                if(isEntryConveyorFree(8)){
                    client.writeMInPiece(inPiece1, 6);
                    client.writeMOutPiece(outPiece1, 6);
                    client.writeWOutPiece(inPiece1, 3);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 7);
                client.writeMOutPiece(outPiece1, 7);

                if(isEntryConveyorFree(9) && quantity > 0){
                    client.writeMInPiece(inPiece1, 8);
                    client.writeMOutPiece(outPiece1, 8);
                    client.writeWOutPiece(inPiece1, 4);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 9);
                client.writeMOutPiece(outPiece1, 9);

                if(isEntryConveyorFree(10) && quantity > 0){
                    client.writeMInPiece(inPiece1, 10);
                    client.writeMOutPiece(outPiece1, 10);
                    client.writeWOutPiece(inPiece1, 5);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 11);
                client.writeMOutPiece(outPiece1, 11);
            }
        } else {
            while(quantity > 0) {
                if(isEntryConveyorFree(5)){
                    client.writeMInPiece(inPiece1, 0);
                    client.writeMOutPiece(outPiece1, 0);
                    client.writeWOutPiece(inPiece1, 0);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 1);
                client.writeMOutPiece(outPiece1, 1);

                if(isEntryConveyorFree(6) && quantity > 0){
                    client.writeMInPiece(inPiece1, 2);
                    client.writeMOutPiece(outPiece1, 2);
                    client.writeWOutPiece(inPiece1, 1);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 3);
                client.writeMOutPiece(outPiece1, 3);

                if(isEntryConveyorFree(7) && quantity > 0){
                    client.writeMInPiece(inPiece1, 4);
                    client.writeMOutPiece(outPiece1, 4);
                    client.writeWOutPiece(inPiece1, 2);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 5);
                client.writeMOutPiece(outPiece1, 5);
            }
        }
    }


    public void transformP1toP4(int quantity, int inPiece1, int outPiece1, int inPiece2, int outPiece2){
        int unfinishedPieces = quantity;
        while(unfinishedPieces > 0) {
            //NOTE: conveyorNumber offsets go from 5 to 10
            if(isEntryConveyorFree(5)){
                client.writeMInPiece(inPiece1, 0);
                client.writeMOutPiece(outPiece1, 0);
                client.writeWOutPiece(inPiece1, 0);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 1);
            client.writeMOutPiece(outPiece2, 1);

            if(isEntryConveyorFree(6) && unfinishedPieces > 0){
                client.writeMInPiece(inPiece1, 2);
                client.writeMOutPiece(outPiece1, 2);
                client.writeWOutPiece(inPiece1, 1);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 3);
            client.writeMOutPiece(outPiece2, 3);

            if(isEntryConveyorFree(7) && unfinishedPieces > 0){
                client.writeMInPiece(inPiece1, 4);
                client.writeMOutPiece(outPiece1, 4);
                client.writeWOutPiece(inPiece1, 2);
                unfinishedPieces--;
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


