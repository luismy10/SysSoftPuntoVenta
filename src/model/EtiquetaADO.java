
package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class EtiquetaADO {
    
  public static ArrayList<EtiquetaTB> ListTipoEtiquetas() {
        ArrayList<EtiquetaTB> list = new ArrayList<>();
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statementLista = null;
            ResultSet resultSet = null;
            try {
                statementLista = DBUtil.getConnection().prepareStatement("SELECT * FROM TipoEtiquetaTB");
                resultSet = statementLista.executeQuery();
                while (resultSet.next()) {
                    EtiquetaTB etiquetaTB = new EtiquetaTB();
                    etiquetaTB.setIdEtiqueta(resultSet.getInt(1));
                    etiquetaTB.setNombre(resultSet.getString(2));
                    list.add(etiquetaTB);
                }
            } catch (SQLException ex) {

            } finally {
                try {
                    if (statementLista != null) {
                        statementLista.close();
                    }
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {

                }

            }
        }
        return list;
    }
    
    
}
