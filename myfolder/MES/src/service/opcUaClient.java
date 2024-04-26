package service;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;

import static java.net.URI.create;

import Comms.opcUa;

public class opcUaClient {

    private opcUaClient opClient;

    //private String identifier = "|var|CODESYS Control Win V3 x64.Application.GVL.";

    public void connect(String endpointURL) {
        try {
            opClient = opcUaClient.create(endpointURL);
            opClient.connect().get();
            System.out.println("Connected to OPCUA server with endpoint:" + endpointURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
