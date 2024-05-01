package org.OPC_UA;

import Statistics.M11timer;
import Statistics.M12timer;
import Statistics.M13timer;
import Statistics.M14timer;
import Statistics.M15timer;
import Statistics.M16timer;
import Statistics.M21timer;
import Statistics.M22timer;
import Statistics.M23timer;
import Statistics.M24timer;
import Statistics.M25timer;
import Statistics.M26timer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

public class OpcuaClient {

    private static OpcUaClient myclient;
    private static double maxAge = 0;

    public void connect(String endpointURL) {
        try {
            myclient = OpcUaClient.create(endpointURL);
            myclient.connect().get();
            System.out.println("Connected to OPCUA server with endpoint:" + endpointURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataValue read(String variable, int index) {
        String ID = variable;
        NodeId nodeId = new NodeId(index, ID);
        DataValue value;
        try {
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


    public static void main(String[] args) {
        OpcuaClient client = new OpcuaClient();
        client.connect("opc.tcp://localhost:4840");  // replace with your OPC UA server's endpoint URL
        DataValue value = client.read("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C10.offset_O", 4);

        // Now you can get the value and its type
        Object val = value.getValue().getValue();
        System.out.println("Value: " + val);

        //boolean resultWord = client.writeWord("|var|CODESYS Control Win V3 x64.Application.GVL.raulnengue", 4, "10");
        boolean resultInt = client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.raulnengue", 4, "8");

        stats();

        if (resultInt) {
            System.out.println("Integer value written successfully.");
        } else {
            System.out.println("Failed to write integer value.");
        }


    }


    public static void stats() {
        M11timer m11 = new M11timer();
        m11.M11_timer();

        M12timer m12 = new M12timer();
        m12.M12_timer();

        M13timer m13 = new M13timer();
        m13.M13_timer();

        M14timer m14 = new M14timer();
        m14.M14_timer();

        M15timer m15 = new M15timer();
        m15.M15_timer();

        M16timer m16 = new M16timer();
        m16.M16_timer();

        M21timer m21 = new M21timer();
        m21.M21_timer();

        M22timer m22 = new M22timer();
        m22.M22_timer();

        M23timer m23 = new M23timer();
        m23.M23_timer();

        M24timer m24 = new M24timer();
        m24.M24_timer();

        M25timer m25 = new M25timer();
        m25.M25_timer();

        M26timer m26 = new M26timer();
        m26.M26_timer();
    }
}