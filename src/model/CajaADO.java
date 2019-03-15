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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ruberfc
 */
public class CajaADO {
    
     public static String CrudInsertar(CajaTB cajaTB) {

        PreparedStatement caja = null;
//        PreparedStatement caja_Trabajador = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            caja = DBUtil.getConnection().prepareStatement("INSERT INTO CajaTB(MontoInicial,MontoFinal,Entrada,Salida,Devolucion,FechaApertura,FechaCierre,Estado,IdEmpleado)"
                    + "VALUES(?,?,?,?,?,?,?,?,?)");

//            caja_Trabajador = DBUtil.getConnection().prepareStatement("INSERT INTO CajaTrabajadorTB(IdEmpleado,MontoInicial,Entrada,Salida,Devolucion,Estado,FechaApertura,FechaCierre)"
//                    + "VALUES(?,?,?,?,?,?,?,?)");
            caja.setDouble(1, cajaTB.getMontoInicial());
            caja.setDouble(2, cajaTB.getMontoFinal());
            caja.setDouble(3, cajaTB.getEntrada());
            caja.setDouble(4, cajaTB.getSalida());
            caja.setDouble(5, cajaTB.getDevolucion());
            caja.setTimestamp(6, cajaTB.getFechaApertura());
            caja.setTimestamp(7, cajaTB.getFechaCierre());
            caja.setString(8, cajaTB.getEstado());
            caja.setString(9, cajaTB.getIdEmpleado());

            caja.addBatch();
//            caja_Trabajador.addBatch();

            caja.executeBatch();
//            caja_Trabajador.executeBatch();
            System.out.println("registro algo");

            DBUtil.getConnection().commit();

            return "Se realizó la apertura de caja para el empleado actual ";
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
//                if (caja_Trabajador != null ){
//                    caja_Trabajador.close();
//                }

                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }
    
    public static String consultarEstadoCaja(String estado, String idEmpleado) {

        PreparedStatement consultar_Caja = null;

        try {
            DBUtil.dbConnect();
            consultar_Caja = DBUtil.getConnection().prepareStatement("select * from CajaTB  where Estado = ? and IdEmpleado = ?");
            consultar_Caja.setString(1, estado);
            consultar_Caja.setString(2, idEmpleado);
            return consultar_Caja.executeQuery().next() ? "activo".toUpperCase() : "inactivo".toUpperCase();
        } catch (SQLException ex) {
            return ex.getLocalizedMessage();
        } finally {
            try {
                if (consultar_Caja != null) {
                    consultar_Caja.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }

    }
    
    public static CajaTB consultarMontosCaja(String idEmpleado, String estado) {
        PreparedStatement entrada = null, salida = null, devolucion = null, fondoCaja = null;
        ResultSet resultSetEntrada = null, resultSetSalida = null, resultSetDevolucion = null, resultSetFondoCaja = null;
        CajaTB cajaTB = null;

        try {
            DBUtil.dbConnect();
            entrada = DBUtil.getConnection().prepareStatement("select Entrada from CajaTB where IdEmpleado = ? and Estado = ?");
            entrada.setString(1, idEmpleado);
            entrada.setString(2, estado);

            salida = DBUtil.getConnection().prepareStatement("select Salida from CajaTB where IdEmpleado = ? and Estado = ?");
            salida.setString(1, idEmpleado);
            salida.setString(2, estado);

            devolucion = DBUtil.getConnection().prepareStatement("select Devolucion from CajaTB where IdEmpleado = ? and Estado = ?");
            devolucion.setString(1, idEmpleado);
            devolucion.setString(2, estado);

            fondoCaja = DBUtil.getConnection().prepareStatement("select MontoInicial from CajaTB where IdEmpleado = ? and Estado = ?");
            fondoCaja.setString(1, idEmpleado);
            fondoCaja.setString(2, estado);

            resultSetEntrada = entrada.executeQuery();
            resultSetSalida = salida.executeQuery();
            resultSetDevolucion = devolucion.executeQuery();
            resultSetFondoCaja = fondoCaja.executeQuery();

            cajaTB = new CajaTB();
            cajaTB.setEntrada(resultSetEntrada.next()? resultSetEntrada.getDouble("Entrada"):0.0);
            cajaTB.setSalida(resultSetSalida.next()? resultSetSalida.getDouble("Salida"):0.0);
            cajaTB.setDevolucion(resultSetDevolucion.next()? resultSetDevolucion.getDouble("Devolucion"):0.0);
            cajaTB.setMontoInicial(resultSetFondoCaja.next() ? resultSetFondoCaja.getDouble("MontoInicial") : 0.0);

        } catch (SQLException ex) {
        } finally {

            try {
                if (entrada != null) {
                    entrada.close();
                }
                if (resultSetEntrada != null) {
                    resultSetEntrada.close();
                }
                if (salida != null) {
                    salida.close();
                }
                if (resultSetSalida != null) {
                    resultSetSalida.close();
                }
                if (devolucion != null) {
                    devolucion.close();
                }
                if (resultSetDevolucion != null) {
                    resultSetDevolucion.close();
                }
                if (fondoCaja != null) {
                    fondoCaja.close();
                }
                if (resultSetFondoCaja != null) {
                    resultSetFondoCaja.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }

        return cajaTB;
    }

    public static VentaTB consultarMontosVenta(String idEmpleado, String estado) {
        PreparedStatement ventasTotales = null;
        ResultSet resultSetVentasTotales = null;
        VentaTB ventaTB = null;

        try {
            DBUtil.dbConnect();

            ventasTotales = DBUtil.getConnection().prepareCall("select sum(total) as Total from VentaTB as v inner join CajaTB as c on v.Vendedor = c.IdEmpleado where c.IdEmpleado = ? and c.Estado = ?");
            ventasTotales.setString(1, idEmpleado);
            ventasTotales.setString(2, estado);

            resultSetVentasTotales = ventasTotales.executeQuery();

            if (resultSetVentasTotales.next()) {
                ventaTB = new VentaTB();
                ventaTB.setTotal(resultSetVentasTotales.getDouble("Total"));
            }

        } catch (SQLException ex) {
        } finally {

            try {
                if (ventasTotales != null) {
                    ventasTotales.close();
                }
                if (resultSetVentasTotales != null) {
                    resultSetVentasTotales.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
        return ventaTB;
    }
    
    
    public static String crudCorteCaja(CajaTB cajaTB){
        PreparedStatement corte_caja = null;
        
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            corte_caja = DBUtil.getConnection().prepareStatement("update CajaTB set FechaCierre = ?, Estado = ? where Estado = ? and IdEmpleado = ?");
            
            corte_caja.setTimestamp(1, cajaTB.getFechaCierre());
            corte_caja.setString(2, "inactivo".toUpperCase());
            corte_caja.setString(3, cajaTB.getEstado());
            corte_caja.setString(4, cajaTB.getIdEmpleado());
            
            corte_caja.addBatch();
            
            corte_caja.executeBatch();
            DBUtil.getConnection().commit();
            return "update".toUpperCase();
        } catch (SQLException ex) {
            return ex.getLocalizedMessage();
        } finally {
            try {
                if (corte_caja != null) {
                    corte_caja.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
   
    }
}
