package org.OPC_UA;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

public class OpcuaClient {

    private OpcUaClient myclient;
    private double maxAge = 0;

    public void connect(String endpointURL){
        try{
            myclient = OpcUaClient.create(endpointURL);
            myclient.connect().get();
            System.out.println("Connected to OPCUA server with endpoint:"+endpointURL);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public DataValue read(String variable, int index){
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        DataValue value;
        try {
            value = myclient.readValue(maxAge, TimestampsToReturn.Both, nodeId).get();
            return value;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean writeWord(String variable, int index, String value) {
        UShort uShort = UShort.valueOf(value);
        String ID =  variable;
        NodeId nodeId = new NodeId(index, ID);
        Variant variant = new Variant(uShort);
        DataValue dataValue = new DataValue(variant);

        try{
            myclient.writeValue(nodeId,dataValue).get();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeInt16(String variable, int index, String value) {
        Short shortValue = Short.valueOf(value);
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        Variant variant = new Variant(shortValue);
        DataValue dataValue = new DataValue(variant);

        try{
            myclient.writeValue(nodeId,dataValue).get();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    public static void main(String[] args) {
        OpcuaClient client = new OpcuaClient();
        client.connect("opc.tcp://localhost:4840");  // replace with your OPC UA server's endpoint URL
        DataValue value = client.read("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C1.offset_I", 4);

        // Now you can get the value and its type
        Object val = value.getValue().getValue();
        System.out.println("Value: " + val);

        //boolean resultWord = client.writeWord("|var|CODESYS Control Win V3 x64.Application.GVL.raulnengue", 4, "10");
        boolean resultInt = client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.raulnengue", 4, "8");



        if (resultInt) {
            System.out.println("Integer value written successfully.");
        } else {
            System.out.println("Failed to write integer value.");
        }


    }
    }
