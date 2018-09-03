package model;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class DetalleADO {

    public static String CrudEntity(DetalleTB detalleTB){
        String selectStmt = "{call Sp_Crud_Detalle(?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setInt("IdDetalle",detalleTB.getIdDetalle().get());
            callableStatement.setString("IdMantenimiento",detalleTB.getIdMantenimiento().get());
            callableStatement.setString("Nombre",detalleTB.getNombre().get());
            callableStatement.setString("Descripcion",detalleTB.getDescripcion().get());
            callableStatement.setString("Estado",detalleTB.getEstado().get());
            callableStatement.setString("UsuarioRegistro",detalleTB.getUsuarioRegistro().get());
            callableStatement.registerOutParameter("Message", java.sql.Types.VARCHAR, 20);
            callableStatement.execute();
            return callableStatement.getString("Message");
        }catch (SQLException | ClassNotFoundException e){
            return e.getLocalizedMessage();
        }finally {
            try{
                if (callableStatement != null){
                    callableStatement.close();
                }
                DBUtil.dbDisconnect();
            }catch (Exception ex){
                return ex.getLocalizedMessage();
            }
        }
    }
}
