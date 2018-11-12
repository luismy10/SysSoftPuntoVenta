/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author Ruberfc
 */
public class ComprobanteADO {

    public static String GetSerieNumeracion() {
        CallableStatement callableStatement = null;
        try {

            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall("{? = call Fc_Serie_Numero_Generado()}");
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.execute();
            return callableStatement.getString(1);

        } catch (SQLException ex) {
            System.out.println("ComprobanteADO:" + ex.getLocalizedMessage());
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }

        }

        return "";
    }

}
