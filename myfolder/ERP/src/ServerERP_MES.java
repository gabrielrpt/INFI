import java.io.*;
import java.net.*;

public class ServerERP_MES {
    public Order order;
    public void connection() {
        final int PORT = 12345;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connected.");

                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received from client: " + message);
                        String[] parts = message.split(",");

                        // Check if the message has three parts (arrival date, dispatch date, production time)
                        if (parts.length == 3) {
                            // Parse the parts to integers
                            int arrivalDate = Integer.parseInt(parts[0]);
                            int dispatchDate = Integer.parseInt(parts[1]);
                            int productionTime = Integer.parseInt(parts[2]);
                        } else {
                            // Handle invalid message format
                            System.err.println("Invalid message format.");
                        }
                        out.println("Server echoing back: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

