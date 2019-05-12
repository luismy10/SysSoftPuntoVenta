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

    public static String crudPagoProveedores(PagoProveedoresTB pagoProveedoresTB, double pagado, double pendiente) {
        
        String result = "";
        
        PreparedStatement pago_Proveedores = null;
        PreparedStatement update_Compra = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            pago_Proveedores = DBUtil.getConnection().prepareStatement("insert into PagoProveedoresTB(MontoTotal,MontoActual,CuotaActual,Plazos,Dias,FechaActual,Observacion,Estado,IdProveedor,IdCompra,IdEmpleado) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?)");

            update_Compra = DBUtil.getConnection().prepareStatement("update CompraTB set EstadoCompra = ? where IdCompra = ?");

            if (pagoProveedoresTB.getMontoTotal() > pagado) {
                pago_Proveedores.setDouble(1, pagoProveedoresTB.getMontoTotal());
                pago_Proveedores.setDouble(2, pagoProveedoresTB.getMontoActual());
                pago_Proveedores.setInt(3, pagoProveedoresTB.getCuotaActual());
                pago_Proveedores.setString(4, pagoProveedoresTB.getPlazos());
                pago_Proveedores.setInt(5, pagoProveedoresTB.getDias());
                pago_Proveedores.setTimestamp(6, pagoProveedoresTB.getFechaActual());
                pago_Proveedores.setString(7, pagoProveedoresTB.getObservacion());
                pago_Proveedores.setString(8, pagoProveedoresTB.getEstado());
                pago_Proveedores.setString(9, pagoProveedoresTB.getIdProveedor());
                pago_Proveedores.setString(10, pagoProveedoresTB.getIdCompra());
                pago_Proveedores.setString(11, pagoProveedoresTB.getIdEmpleado());
                pago_Proveedores.addBatch();
                
//                System.out.println("==========");
//                System.out.println(pagoProveedoresTB.getMontoTotal() + "");
//                System.out.println(pagoProveedoresTB.getMontoActual() + "");
//                
                result = "register";
                
                if (pagoProveedoresTB.getMontoTotal() == (pagado + pagoProveedoresTB.getMontoActual())) {
                    update_Compra.setInt(1, 1);
                    update_Compra.setString(2, pagoProveedoresTB.getIdCompra());
                    update_Compra.addBatch();

//                    System.out.println("**********");
//                    System.out.println(pagoProveedoresTB.getMontoTotal() + "");
//                    System.out.println(pagoProveedoresTB.getMontoActual() + "");
                    
                    result = "update";
                }
                
            } else {
                result = "La compra se hiso al contado o ya se termino de pagar la misma.";
            }

            pago_Proveedores.executeBatch();
            update_Compra.executeBatch();
            DBUtil.getConnection().commit();
            return result;

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
                pagoProveedoresTB.setCuotaActual(rsEmps.getInt("CuotaActual"));
                pagoProveedoresTB.setPlazos(rsEmps.getString("Plazos"));
                pagoProveedoresTB.setDias(rsEmps.getInt("Dias"));
                pagoProveedoresTB.setFechaActual(rsEmps.getTimestamp("FechaActual"));
                pagoProveedoresTB.setObservacion(rsEmps.getString("Observacion"));
                pagoProveedoresTB.setEstado(rsEmps.getString("Estado"));
                pagoProveedoresTB.setIdProveedor(rsEmps.getString("IdProveedor"));
                pagoProveedoresTB.setIdCompra(rsEmps.getString("IdCompra"));
                pagoProveedoresTB.setIdEmpleado(rsEmps.getString("IdEmpleado"));
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
    
    public static String EliminarPagoCuota(String idPagoProveedores) {
        
        String result = "";           
        PreparedStatement statementPagoCuota = null;
        PreparedStatement statementHistorial = null;

            try {
                
                DBUtil.dbConnect();
                DBUtil.getConnection().setAutoCommit(false);
                
                statementPagoCuota = DBUtil.getConnection().prepareStatement("delete from PagoProveedoresTB where IdPagoProveedores = ?;");
                statementPagoCuota.setString(1,idPagoProveedores);
                statementPagoCuota.addBatch();                
                
//                statementHistorial = DBUtil.getConnection().prepareStatement("DELETE FROM HistorialArticuloTB WHERE IdArticulo = ?");
//                statementHistorial.setString(1, idArticulo);
//                statementHistorial.addBatch();

                statementPagoCuota.executeBatch();
//                statementHistorial.executeBatch();
                DBUtil.getConnection().commit();
                result = "removed";

            } catch (SQLException ex) {
                try {
                    result = ex.getLocalizedMessage();
                    DBUtil.getConnection().rollback();
                } catch (SQLException e) {
                    result = e.getLocalizedMessage();
                }
            } finally {
                try {

                    if (statementPagoCuota != null) {
                        statementPagoCuota.close();
                    }
//                    if (statementHistorial != null) {
//                        statementHistorial.close();
//                    }

                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    result = ex.getLocalizedMessage();
                }
                
              }  
        return result;

    }

}
