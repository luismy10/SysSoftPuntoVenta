/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Ruberfc
 */
public class VentaADO {

    public static String CrudVenta(VentaTB ventaTB) {

        CallableStatement serie_numeracion = null;

        PreparedStatement venta = null;

        PreparedStatement comprobante = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            serie_numeracion = DBUtil.getConnection().prepareCall("{? = call Fc_Serie_Numero_Generado()}");
            serie_numeracion.registerOutParameter(1, java.sql.Types.VARCHAR);
            serie_numeracion.execute();
            String[] id_comprabante = serie_numeracion.getString(1).split("-");

            venta = DBUtil.getConnection().prepareStatement("INSERT INTO [dbo].[VentaTB]\n"
                    + "           ([Cliente]\n"
                    + "           ,[Vendedor]\n"
                    + "           ,[Comprobante]\n"
                    + "           ,[Serie]\n"
                    + "           ,[Numeracion]\n"
                    + "           ,[FechaVenta]\n"
                    + "           ,[SubTotal]\n"
                    + "           ,[Gravada]\n"
                    + "           ,[Descuento]\n"
                    + "           ,[Igv]\n"
                    + "           ,[Total])\n"
                    + "     VALUES\n"
                    + "           (?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?\n"
                    + "           ,?)");

            comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO [dbo].[ComprobanteTB]\n"
                    + "           ([Serie]\n"
                    + "           ,[Numeracion]\n"
                    + "           ,[FechaRegistro])\n"
                    + "     VALUES\n"
                    + "           (?,?,?)");

            venta.setString(1, ventaTB.getCliente());
            venta.setString(2, ventaTB.getVendedor());
            venta.setInt(3, ventaTB.getComprobante());
            venta.setString(4, id_comprabante[0]);
            venta.setString(5, id_comprabante[1]);
            venta.setDate(6, ventaTB.getFechaVenta());
            venta.setDouble(7, ventaTB.getSubTotal());
            venta.setDouble(8, ventaTB.getGravada());
            venta.setDouble(9, ventaTB.getDescuento());
            venta.setDouble(10, ventaTB.getIgv());
            venta.setDouble(11, ventaTB.getTotal());
            venta.addBatch();

            comprobante.setBytes(1, id_comprabante[0].getBytes());
            comprobante.setString(2, id_comprabante[1]);
            comprobante.setDate(3, ventaTB.getFechaVenta());
            comprobante.addBatch();

            venta.executeBatch();
            comprobante.executeBatch();
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
                if (serie_numeracion != null) {
                    serie_numeracion.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }
}
