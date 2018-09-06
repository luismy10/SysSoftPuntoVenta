package model;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class PersonaADO {

    public static String CrudEntity(PersonaTB personaTB) {
        String selectStmt = "{call Sp_Crud_Persona(?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setLong("IdPersona", personaTB.getIdPersona());
            callableStatement.setInt("TipoDocumento", personaTB.getTipoDocumento());
            callableStatement.setString("NumeroDocumento", personaTB.getNumeroDocumento());
            callableStatement.setString("ApellidoPaterno", personaTB.getApellidoPaterno().get());
            callableStatement.setString("ApellidoMaterno", personaTB.getApellidoMaterno());
            callableStatement.setString("PrimerNombre", personaTB.getPrimerNombre());
            callableStatement.setString("SegundoNombre", personaTB.getSegundoNombre());
            callableStatement.setInt("Sexo", personaTB.getSexo());
            callableStatement.registerOutParameter("Message", java.sql.Types.VARCHAR, 20);
            callableStatement.execute();
            return callableStatement.getString("Message");
        } catch (SQLException | ClassNotFoundException e) {
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

}
