package Logic;
import database.javaDatabase;
import org.OPC_UA.OPCUAClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;


public class ShopFloor {
    private final OPCUAClient client;

    public ShopFloor(OPCUAClient client) {
        this.client = client;
    }


    public void processOrder(Orders order) throws SQLException {
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
            new Thread(() -> checkAndExtractPX(quantity, current, 4, 99)).start();
            while (true){
                //Wait for the transformation to finish
                if(current.get() == quantity){
                    System.out.println("All P1 pieces have been transformed to P4");
                    break;
                }
            }
            transformP4toFinal(quantity, 4, finalPiece);
            checkAndExtractPX(quantity, current, finalPiece, 99);
            //print a string saying that the order has been completed
            javaDatabase.setOrderComplete(order.getOrderNumber());
            System.out.println("Order completed");
        } else {
            outPiece2 = workPiece.equals("P7") ? 7 : 9;
            if(outPiece2 == 7) {
                transformP2toP7(quantity, 2, 8, 8, outPiece2);
                checkAndExtractPX(quantity, current, finalPiece, 99);
            } else {
                // Start a new thread for transformP2toP8
                new Thread(() -> transformPXtoPX(quantity, 2,8)).start();
                // Start a new thread for checkAndExtractP4
                new Thread(() -> checkAndExtractPX(quantity, current, 8, 99)).start();
                while (true){
                    //Wait for the transformation to finish
                    if(current.get() == quantity){
                        System.out.println("All P2 pieces have been transformed to P8");
                        break;
                    }
                }
                transformPXtoPX(quantity, 8, finalPiece);
                checkAndExtractPX(quantity, current, finalPiece, 99);
            }
            //print a string saying that the order has been completed
            javaDatabase.setOrderComplete(order.getOrderNumber());
            System.out.println("Order completed");
        }
    }


    private void checkAndExtractPX(int quantity, AtomicInteger current, int piece, int conveyorNumber) {
        // Implement this method to check the warehouse for P4 pieces and extract them
        int remainingQuantity = quantity;
        if (piece == 4 || (piece == 8 && conveyorNumber == 99)){
            while (remainingQuantity > 0) {
                int PxQuantity = client.getPieceQuantity(piece, 2);

                if (PxQuantity > 0) {
                    // print the quantity of P4 pieces in the warehouse
                    System.out.println("Pieces in the warehouse: " + PxQuantity);
                    while(PxQuantity > 0) {
                        if(isEntryConveyorFree(16)){
                            client.writeWarehouseArray(2, piece, client.getPieceQuantity(piece, 2) - 1);
                            client.writeWOutPiece(piece, 6);
                            client.writeInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.WC11.in_piece", 4, String.valueOf(piece));
                            PxQuantity--;
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
                if(piece == 5 || piece == 6 || piece == 7 || piece == 9){
                    // Implement this method to check the warehouse for P4 pieces and extract them
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
                } else {
                    int P4Quantity = client.getPieceQuantity(piece, 1);
                    if (P4Quantity > 0) {
                        while(P4Quantity > 0) {
                            client.writeWarehouseArray(1, piece, client.getPieceQuantity(piece, 1) - 1);
                            client.writeWOutPiece(piece, conveyorNumber);
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
    }


    public void transformP2toP7(int quantity, int inPiece1, int outPiece1, int inPiece2, int outPiece2){
        int unfinishedPieces = quantity;
        while(unfinishedPieces > 0) {
            //print
            System.out.println("Transforming P2 to final piece");
            //NOTE: conveyorNumber offsets go from 5 to 10
            if(isEntryConveyorFree(8)){
                client.writeMInPiece(inPiece1, 6);
                client.writeMOutPiece(outPiece1, 6);
                client.writeMInPiece(inPiece2, 7);
                client.writeMOutPiece(outPiece2, 7);
                client.writeWOutPiece(inPiece1, 3);
                unfinishedPieces--;
                try {
                    Thread.sleep(2000); // delay of 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if(isEntryConveyorFree(9) && unfinishedPieces > 0){
                client.writeMInPiece(inPiece1, 8);
                client.writeMOutPiece(outPiece1, 8);
                client.writeMInPiece(inPiece2, 9);
                client.writeMOutPiece(outPiece2, 9);
                client.writeWOutPiece(inPiece1, 4);
                unfinishedPieces--;
                try {
                    Thread.sleep(2000); // delay of 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(isEntryConveyorFree(10) && unfinishedPieces > 0){
                client.writeMInPiece(inPiece1, 10);
                client.writeMOutPiece(outPiece1, 10);
                client.writeMInPiece(inPiece2, 11);
                client.writeMOutPiece(outPiece2, 11);
                client.writeWOutPiece(inPiece1, 5);
                unfinishedPieces--;
                try {
                    Thread.sleep(2000); // delay of 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

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
                    client.writeMInPiece(outPiece1, 7);
                    client.writeMOutPiece(outPiece1, 7);
                    client.writeWOutPiece(inPiece1, 3);
                    quantity--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(isEntryConveyorFree(9) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 8);
                    client.writeMOutPiece(outPiece1, 8);
                    client.writeMInPiece(outPiece1, 9);
                    client.writeMOutPiece(outPiece1, 9);
                    client.writeWOutPiece(inPiece1, 4);
                    quantity--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(isEntryConveyorFree(10) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 10);
                    client.writeMOutPiece(outPiece1, 10);
                    client.writeMInPiece(outPiece1, 11);
                    client.writeMOutPiece(outPiece1, 11);
                    client.writeWOutPiece(inPiece1, 5);
                    quantity--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

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
                    client.writeMInPiece(outPiece1, 1);
                    client.writeMOutPiece(outPiece1, 1);
                    client.writeWOutPiece(inPiece1, 0);
                    client.writeWarehouseArray(1, inPiece1, client.getPieceQuantity(inPiece1, 1) - 1);

                    System.out.println("Conveyor 5 is free");
                    quantity--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(isEntryConveyorFree(6) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 2);
                    client.writeMOutPiece(outPiece1, 2);
                    client.writeMInPiece(outPiece1, 3);
                    client.writeMOutPiece(outPiece1, 3);
                    client.writeWOutPiece(inPiece1, 1);
                    client.writeWarehouseArray(1, inPiece1, client.getPieceQuantity(inPiece1, 1) - 1);
                    quantity--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(isEntryConveyorFree(7) && quantity > 0 && client.getPieceQuantity(inPiece1, 1)!=0){
                    client.writeMInPiece(inPiece1, 4);
                    client.writeMOutPiece(outPiece1, 4);
                    client.writeMInPiece(outPiece1, 5);
                    client.writeMOutPiece(outPiece1, 5);
                    client.writeWOutPiece(inPiece1, 2);
                    client.writeWarehouseArray(1, inPiece1, client.getPieceQuantity(inPiece1, 1) - 1);
                    quantity--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

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
                client.writeMInPiece(inPiece2, 1);
                client.writeMOutPiece(outPiece2, 1);
                checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 0);
                unfinishedPieces--;
                try {
                    Thread.sleep(2000); // delay of 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            if(isEntryConveyorFree(6) && unfinishedPieces > 0){
                System.out.println("Conveyor 6 is free");
                client.writeMInPiece(inPiece1, 2);
                client.writeMOutPiece(outPiece1, 2);
                client.writeMInPiece(inPiece2, 3);
                client.writeMOutPiece(outPiece2, 3);
                checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 1);
                unfinishedPieces--;
                try {
                    Thread.sleep(2000); // delay of 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            if(isEntryConveyorFree(7) && unfinishedPieces > 0){
                System.out.println("Conveyor 7 is free");
                client.writeMInPiece(inPiece1, 4);
                client.writeMOutPiece(outPiece1, 4);
                client.writeMInPiece(inPiece2, 5);
                client.writeMOutPiece(outPiece2, 5);
                checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 2);
                unfinishedPieces--;
                try {
                    Thread.sleep(2000); // delay of 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            try {
                Thread.sleep(2000); // delay of 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void transformPXtoPX(int quantity, int inPiece1, int outPiece1){
        int unfinishedPieces = quantity;
        System.out.println("Transforming P2 to P8");
        while(unfinishedPieces > 0) {
            //NOTE: conveyorNumber offsets go from 5 to 10
            if(inPiece1 == 2) {
                if (isEntryConveyorFree(5)) {
                    System.out.println("Conveyor 5 is free");
                    client.writeMInPiece(inPiece1, 1);
                    client.writeMOutPiece(outPiece1, 1);
                    client.writeMInPiece(0, 0);
                    client.writeMOutPiece(0, 0);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 0);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if (isEntryConveyorFree(6) && unfinishedPieces > 0) {
                    System.out.println("Conveyor 6 is free");
                    client.writeMInPiece(0, 2);
                    client.writeMOutPiece(0, 2);
                    client.writeMInPiece(inPiece1, 3);
                    client.writeMOutPiece(outPiece1, 3);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 1);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if (isEntryConveyorFree(7) && unfinishedPieces > 0) {
                    System.out.println("Conveyor 7 is free");
                    client.writeMInPiece(0, 4);
                    client.writeMOutPiece(0, 4);
                    client.writeMInPiece(inPiece1, 5);
                    client.writeMOutPiece(outPiece1, 5);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 2);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if(isEntryConveyorFree(8) && unfinishedPieces > 0){
                    System.out.println("Conveyor 8 is free");
                    client.writeMInPiece(0, 6);
                    client.writeMOutPiece(0, 6);
                    client.writeMInPiece(inPiece1, 7);
                    client.writeMOutPiece(outPiece1, 7);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 3);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



                if(isEntryConveyorFree(9) && unfinishedPieces > 0){
                    System.out.println("Conveyor 9 is free");
                    client.writeMInPiece(0, 8);
                    client.writeMOutPiece(0, 8);
                    client.writeMInPiece(inPiece1, 9);
                    client.writeMOutPiece(outPiece1, 9);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 4);
                    unfinishedPieces--;
                    try {
                       Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                   }
              }



                if(isEntryConveyorFree(10) && unfinishedPieces > 0){
                    System.out.println("Conveyor 10 is free");
                    client.writeMInPiece(0, 10);
                    client.writeMOutPiece(0, 10);
                    client.writeMInPiece(inPiece1, 11);
                    client.writeMOutPiece(outPiece1, 11);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 5);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if(isEntryConveyorFree(8)){
                    System.out.println("Conveyor 8 is free");
                    client.writeMInPiece(inPiece1, 6);
                    client.writeMOutPiece(outPiece1, 6);
                    client.writeMInPiece(0, 7);
                    client.writeMOutPiece(0, 7);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 3);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(isEntryConveyorFree(9) && unfinishedPieces > 0){
                    System.out.println("Conveyor 9 is free");
                    client.writeMInPiece(inPiece1, 8);
                    client.writeMOutPiece(outPiece1, 8);
                    client.writeMInPiece(0, 9);
                    client.writeMOutPiece(0, 9);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 4);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



                if(isEntryConveyorFree(10) && unfinishedPieces > 0){
                    System.out.println("Conveyor 10 is free");
                    client.writeMInPiece(inPiece1, 10);
                    client.writeMOutPiece(outPiece1, 10);
                    client.writeMInPiece(0, 11);
                    client.writeMOutPiece(0, 11);
                    checkAndExtractPX(1, new AtomicInteger(0), inPiece1, 5);
                    unfinishedPieces--;
                    try {
                        Thread.sleep(2000); // delay of 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


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


