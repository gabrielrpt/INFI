import database.javaDatabase;

public class SQLQueryERP {
    String query;
    String scheme = "erpmes";

    //Builds SQL query for database.newEntry method

    public String SQLQuery(String table, String nameId, int orderNumber, String workPiece, int quantity, int dueDate, int latePen, int earlyPen){

        query = "INSERT INTO " + scheme + "." + table + "(orderNumber,workPiece,quantity,dueDate,latePen,earlyPen) "
                + "VALUES ('" + orderNumber + "'" + ", '" + workPiece + "','"
                + quantity + "','" + dueDate + "','" + latePen + "','" + earlyPen + "')";

        return query;
    }


    /*public String getQuery() {
        return query;
    }*/
}
