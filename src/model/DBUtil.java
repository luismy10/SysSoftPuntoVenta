package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection = null;
    private static final String DIRECTION = "localhost";
    private static final String PORT = "1433";
    private static final String DBNAME = "PuntoVentaSysSoftDBDesarrollo";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";//Qz0966lb
    private static final String URL = "jdbc:sqlserver://" + DIRECTION + ":" + PORT + ";databaseName=" + DBNAME + "";

    public static void dbConnect()  {
        try {   
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void dbDisconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}
