package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RepresentanteADO {

    public static String CrudRepresentante(RepresentanteTB representanteTB) {
        String selectStmt = "{call Sp_Crud_Representante(?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdProveedor", representanteTB.getIdProveedor());
            callableStatement.setString("IdPersona", representanteTB.getIdPersona());

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

    public static String DeleteRepresentante(String idProveedor, String idPersona) {
        String selectStmt = "delete from ProveedorPersonaTB where IdProveedor = ? and IdPersona = ?";
        PreparedStatement preparedStatement = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, idProveedor);
            preparedStatement.setString(2, idPersona);
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
}
