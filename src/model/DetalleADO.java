package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DetalleADO {

    public static ObservableList<DetalleTB> ListDetail(String... value) {
        String selectStmt = "{call Sp_List_Table_Detalle(?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<DetalleTB> empList = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value[0]);
            preparedStatement.setString(2, value[1]);
            resultSet = preparedStatement.executeQuery();
            empList = getEntityDetailList(resultSet);
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                DBUtil.dbDisconnect();

            } catch (SQLException ex) {

            }

        }
        return empList;
    }

    private static ObservableList<DetalleTB> getEntityDetailList(ResultSet rs) throws SQLException {
        ObservableList<DetalleTB> empList = FXCollections.observableArrayList();

        while (rs.next()) {
            DetalleTB emp = new DetalleTB();
            emp.setIdDetalle(rs.getInt("IdDetalle"));
            emp.setNombre(rs.getString("Nombre"));
            emp.setDescripcion(rs.getString("Descripcion"));
            emp.setEstado(rs.getString("Estado"));
            empList.add(emp);
        }
        return empList;
    }

    public static String CrudEntity(DetalleTB detalleTB) {
        String selectStmt = "{call Sp_Crud_Detalle(?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setInt("IdDetalle", detalleTB.getIdDetalle().get());
            callableStatement.setString("IdMantenimiento", detalleTB.getIdMantenimiento().get());
            callableStatement.setString("Nombre", detalleTB.getNombre().get());
            callableStatement.setString("Descripcion", detalleTB.getDescripcion().get());
            callableStatement.setString("Estado", detalleTB.getEstado().get());
            callableStatement.setString("UsuarioRegistro", detalleTB.getUsuarioRegistro().get());
            callableStatement.registerOutParameter("Message", java.sql.Types.VARCHAR, 20);
            callableStatement.execute();
            return callableStatement.getString("Message");
        } catch (SQLException e) {
            return e.getLocalizedMessage();
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                return ex.getLocalizedMessage();
            }
        }
    }

    public static String DeleteDetail(DetalleTB detalleTB) {
        String selectStmt = "delete from DetalleTB where IdDetalle = ? and IdMantenimiento = ?";
        PreparedStatement preparedStatement = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setInt(1, detalleTB.getIdDetalle().get());
            preparedStatement.setString(2, detalleTB.getIdMantenimiento().get());
            return preparedStatement.executeUpdate() == 1 ? "eliminado" : "error";
        } catch (SQLException e) {
            return "La operación de selección de SQL ha fallado: " + e.getLocalizedMessage();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                return "La consulta se cerro antes de finalizar: " + ex.getLocalizedMessage();
            }
        }
    }

    public static ObservableList<DetalleTB> GetDetailIdName(String value) throws ClassNotFoundException, SQLException {
        String selectStmt = "{call Sp_Get_Detalle_IdNombre(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            resultSet = preparedStatement.executeQuery();
            ObservableList<DetalleTB> empList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                DetalleTB detalleTB = new DetalleTB();
                detalleTB.setIdDetalle(resultSet.getInt("IdDetalle"));
                detalleTB.setNombre(resultSet.getString("Nombre"));
                empList.add(detalleTB);
            }
            return empList;
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            DBUtil.dbDisconnect();
        }
    }

}
