package model;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static Connection connection = null;
    private static final String DIRECTION = "ALEZA";
    private static final String PORT = "1433";
    private static final String DBNAME = "PuntoVentaSysSoftDB";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456";
    private static final String URL = "jdbc:sqlserver://" + DIRECTION + ":" + PORT + ";databaseName=" + DBNAME + "";

    public static void dbConnect()  {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection Failed! Check output console" + e);
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
