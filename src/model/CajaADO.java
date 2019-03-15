/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ruberfc
 */
public class CajaADO {

    public static String CrudInsertar(CajaTB cajaTB) {

        PreparedStatement caja_apertura = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            caja_apertura = DBUtil.getConnection().prepareStatement("insert into CajaTB(MontoInicial,MontoFinal,Ingresos,Egresos,Devoluciones,Entradas,Salidas,FechaApertura,FechaCierre,Estado,IdEmpleado)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?)");

            caja_apertura.setDouble(1, cajaTB.getMontoInicial());
            caja_apertura.setDouble(2, cajaTB.getMontoFinal());
            caja_apertura.setDouble(3, cajaTB.getIngresos());
            caja_apertura.setDouble(4, cajaTB.getEgresos());
            caja_apertura.setDouble(5, cajaTB.getDevoluciones());
            caja_apertura.setDouble(6, cajaTB.getEntradas());
            caja_apertura.setDouble(7, cajaTB.getSalidas());
            caja_apertura.setTimestamp(8, cajaTB.getFechaApertura());
            caja_apertura.setTimestamp(9, cajaTB.getFechaCierre());
            caja_apertura.setString(10, cajaTB.getEstado());
            caja_apertura.setString(11, cajaTB.getIdEmpleado());

            caja_apertura.addBatch();
            caja_apertura.executeBatch();

            DBUtil.getConnection().commit();

            return "Se realizó la apertura de caja con éxito para el usuario actual";
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
                return ex.getLocalizedMessage();
            } catch (SQLException ex1) {
                return ex1.getLocalizedMessage();
            }
        } finally {
            try {
                if (caja_apertura != null) {
                    caja_apertura.close();
                }

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

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CajaTB cajaTB = null;

        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement("select MontoInicial,MontoFinal,Ingresos,Egresos,Devoluciones,Entradas,Salidas,FechaApertura,FechaCierre,Estado,IdEmpleado from CajaTB where IdEmpleado = ? and Estado = ?");
            preparedStatement.setString(1, idEmpleado);
            preparedStatement.setString(2, estado);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                cajaTB = new CajaTB();
                cajaTB.setMontoInicial(resultSet.getDouble("MontoInicial"));
                cajaTB.setMontoFinal(resultSet.getDouble("MontoFinal"));
                cajaTB.setIngresos(resultSet.getDouble("Ingresos"));
                cajaTB.setEgresos(resultSet.getDouble("Salida"));
                cajaTB.setDevoluciones(resultSet.getDouble("Devolucion"));
                cajaTB.setEntradas(resultSet.getDouble("Entradas"));
                cajaTB.setSalidas(resultSet.getDouble("Salidas"));
                cajaTB.setFechaApertura(resultSet.getTimestamp("FechaApertura"));
                cajaTB.setFechaCierre(resultSet.getTimestamp("FechaCierre"));
                cajaTB.setEstado(resultSet.getString("Estado"));
                cajaTB.setIdEmpleado(resultSet.getString("IdEmpleado"));
            }

        } catch (SQLException ex) {
        } finally {

            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
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

    public static String crudCorteCaja(CajaTB cajaTB) {
        PreparedStatement corte_caja = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            corte_caja = DBUtil.getConnection().prepareStatement("update CajaTB set MontoFinal = ?, FechaCierre = ?, Estado = ? where Estado = ? and IdEmpleado = ?");
            corte_caja.setDouble(1, cajaTB.getMontoFinal());
            corte_caja.setTimestamp(2, cajaTB.getFechaCierre());
            corte_caja.setString(3, "INACTIVO".toUpperCase());
            corte_caja.setString(4, cajaTB.getEstado());
            corte_caja.setString(5, cajaTB.getIdEmpleado());

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
