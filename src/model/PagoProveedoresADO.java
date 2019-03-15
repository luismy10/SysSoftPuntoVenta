/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Ruberfc
 */
public class PagoProveedoresADO {

    public static String crudPagoProveedores(PagoProveedoresTB pagoProveedoresTB) {

        PreparedStatement pago_Proveedores = null;
        PreparedStatement update_Compra = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            pago_Proveedores = DBUtil.getConnection().prepareStatement("insert into PagoProveedoresTB(MontoTotal,MontoActual,CuotaTotal,CuotaActual,ValorCuota,Plazos,FechaInicial,FechaActual,FechaFinal,Observacion,Estado,IdProveedor,IdCompra) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            update_Compra = DBUtil.getConnection().prepareStatement("update CompraTB set EstadoCompra = ? where IdCompra = ?");

            if (pagoProveedoresTB.getMontoTotal() > pagoProveedoresTB.getMontoActual()) {
                pago_Proveedores.setDouble(1, pagoProveedoresTB.getMontoTotal());
                pago_Proveedores.setDouble(2, pagoProveedoresTB.getMontoActual());
                pago_Proveedores.setInt(3, pagoProveedoresTB.getCuotaTotal());
                pago_Proveedores.setInt(4, pagoProveedoresTB.getCuotaActual());
                pago_Proveedores.setDouble(5, pagoProveedoresTB.getValorCuota());
                pago_Proveedores.setString(6, pagoProveedoresTB.getPlazos());
                pago_Proveedores.setTimestamp(7, pagoProveedoresTB.getFechaInicial());
                pago_Proveedores.setTimestamp(8, pagoProveedoresTB.getFechaActual());
                pago_Proveedores.setTimestamp(9, pagoProveedoresTB.getFechaFinal());
                pago_Proveedores.setString(10, pagoProveedoresTB.getObservacion());
                pago_Proveedores.setString(11, pagoProveedoresTB.getEstado());
                pago_Proveedores.setString(12, pagoProveedoresTB.getIdProveedor());
                pago_Proveedores.setString(13, pagoProveedoresTB.getIdCompra());
                pago_Proveedores.addBatch();
                System.out.println("==========");
                System.out.println(pagoProveedoresTB.getMontoTotal() + "");
                System.out.println(pagoProveedoresTB.getMontoActual() + "");

                if (pagoProveedoresTB.getMontoTotal() == (pagoProveedoresTB.getMontoActual() + pagoProveedoresTB.getValorCuota())) {
                    update_Compra.setString(1, "pagado".toUpperCase());
                    update_Compra.setString(2, pagoProveedoresTB.getIdCompra());
                    update_Compra.addBatch();
                    System.out.println("**********");
                    System.out.println(pagoProveedoresTB.getMontoTotal() + "");
                    System.out.println(pagoProveedoresTB.getMontoActual() + "");
                }

            }

            pago_Proveedores.executeBatch();
            update_Compra.executeBatch();
            DBUtil.getConnection().commit();
            return (pagoProveedoresTB.getMontoTotal() == (pagoProveedoresTB.getMontoActual() + pagoProveedoresTB.getValorCuota())) ? "update" : "register";

        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
                return ex.getLocalizedMessage();
            } catch (SQLException ex1) {
                return ex1.getLocalizedMessage();
            }
        } finally {
            try {
                if (pago_Proveedores != null) {
                    pago_Proveedores.close();
                }
                if (update_Compra != null) {
                    update_Compra.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }

    }

    public static ObservableList<PagoProveedoresTB> ListHistorialPagoCompras(String value) {
        String selectStmt = "{call Sp_Listar_Historial_Pagos(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<PagoProveedoresTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                PagoProveedoresTB pagoProveedoresTB = new PagoProveedoresTB();

                pagoProveedoresTB.setIdPagoProveedores(rsEmps.getInt("IdPagoProveedores"));
                pagoProveedoresTB.setMontoTotal(rsEmps.getDouble("MontoTotal"));
                pagoProveedoresTB.setMontoActual(rsEmps.getDouble("MontoActual"));
                pagoProveedoresTB.setCuotaTotal(rsEmps.getInt("CuotaTotal"));
                pagoProveedoresTB.setCuotaActual(rsEmps.getInt("CuotaActual"));
                pagoProveedoresTB.setValorCuota(rsEmps.getDouble("ValorCuota"));
                pagoProveedoresTB.setPlazos(rsEmps.getString("Plazos"));
                pagoProveedoresTB.setFechaInicial(rsEmps.getTimestamp("FechaInicial"));
                pagoProveedoresTB.setFechaActual(rsEmps.getTimestamp("FechaActual"));
                pagoProveedoresTB.setFechaFinal(rsEmps.getTimestamp("FechaFinal"));
                pagoProveedoresTB.setObservacion(rsEmps.getString("Observacion"));
                pagoProveedoresTB.setEstado(rsEmps.getString("Estado"));
                pagoProveedoresTB.setIdProveedor(rsEmps.getString("IdProveedor"));
                pagoProveedoresTB.setIdCompra(rsEmps.getString("IdCompra"));
                empList.add(pagoProveedoresTB);
            }
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (rsEmps != null) {
                    rsEmps.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return empList;
    }

}
