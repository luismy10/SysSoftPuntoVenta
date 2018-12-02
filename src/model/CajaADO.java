/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Session;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Ruberfc
 */
public class CajaADO {

    public static String CrudInsertar(CajaTB cajaTB) {

        PreparedStatement caja = null;
        PreparedStatement caja_Empleado = null;
        

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            caja = DBUtil.getConnection().prepareStatement("INSERT INTO CajaTB(IdCajaTrabajador,MontoInicial,MontoFinal,Estado,Fecha)"
                    + "VALUES(?,?,?,?,?)");

            caja_Empleado = DBUtil.getConnection().prepareStatement("INSERT INTO CajaTrabajadorTB(IdEmpleado,MontoInicial,Entrada,Salida,Devolucion,Estado,FechaApertura,FechaCierre)"
                    + "VALUES(?,?,?,?,?,?,?,?)");


            caja.addBatch();
            caja_Empleado.addBatch();
            
            caja.executeBatch();
            caja_Empleado.executeBatch();

            DBUtil.getConnection().commit();
            return "register";
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
                return ex.getLocalizedMessage();
            } catch (SQLException ex1) {
                return ex1.getLocalizedMessage();
            }
        } finally {
            try {
                if (caja != null) {
                    caja.close();
                }
                if (caja_Empleado != null) {
                    caja_Empleado.close();
                }
                
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }

}
