package model;

import controller.Tools;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;

public class DBUtil {

    private static Connection connection = null;
    private static final String DIRECTION = "localhost";
    private static final String PORT = "1433";
    private static final String DBNAME = "PuntoVentaSysSoftDB";
    private static final String USER = "sa";
    private static final String PASSWORD = "Qz0966lb";//Qz0966lb
    private static final String URL = "jdbc:sqlserver://" + DIRECTION + ":" + PORT + ";databaseName=" + DBNAME + "";

    public static void dbConnect()  {
        try {   
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            Tools.AlertMessage(null, Alert.AlertType.NONE, "Conexion", e.getLocalizedMessage(), false);
        }
    }

    public static void dbDisconnect() throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            dbConnect();

            stmt = connection.createStatement();

            resultSet = stmt.executeQuery(queryStmt);

         
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
       return resultSet;
    }

    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = connection.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static boolean StateConnection(String url) {
        try {
            int timeOut = 2000;
            String host = url;
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(timeOut);
        } catch (IOException e) {
        }
        return false;
    }
    
     public static boolean StateConnection() {
        try {
            int timeOut = 2000;
            String host = DIRECTION;
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(timeOut);
        } catch (IOException e) {
        }
        return false;
    }

}
