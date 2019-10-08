package model;

import controller.Tools;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CuentasHistorialClienteADO {

    public static String Crud_CuentasHistorialCliente(CuentasHistorialClienteTB cuentasHistorialClienteTB, MovimientoCajaTB movimientoCajaTB, boolean completed,String idVenta) {
        String result = "";
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statementCuentas = null;
            PreparedStatement statementMovimiento = null;
            PreparedStatement statementVenta = null;
            try {
                DBUtil.getConnection().setAutoCommit(false);

                statementCuentas = DBUtil.getConnection().prepareStatement("INSERT INTO CuentasHistorialClienteTB(IdCuentaClientes,Abono,FechaAbono,Referencia)VALUES(?,?,?,?)");
                statementCuentas.setInt(1, cuentasHistorialClienteTB.getIdCuentaClientes());
                statementCuentas.setDouble(2, cuentasHistorialClienteTB.getAbono());
                statementCuentas.setTimestamp(3, Timestamp.valueOf(cuentasHistorialClienteTB.getFechaAbono()));
                statementCuentas.setString(4, cuentasHistorialClienteTB.getReferencia());
                statementCuentas.addBatch();

                statementMovimiento = DBUtil.getConnection().prepareStatement("INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,HoraMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?,?)");
                statementMovimiento.setInt(1, movimientoCajaTB.getIdCaja());
                statementMovimiento.setString(2, movimientoCajaTB.getIdUsuario());
                statementMovimiento.setString(3, Tools.getDate());
                statementMovimiento.setString(4, Tools.getHour("HH:mm:ss"));
                statementMovimiento.setString(5, movimientoCajaTB.getComentario());
                statementMovimiento.setString(6, movimientoCajaTB.getMovimiento());
                statementMovimiento.setDouble(7, movimientoCajaTB.getEntrada());
                statementMovimiento.setDouble(8, movimientoCajaTB.getSalidas());
                statementMovimiento.setDouble(9, movimientoCajaTB.getSaldo());
                statementMovimiento.addBatch();

                statementVenta = DBUtil.getConnection().prepareStatement("UPDATE VentaTB SET Estado = ? WHERE IdVenta = ?");
                if (completed) {
                    statementVenta.setInt(1, 1);
                    statementVenta.setString(2, idVenta);
                    statementVenta.addBatch();
                }

                statementCuentas.executeBatch();
                statementMovimiento.executeBatch();
                statementVenta.executeBatch();
                DBUtil.getConnection().commit();
                result = "register";
            } catch (SQLException ex) {
                try {
                    DBUtil.getConnection().rollback();
                } catch (SQLException e) {

                }
                result = ex.getLocalizedMessage();
            } finally {
                try {
                    if (statementCuentas != null) {
                        statementCuentas.close();
                    }
                    if (statementMovimiento != null) {
                        statementMovimiento.close();
                    }
                    if (statementVenta != null) {
                        statementVenta.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    result = ex.getLocalizedMessage();
                }
            }
        } else {
            result = "No se puedo conectar al servidor, revise su conexión.";
        }

        return result;
    }

    public static ObservableList<CuentasHistorialClienteTB> ListarAbonos(int idCuentasCliente) {
        ObservableList<CuentasHistorialClienteTB> empList = FXCollections.observableArrayList();
        PreparedStatement statementList = null;
        try {
            DBUtil.dbConnect();
            statementList = DBUtil.getConnection().prepareStatement("{call Sp_Listar_CuentasHistorial_By_IdCuenta(?)}");
            statementList.setInt(1, idCuentasCliente);
            try (ResultSet result = statementList.executeQuery()) {
                while (result.next()) {
                    CuentasHistorialClienteTB historialClienteTB = new CuentasHistorialClienteTB();
                    historialClienteTB.setIdCuentasHistorialCliente(result.getInt("IdCuentasHistorialCliente"));
                    historialClienteTB.setFechaAbono(result.getTimestamp("FechaAbono").toLocalDateTime());
                    historialClienteTB.setAbono(result.getDouble("Abono"));
                    historialClienteTB.setReferencia(result.getString("Referencia"));
                    empList.add(historialClienteTB);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Cuentas Historial cliente:" + ex.getLocalizedMessage());
        } finally {
            try {
                if (statementList != null) {
                    statementList.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return empList;
    }

    public static String Delete_CuentasHistorialCliente(int idCuentasHistorialClientes, MovimientoCajaTB movimientoCajaTB) {
        String result = "";
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statementDelete = null;
            PreparedStatement statementMovimiento = null;
            try {
                DBUtil.getConnection().setAutoCommit(false);
                statementDelete = DBUtil.getConnection().prepareStatement("DELETE FROM CuentasHistorialClienteTB WHERE IdCuentasHistorialCliente = ?");
                statementDelete.setInt(1, idCuentasHistorialClientes);
                statementDelete.addBatch();

                statementMovimiento = DBUtil.getConnection().prepareStatement("INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?)");
                statementMovimiento.setInt(1, movimientoCajaTB.getIdCaja());
                statementMovimiento.setString(2, movimientoCajaTB.getIdUsuario());
                statementMovimiento.setTimestamp(3, Timestamp.valueOf(movimientoCajaTB.getFechaMovimiento()));
                statementMovimiento.setString(4, movimientoCajaTB.getComentario());
                statementMovimiento.setString(5, movimientoCajaTB.getMovimiento());
                statementMovimiento.setDouble(6, movimientoCajaTB.getEntrada());
                statementMovimiento.setDouble(7, movimientoCajaTB.getSalidas());
                statementMovimiento.setDouble(8, movimientoCajaTB.getSaldo());
                statementMovimiento.addBatch();

                statementDelete.executeBatch();
                statementMovimiento.executeBatch();
                DBUtil.getConnection().commit();
                result = "deleted";
            } catch (SQLException ex) {
                try {
                    DBUtil.getConnection().rollback();
                } catch (SQLException e) {

                }
                result = ex.getLocalizedMessage();
            } finally {
                try {
                    if (statementDelete != null) {
                        statementDelete.close();
                    }
                    if (statementMovimiento != null) {
                        statementMovimiento.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    result = ex.getLocalizedMessage();
                }
            }
        } else {
            result = "No se puedo conectar al servidor, revise su conexión.";

        }
        return result;
    }

}
