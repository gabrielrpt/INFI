package Logic;
import org.OPC_UA.OPCUAClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.concurrent.atomic.AtomicInteger;


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
        AtomicInteger current = new AtomicInteger(0);
        int  outPiece2;

        if (rawPiece.equals("P1")) {
            System.out.println("Processing order for P1 pieces");
            // Start a new thread for transformP1toP4
            new Thread(() -> transformP1toP4(quantity, 1, 3, 3, 4)).start();
            // Start a new thread for checkAndExtractP4
            new Thread(() -> checkAndExtractPX(quantity, current, 4)).start();
            while (true){
                //Wait for the transformation to finish
                if(current.get() == quantity){
                    System.out.println("All P1 pieces have been transformed to P4");
                    break;
                }
            }
            transformP4toFinal(quantity, 4, finalPiece);
            checkAndExtractPX(quantity, current, finalPiece);
        } else {
            outPiece2 = workPiece.equals("P7") ? 7 : 9;
            transformP2toFinal(quantity, 2, 8, 8, outPiece2);
            checkAndExtractPX(quantity, current, finalPiece);
        }
    }

    private void checkAndExtractPX(int quantity, AtomicInteger current, int piece) {
        // Implement this method to check the warehouse for P4 pieces and extract them
        int remainingQuantity = quantity;
        if (piece == 4){
            while (remainingQuantity > 0) {
                int P4Quantity = client.getPieceQuantity(4, 2);

                if (P4Quantity > 0) {
                    // print the quantity of P4 pieces in the warehouse
                    System.out.println("P4 pieces in the warehouse: " + P4Quantity);
                    while(P4Quantity > 0) {
                        if(isEntryConveyorFree(16)){
                            client.writeWarehouseArray(2, 4, client.getPieceQuantity(4, 2) - 1);
                            client.writeWOutPiece(4, 6);
                            P4Quantity--;
                            current.incrementAndGet();
                            remainingQuantity--;
                            //print the value of current
                            System.out.println("Current: " + current);
                            try {
                                Thread.sleep(3000); // delay of 3 seconds
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            while (remainingQuantity > 0) {
                int P4Quantity = client.getPieceQuantity(piece, 2);
                if (P4Quantity > 0) {
                    while(P4Quantity > 0) {
                        client.writeWarehouseArray(2, piece, client.getPieceQuantity(piece, 2) - 1);
                        client.writeWOutPiece(piece, 7);
                        P4Quantity--;
                        remainingQuantity--;
                        try {
                            Thread.sleep(3000); // delay of 1 second
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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

            try {
                Thread.sleep(2000); // delay of 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void transformP4toFinal(int quantity, int inPiece1, int outPiece1){
        // Implement this method to transform P4 pieces to the final piece
        System.out.println("Transforming P4 to final piece");
        if (outPiece1 ==5){
            while(quantity > 0) {
                if(isEntryConveyorFree(8) && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 6);
                    client.writeMOutPiece(outPiece1, 6);
                    client.writeWOutPiece(inPiece1, 3);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 7);
                client.writeMOutPiece(outPiece1, 7);

                if(isEntryConveyorFree(9) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 8);
                    client.writeMOutPiece(outPiece1, 8);
                    client.writeWOutPiece(inPiece1, 4);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 9);
                client.writeMOutPiece(outPiece1, 9);

                if(isEntryConveyorFree(10) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 10);
                    client.writeMOutPiece(outPiece1, 10);
                    client.writeWOutPiece(inPiece1, 5);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 11);
                client.writeMOutPiece(outPiece1, 11);

                try {
                    Thread.sleep(3000); // delay of 3 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            while(quantity > 0) {
                if(isEntryConveyorFree(5) && (client.getPieceQuantity(inPiece1, 1)>0)){
                    client.writeMInPiece(inPiece1, 0);
                    client.writeMOutPiece(outPiece1, 0);
                    client.writeWOutPiece(inPiece1, 0);
                    client.writeWarehouseArray(1, inPiece1, client.getPieceQuantity(inPiece1, 1) - 1);

                    System.out.println("Conveyor 5 is free");
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 1);
                client.writeMOutPiece(outPiece1, 1);

                if(isEntryConveyorFree(6) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 2);
                    client.writeMOutPiece(outPiece1, 2);
                    client.writeWOutPiece(inPiece1, 1);
                    client.writeWarehouseArray(1, inPiece1, client.getPieceQuantity(inPiece1, 1) - 1);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 3);
                client.writeMOutPiece(outPiece1, 3);

                if(isEntryConveyorFree(7) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 4);
                    client.writeMOutPiece(outPiece1, 4);
                    client.writeWOutPiece(inPiece1, 2);
                    client.writeWarehouseArray(1, inPiece1, client.getPieceQuantity(inPiece1, 1) - 1);
                    quantity--;
                }
                client.writeMInPiece(outPiece1, 5);
                client.writeMOutPiece(outPiece1, 5);

                try {
                    Thread.sleep(3000); // delay of 3 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void transformP1toP4(int quantity, int inPiece1, int outPiece1, int inPiece2, int outPiece2){
        int unfinishedPieces = quantity;
        System.out.println("Transforming P1 to P4");
        while(unfinishedPieces > 0) {
            //NOTE: conveyorNumber offsets go from 5 to 10
            if(isEntryConveyorFree(5)){
                System.out.println("Conveyor 5 is free");
                client.writeMInPiece(inPiece1, 0);
                client.writeMOutPiece(outPiece1, 0);
                client.writeWOutPiece(inPiece1, 0);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 1);
            client.writeMOutPiece(outPiece2, 1);

            if(isEntryConveyorFree(6) && unfinishedPieces > 0){
                System.out.println("Conveyor 6 is free");
                client.writeMInPiece(inPiece1, 2);
                client.writeMOutPiece(outPiece1, 2);
                client.writeWOutPiece(inPiece1, 1);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 3);
            client.writeMOutPiece(outPiece2, 3);

            if(isEntryConveyorFree(7) && unfinishedPieces > 0){
                System.out.println("Conveyor 7 is free");
                client.writeMInPiece(inPiece1, 4);
                client.writeMOutPiece(outPiece1, 4);
                client.writeWOutPiece(inPiece1, 2);
                unfinishedPieces--;
            }

            client.writeMInPiece(inPiece2, 5);
            client.writeMOutPiece(outPiece2, 5);

            try {
                Thread.sleep(2000); // delay of 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isEntryConveyorFree(int conveyorNumber) {
        String variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C" + conveyorNumber + ".recv_I";
        DataValue value = client.read(variable, 4);
        if (value != null && value.getValue().getValue() != null) {
            // Compare with boolean false, not string "false"
            return Boolean.FALSE.equals(value.getValue().getValue());
        }
        return false;
    }
}


