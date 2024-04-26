package database;

import java.sql.*;

public class javaDatabase {

        static Statement stmt;
        static ResultSet rs;
        static Connection connection;
        static String databaseUrl = "jdbc:postgresql://db.fe.up.pt:5432/infind202419";
        static String user = "infind202419";
        static String password = "m6Fhd32pLt";
        static String table = "orders";


        public static void databaseConnection(String databaseUrl, String user, String password) throws SQLException {
            try {
                connection = DriverManager.getConnection(databaseUrl, user, password);

            } catch (SQLException e) {
                throw e;
            }
        }

        public boolean isConnected() throws SQLException {
            try (Connection connection = DriverManager.getConnection(databaseUrl, user, password)) {
                return true; // Connection established if no exception
            } catch (SQLException e) {
                return false;
            }
        }

    /* To use databaseConnection
    Database db = new Database();
     try {
        db.databaseConnection(databaseUrl, user, password);
        // Connection logic would be inside the createConnection method (optional)
     } catch (SQLException e) {
        // Handle connection errors
     }*/

    public static int newEntry(String SQLQuery, String databaseUrl, String user, String password) throws SQLException {

        databaseConnection(databaseUrl,user,password);

        try{
            stmt = connection.createStatement();
            int i = stmt.executeUpdate(SQLQuery);
            connection.close();
            return i;

        }catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return -1;
        }
    }

    // Create method insertOrder
    public static int insertOrder(String orderNumber, String workPiece, int quantity, int dueDate, double latePenalty, double earlyPenalty) throws SQLException {
        String SQLQuery = "INSERT INTO " + table + " (orderNumber, workPiece, quantity, dueDate, latePenalty, earlyPenalty) VALUES ('" + orderNumber + "', '" + workPiece + "', " + quantity + ", " + dueDate + ", " + latePenalty + ", " + earlyPenalty + ");";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static int insertOrderCost(double orderCost) throws SQLException {
        String SQLQuery = "INSERT INTO " + table + " (orderCost) VALUES ('" + orderCost + "');";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }
    public static String getUser() {
        return user;
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public static String getPassword() {
        return password;
    }
    public static String getTable() {
        return table;
    }
}
