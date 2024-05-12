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





    //method that writes the inPiece of a machine in the MInPiece array
    public void writeMInPiece(int inPiece, int machineNumber) {
        String variable = "|var|CODESYS Control Win V3 x64.Application.GVL.MInPiece[" + machineNumber + "]";
        writeInt16(variable, 4, String.valueOf(inPiece));
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







// isto e para teste, depois apagar main
    public static void main(String[] args) {
        OPCUAClient client = new OPCUAClient();
        client.connect("opc.tcp://localhost:4840");  // replace with your OPC UA server's endpoint URL
        DataValue value = client.read("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C1.offset_I", 4);

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