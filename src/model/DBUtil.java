package model;

import controller.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection = null;
<<<<<<< HEAD
    private static final String DIRECTION = "192.168.1.33";//"localhost";//"Desktop-adk41hj";//
    private static final String PORT = "1433";
    private static final String DBNAME = "PuntoVentaSysSoftDBDesarrollo";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";//Qz0966lb
=======
    private static final String DIRECTION = Session.DIRECTION;//"Desktop-adk41hj
    private static final String PORT = Session.PORT;
    private static final String DBNAME = Session.DBNAME;
    private static final String USER = Session.USER;
    private static final String PASSWORD = Session.PASSWORD;//Qz0966lb
>>>>>>> eaefcc5fc1f1f36bf533937d711f046a10cb94fe
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
