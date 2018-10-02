
package model;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class LoteADO {
    
    public static String GenerateBatch() {
        CallableStatement lote_generado = null;
        try {
            DBUtil.dbConnect();
            lote_generado = DBUtil.getConnection().prepareCall("{? = call Fc_Generar_Lote_Alfanumerico()}");
            lote_generado.registerOutParameter(1, java.sql.Types.VARCHAR);
            lote_generado.execute();
            return lote_generado.getString(1);            
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
        } finally {
            try {
                
                if (lote_generado != null) {
                    lote_generado.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return "";
    }
    
}
