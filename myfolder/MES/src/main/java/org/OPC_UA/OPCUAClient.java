package org.OPC_UA;

import com.google.common.primitives.UnsignedInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import java.time.Duration;
import java.util.*;

public class OPCUAClient {

    private OpcUaClient myclient;

    private Map<String,String> MachineSensorNodeIds;




    public void connect(String endpointURL) {
        try {
            myclient = OpcUaClient.create(endpointURL);
            myclient.connect().get();
            System.out.println("Connected to OPCUA server with endpoint:" + endpointURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataValue read(String variable, int index) {
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        DataValue value;
        try {
            double maxAge = 0;
            value = myclient.readValue(maxAge, TimestampsToReturn.Both, nodeId).get();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean writeWord(String variable, int index, String value) {
        UShort uShort = UShort.valueOf(value);
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        Variant variant = new Variant(uShort);
        DataValue dataValue = new DataValue(variant);

        try {
            myclient.writeValue(nodeId, dataValue).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void resetArrays() {
        for(int i=0; i<24; i++){
            String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.PieceArrivalDay[" + i + "]";
            writeInt16(variable, 4, String.valueOf(0));
            variable = "|var|CODESYS Control Win V3 x64.Application.GVL.PieceDispatchDay[" + i + "]";
            writeInt16(variable, 4, String.valueOf(0));
            variable = "|var|CODESYS Control Win V3 x64.Application.GVL.PieceTime[" + i + "]";
            writeInt16(variable, 4, String.valueOf(0));
        }
    }

    //method that writes the inPiece of a machine in the MInPiece array
    public void writeMInPiece(int inPiece, int machineNumber) {
        String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.MInPiece[" + machineNumber + "]";
        writeInt16(variable, 4, String.valueOf(inPiece));
    }

    public void writeOffset(int firstIndex, boolean resetPieces, int initCell) {
        for(int i=1; i<7; i++){
            String variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M1" + i + ".first_index";
            writeInt16(variable, 4, String.valueOf(firstIndex));
            variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M2" + i + ".first_index";
            writeInt16(variable, 4, String.valueOf(firstIndex));
        }
        if(resetPieces){
            if(firstIndex == 0) {
                for (int i = 1; i < 7; i++) {
                    String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.SupplyPieceNumber";
                    writeInt16(variable, 4, String.valueOf(0));
                    variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M1" + i + ".pieceNumber";
                    writeInt16(variable, 4, String.valueOf(i-1));
                    variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M2" + i + ".pieceNumber";
                    writeInt16(variable, 4, String.valueOf(i-1));
                }
            } else {
                if(initCell == 3){
                    for (int i = 4; i < 7; i++) {
                        String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.SupplyPieceNumber";
                        writeInt16(variable, 4, String.valueOf(0));
                        variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M1" + i + ".pieceNumber";
                        writeInt16(variable, 4, String.valueOf(i-4));
                        variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M2" + i + ".pieceNumber";
                        writeInt16(variable, 4, String.valueOf(i-4));
                    }
                }
                else if(initCell == 0){
                    for (int i = 1; i < 4; i++) {
                        String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.SupplyPieceNumber";
                        writeInt16(variable, 4, String.valueOf(0));
                        variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M1" + i + ".pieceNumber";
                        writeInt16(variable, 4, String.valueOf(i-1));
                        variable = "|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M2" + i + ".pieceNumber";
                        writeInt16(variable, 4, String.valueOf(i-1));
                    }
                }

            }
        }
    }

    public long [] readPieceTimes() {
        long [] pieceTime = new long[24];
        for(int i=0; i<24; i++){
            String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.PieceTime[" + i + "]";
            pieceTime[i] = readInt64(variable, 4);
            System.out.println("Piece Time: " + pieceTime[i]);
        }
        return pieceTime;
    }

    public int [] readPieceArrivalDay() {
        int [] pieceArrivalDays = new int[24];
        for(int i=0; i<24; i++){
            String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.PieceArrivalDay[" + i + "]";
            pieceArrivalDays[i] = readInt16(variable, 4);
        }
        return pieceArrivalDays;
    }

    public int []  readPieceDispatchDay() {
        int [] pieceDispatchDays = new int[24];
        for(int i=0; i<24; i++){
            String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.PieceDispatchDay[" + i + "]";
            pieceDispatchDays[i] = readInt16(variable, 4);
        }
        return pieceDispatchDays;
    }


    //method that writes the outPiece of a machine in the MOutPiece array
    public void writeMOutPiece(int outPiece, int machineNumber) {
        String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.MOutPiece[" + machineNumber + "]";
        writeInt16(variable, 4, String.valueOf(outPiece));
    }

    public void writeWOutPiece(int outPiece, int conveyorNumber) {
        String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.registers[" + conveyorNumber + "]";
        writeWord(variable, 4, String.valueOf(outPiece));
    }

    public boolean writeInt16(String variable, int index, String value) {
        Short shortValue = Short.valueOf(value);
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        Variant variant = new Variant(shortValue);
        DataValue dataValue = new DataValue(variant);

        try {
            myclient.writeValue(nodeId, dataValue).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeInt64(String variable, int index, String value) {
        Long longValue;
        try {
            longValue = Long.valueOf(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + value);
            return false;
        }

        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        Variant variant = new Variant(longValue);
        DataValue dataValue = new DataValue(variant);

        try {
            myclient.writeValue(nodeId, dataValue).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int[] readWarehouseArray(String variable, int index) {
        int[] wareHouse = new int[10];
        for (int i = 0; i < 10; i++) {
            DataValue arrayvalue = read(variable + "[" + i + "]", index);
            if (arrayvalue != null && arrayvalue.getValue().getValue() != null) {
                Object value = arrayvalue.getValue().getValue();
                int quantity;
                if (value instanceof UnsignedInteger) {
                    quantity = ((UnsignedInteger) value).intValue();
                } else if (value instanceof Short) {
                    quantity = ((Short) value).intValue();
                } else {
                    throw new IllegalArgumentException("Unexpected type: " + value.getClass());
                }
                wareHouse[i] = quantity; // store the count of P(i) pieces
            }
        }
        return wareHouse;
    }

    public int getPieceQuantity(int pieceType, int warehouseNumber) {
        // Construct the warehouse node string based on the provided warehouse number
        String warehouseNode = "|var|CODESYS Control Win V3 x64.Application.GVL.W" + warehouseNumber;

        // Read the warehouse array
        int[] warehouseArray = readWarehouseArray(warehouseNode, 4);
        // Return the quantity of the specified piece type
        return warehouseArray[pieceType];
    }

    public boolean writeWarehouseArray(int warehouseNumber, int position, int value) {
        // Construct the warehouse node string based on the provided warehouse number
        String warehouseNode;
        if (warehouseNumber == 1) {
            String warehouse1node = "|var|CODESYS Control Win V3 x64.Application.GVL.W1";
            warehouseNode = warehouse1node;
        } else if (warehouseNumber == 2) {
            String warehouse2node = "|var|CODESYS Control Win V3 x64.Application.GVL.W2";
            warehouseNode = warehouse2node;
        } else {
            throw new IllegalArgumentException("Invalid warehouse number: " + warehouseNumber);
        }

        // Construct the node string for the specific position in the array
        String node = warehouseNode + "[" + position + "]";

        // Use the writeInt16 method to write the value to the specific position in the array
        return writeInt16(node, 4, String.valueOf(value));
    }
    public Duration readTime(String variable, int index) {
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        DataValue value;
        try {
            double maxAge = 0;
            value = myclient.readValue(maxAge, TimestampsToReturn.Both, nodeId).get();
            if (value != null) {
                Object timeValue = value.getValue().getValue();
                if (timeValue instanceof ULong) {
                    return Duration.ofMillis(((ULong) timeValue).longValue());
                } else if (timeValue instanceof Long) {
                    return Duration.ofMillis((Long) timeValue);
                } else {
                    throw new IllegalArgumentException("Unexpected type: " + timeValue.getClass());
                }
            } else {
                throw new IllegalArgumentException("Value is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int readInt16(String variable, int index) {
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        DataValue value;
        try {
            double maxAge = 0;
            value = myclient.readValue(maxAge, TimestampsToReturn.Both, nodeId).get();
            if (value != null) {
                Object intValue = value.getValue().getValue();
                if (intValue instanceof Short) {
                    return ((Short) intValue).intValue();
                } else {
                    throw new IllegalArgumentException("Unexpected type: " + intValue.getClass());
                }
            } else {
                throw new IllegalArgumentException("Value is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long readInt64(String variable, int index) {
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        DataValue value;
        try {
            double maxAge = 0;
            value = myclient.readValue(maxAge, TimestampsToReturn.Both, nodeId).get();
            if (value != null) {
                Object intValue = value.getValue().getValue();
                if (intValue instanceof Long) {
                    return ((Long) intValue).longValue();
                } else {
                    throw new IllegalArgumentException("Unexpected type: " + intValue.getClass());
                }
            } else {
                throw new IllegalArgumentException("Value is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }





    // isto e para teste, depois apagar main
    public static void main(String[] args) {
        OPCUAClient client = new OPCUAClient();
        client.connect("opc.tcp://localhost:4840");  // replace with your OPC UA server's endpoint URL
        DataValue value = client.read("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C1.offset_I", 4);
        //"|var|CODESYS Control Win V3 x64.Application.PLC_PRG.WC11.in_piece"
        // Now you can get the value and its type
        Object val = value.getValue().getValue();
        System.out.println("Value: " + val);

        //boolean resultWord = client.writeWord("|var|CODESYS Control Win V3 x64.Application.GVL.raulnengue", 4, "10");
        boolean resultInt = client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.raulnengue", 4, "8");
        //client.writeWOutPiece(1, 3);

        if (resultInt) {
            System.out.println("Integer value written successfully.");
        } else {
            System.out.println("Failed to write integer value.");
        }
        int[] arrayValues = client.readWarehouseArray("|var|CODESYS Control Win V3 x64.Application.GVL.test", 4); // por o array certo depois!!!

        // Print the values of the array
        System.out.println("Array values:");
        for (int value2 : arrayValues) {
            System.out.println(value2);

        }
    }
}