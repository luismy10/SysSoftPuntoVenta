package model;

import controller.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CajaADO {

    public static String AperturarCaja(CajaTB cajaTB) {
        String result = "";
        PreparedStatement statementCaja = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            String quey = "INSERT INTO CajaTB(Estado,IdUsuario,Contado,Calculado,Diferencia,FechaRegistro)VALUES(?,?,?,?,?,?)";
            statementCaja = DBUtil.getConnection().prepareStatement(quey);
            statementCaja.setBoolean(1, cajaTB.isEstado());
            statementCaja.setString(2, cajaTB.getIdUsuario());
            statementCaja.setDouble(3, cajaTB.getContado());
            statementCaja.setDouble(4, cajaTB.getCalculado());
            statementCaja.setDouble(5, cajaTB.getDiferencia());
            statementCaja.setTimestamp(6, Timestamp.valueOf(cajaTB.getFechaRegistro()));
            statementCaja.addBatch();

            statementCaja.executeBatch();
            DBUtil.getConnection().commit();
            result = "registrado";
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
                result = ex.getLocalizedMessage();
            } catch (SQLException ex1) {
                result = ex1.getLocalizedMessage();
            }
        } finally {
            try {
                if (statementCaja != null) {
                    statementCaja.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                result = ex.getLocalizedMessage();
            }
        }
        return result;
    }

    public static CajaTB ValidarCreacionCaja(String idUsuario) {
        CajaTB cajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT IdCaja,Estado FROM CajaTB WHERE IdUsuario = ? AND CAST(FechaRegistro AS DATE) = CAST(GETDATE() AS DATE) AND Estado = 1");
            statementVentas.setString(1, idUsuario);
            try (ResultSet result = statementVentas.executeQuery()) {
                if (result.next()) {
                    cajaTB = new CajaTB();
                    cajaTB.setIdCaja(result.getInt("IdCaja"));
                    cajaTB.setEstado(result.getBoolean("Estado"));
                }
            }
        } catch (SQLException ex) {

        } finally {
            try {
                if (statementVentas != null) {
                    statementVentas.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return cajaTB;
    }

    public static CajaTB ValidarAperturaCaja(int idCaja) {
        CajaTB cajaTB = null;
        PreparedStatement statementCaja = null;
        try {
            DBUtil.dbConnect();
            statementCaja = DBUtil.getConnection().prepareStatement("SELECT IdCaja,Estado FROM CajaTB WHERE idCaja = ? AND CAST(FechaApertura AS DATE) = CAST(GETDATE() AS DATE) AND Estado = 1");
            statementCaja.setInt(1, idCaja);
            try (ResultSet result = statementCaja.executeQuery()) {
                if (result.next()) {
                    cajaTB = new CajaTB();
                    cajaTB.setIdCaja(result.getInt("IdCaja"));
                    cajaTB.setEstado(result.getBoolean("Estado"));
                }
            }
        } catch (SQLException ex) {

        } finally {
            try {
                if (statementCaja != null) {
                    statementCaja.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return cajaTB;
    }

    public static String CerarAperturaCaja(int idCaja, LocalDateTime localDateTime, boolean state, double contado,double calculado) {
        String cajaTB = "";
        PreparedStatement statementCaja = null;
        PreparedStatement movimiento_caja = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            statementCaja = DBUtil.getConnection().prepareStatement("UPDATE CajaTB SET FechaCierre = ?,Contado = ?,Calculado = ?,Diferencia = ?,Estado=? WHERE IdCaja = ?");
            statementCaja.setTimestamp(1, Timestamp.valueOf(localDateTime));
            statementCaja.setDouble(2, contado);
            statementCaja.setDouble(3, calculado);
            statementCaja.setDouble(4, contado - calculado);
            statementCaja.setBoolean(5, state);
            statementCaja.setInt(6, idCaja);
            statementCaja.addBatch();

            movimiento_caja = DBUtil.getConnection().prepareStatement("INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?)");
            movimiento_caja.setInt(1, idCaja);
            movimiento_caja.setString(2, Session.USER_ID);
            movimiento_caja.setTimestamp(3, Timestamp.valueOf(localDateTime));
            movimiento_caja.setString(4, "Finalización del turno");
            movimiento_caja.setString(5, "CERRAR");
            movimiento_caja.setDouble(6, contado);
            movimiento_caja.setDouble(7, 0);
            movimiento_caja.setDouble(8, contado - 0);
            movimiento_caja.addBatch();

            statementCaja.executeBatch();
            movimiento_caja.executeBatch();
            DBUtil.getConnection().commit();
            cajaTB = "completed";
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
            } catch (SQLException e) {

            }
            cajaTB = ex.getLocalizedMessage();
        } finally {
            try {
                if (statementCaja != null) {
                    statementCaja.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return cajaTB;
    }

    public static ObservableList<CajaTB> ListarCajasAperturadas(String fechaInicial, String fechaFinal) {
        PreparedStatement statementLista = null;
        ObservableList<CajaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            statementLista = DBUtil.getConnection().prepareStatement("{call Sp_ListarCajasAperturadas(?,?)}");
            statementLista.setString(1, fechaInicial);
            statementLista.setString(2, fechaFinal);
            try (ResultSet result = statementLista.executeQuery()) {
                while (result.next()) {                   
                    CajaTB cajaTB = new CajaTB();
                    cajaTB.setIdCaja(result.getInt("IdCaja"));
                    cajaTB.setFechaApertura(result.getTimestamp("FechaApertura").toLocalDateTime());
                    cajaTB.setFechaCierre(result.getTimestamp("FechaCierre").toLocalDateTime());
                    cajaTB.setEstado(result.getBoolean("Estado"));
                    cajaTB.setContado(result.getDouble("Contado"));
                    cajaTB.setCalculado(result.getDouble("Calculado"));
                    cajaTB.setDiferencia(result.getDouble("Diferencia"));
                    cajaTB.setEmpleadoTB(new EmpleadoTB(result.getString("Apellidos"), result.getString("Nombres")));
                    empList.add(cajaTB);
                }
                
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        } finally {
            try {
                if (statementLista != null) {
                    statementLista.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return empList;
    }

}
