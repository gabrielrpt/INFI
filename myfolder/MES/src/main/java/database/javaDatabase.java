package database;

import java.sql.*;

public class javaDatabase {

    static Statement stmt;
    static ResultSet rs;
    static Connection connection;
    static String databaseUrl = "jdbc:postgresql://db.fe.up.pt:5432/infind202419";
    static String user = "infind202419";
    static String password = "m6Fhd32pLt";
    static String ordersTable = "orders";
    static String piecesTable = "pieces";


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
        String SQLQuery = "INSERT INTO erpmes." + ordersTable + " (ordernumber, workpiece, quantity, duedate, latepen, earlypen) VALUES ('" + orderNumber + "', '" + workPiece + "', " + quantity + ", " + dueDate + ", " + latePenalty + ", " + earlyPenalty + ");";
        System.out.println(SQLQuery);
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static int insertOrderCost(double orderCost) throws SQLException {
        String SQLQuery = "UPDATE erpmes." + ordersTable + " SET ordercost = "+ orderCost +" WHERE ordernumber = '124';";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static int insertPiece(String pieceName, String rawPiece, int orderNumber, double rawCost) throws SQLException {
        String SQLQuery = "INSERT INTO erpmes." + piecesTable + " (piecetype, rawpiece, orderid, currenttype, rawcost) VALUES ('" + pieceName + "', '" + rawPiece + "', " + orderNumber + ", '" + rawPiece + "', " + rawCost + ");";
        return newEntry(SQLQuery, databaseUrl, user, password);
    }

    public static ResultSet getPieceByOrderNumber(String orderNumber) {
        ResultSet resultSet = null;
        try {
            Connection connection = DriverManager.getConnection(databaseUrl, user, password);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String SQLQuery = "SELECT * FROM erpmes.pieces WHERE orderid = '" + orderNumber + "'";
            resultSet = statement.executeQuery(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getOrdersByProdDay(int productionDay) {
        ResultSet resultSet = null;
        try {
            Connection connection = DriverManager.getConnection(databaseUrl, user, password);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String SQLQuery = "SELECT * FROM erpmes.orders WHERE productionday = " + productionDay + " AND ordercomplete IS NULL;";
            if (productionDay == 0) {
                SQLQuery = "SELECT * FROM erpmes.orders WHERE ordercomplete IS NULL;";
            }
            resultSet = statement.executeQuery(SQLQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

        public static int setOrderComplete(String orderNumber) throws SQLException {
            String SQLQuery = "UPDATE erpmes." + ordersTable + " SET ordercomplete = 1 WHERE ordernumber = '" + orderNumber + "';";
            return newEntry(SQLQuery, databaseUrl, user, password);
        }

        public static void setPieceInfo(int [] pieceTimes, int [] pieceAD, int [] pieceDD, String orderid){
            for (int i = 0; i < pieceAD.length; i++) {
                if(pieceDD[i] == 0){
                    break;
                }
                String SQLQuery = "UPDATE erpmes." + piecesTable + " SET arrivaldate = " + pieceAD[i] + ", dispatchdate = " + pieceDD[i] + ", productioncost = " + pieceTimes[i] + " WHERE orderid = '" + orderid + "' AND pid = " + i + ";";
                System.out.println(SQLQuery);
                try {
                    newEntry(SQLQuery, databaseUrl, user, password);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public static int getResultSetSize(ResultSet resultSet) {
        int size = 0;
        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return size;
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
        return ordersTable;
    }
}