package model;

import controller.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection = null;
    private static final String DIRECTION = Session.DIRECTION;//"Desktop-adk41hj
    private static final String PORT = Session.PORT;
    private static final String DBNAME = Session.DBNAME;
    private static final String USER = Session.USER;
    private static final String PASSWORD = Session.PASSWORD;//Qz0966lb
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
