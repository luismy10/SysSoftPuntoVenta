package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CajaADO {

    public static String AperturarCaja(CajaTB cajaTB) {
        String result = "";
        PreparedStatement statementCaja = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            String quey = "INSERT INTO CajaTB(Estado,IdUsuario,FechaRegistro)VALUES(?,?,?)";
            statementCaja = DBUtil.getConnection().prepareStatement(quey);
            statementCaja.setBoolean(1, cajaTB.isEstado());
            statementCaja.setString(2, cajaTB.getIdUsuario());
            statementCaja.setTimestamp(3, Timestamp.valueOf(cajaTB.getFechaRegistro()));
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

}
