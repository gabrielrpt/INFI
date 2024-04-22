package Comms;

public class opcUaClient {

    private OpcUaClient opClient;

    private String identifier = "|var|CODESYS Control Win V3 x64.Application.GVL.";

    public void connect(String endpointURL) {
        try {
            myclient = OpcUaClient.create(endpointURL);
            myclient.connect().get();
            System.out.println("Connected to OPCUA server with endpoint:" + endpointURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
