package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MantenimientoADO {

    public static ObservableList<MantenimientoTB> ListPrincipal() throws ClassNotFoundException, SQLException {
        String selectStmt = "select IdMantenimiento,Nombre from MantenimientoTB";
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<MantenimientoTB> empList = getEntityList(rsEmps);
            return empList;
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
            throw e;
        }

    }

    private static ObservableList<MantenimientoTB> getEntityList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<MantenimientoTB> empList = FXCollections.observableArrayList();

        while (rs.next()) {
            MantenimientoTB emp = new MantenimientoTB();
            emp.setIdMantenimiento(rs.getString("IdMantenimiento"));
            emp.setNombre(rs.getString("Nombre"));
            empList.add(emp);
        }
        return empList;
    }

    public static ObservableList<DetalleTB> ListDetail(String... value) throws ClassNotFoundException, SQLException {
        String selectStmt = "{call Sp_List_Table_Detalle(?)}";
        PreparedStatement preparedStatement = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value[0]);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<DetalleTB> empList = getEntityDetailList(resultSet);
            return empList;
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            DBUtil.dbDisconnect();

        }

    }

    private static ObservableList<DetalleTB> getEntityDetailList(ResultSet rs) throws SQLException, ClassNotFoundException {
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

    public static String CrudEntity(MantenimientoTB mantenimientoTB) {
        String selectStmt = "{call Sp_Crud_Mantenimiento(?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdMantenimiento", mantenimientoTB.getIdMantenimiento());
            callableStatement.setString("Nombre", mantenimientoTB.getNombre());
            callableStatement.setString("Estado", mantenimientoTB.getEstado().toString());
            callableStatement.setString("UsuarioRegistro", mantenimientoTB.getUsuarioRegistro());
            callableStatement.registerOutParameter("Message", java.sql.Types.VARCHAR, 20);
            callableStatement.execute();
            return callableStatement.getString("Message");
        } catch (SQLException | ClassNotFoundException ex) {
            return ex.getLocalizedMessage();
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

    public static String GetIdMantenimiento() {
        String selectStmt = "{? = call Fc_Mantenimiento_Generar_Codigo()}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
            callableStatement.execute();
            return callableStatement.getString(1);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e.getLocalizedMessage());
            return "Error al generar";
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                System.out.println("La consulta se cerro antes de finalizar: " + ex.getLocalizedMessage());
            }
        }
    }

    public static String DeleteMantenimiento(String idMantenimiento) {
        String selectStmt = "delete from MantenimientoTB where IdMantenimiento = ?";
        PreparedStatement preparedStatement = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, idMantenimiento);
            return preparedStatement.executeUpdate() == 1 ? "eliminado" : "error";
        } catch (SQLException | ClassNotFoundException e) {
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
}
