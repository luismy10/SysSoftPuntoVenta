package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MovimientoCajaADO {

    public static MovimientoCajaTB FondoCaja(int idCaja) {
        MovimientoCajaTB movimientoCajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT * FROM MovimientoCajaTB WHERE IdCaja = ? AND Movimiento = ?");
            statementVentas.setInt(1, idCaja);
            statementVentas.setString(2, "FONC");
            if (statementVentas.executeQuery().next()) {
                movimientoCajaTB = new MovimientoCajaTB();
                try (ResultSet result = statementVentas.executeQuery()) {
                    while (result.next()) {
                        movimientoCajaTB.setSaldo(movimientoCajaTB.getSaldo() + result.getDouble("Saldo"));
                    }
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
        return movimientoCajaTB;
    }

    public static MovimientoCajaTB VentasEfectivo(int idCaja) {
        MovimientoCajaTB movimientoCajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT * FROM MovimientoCajaTB WHERE IdCaja = ? AND Movimiento = ?");
            statementVentas.setInt(1, idCaja);
            statementVentas.setString(2, "VEN");
            if (statementVentas.executeQuery().next()) {
                movimientoCajaTB = new MovimientoCajaTB();
                try (ResultSet result = statementVentas.executeQuery()) {
                    while (result.next()) {
                        movimientoCajaTB.setSaldo(movimientoCajaTB.getSaldo() + result.getDouble("Saldo"));
                    }
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
        return movimientoCajaTB;
    }

    public static MovimientoCajaTB VentasCredito(int idCaja) {
        MovimientoCajaTB movimientoCajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT * FROM MovimientoCajaTB WHERE IdCaja = ? AND Movimiento = ?");
            statementVentas.setInt(1, idCaja);
            statementVentas.setString(2, "VENCRE");
            if (statementVentas.executeQuery().next()) {
                movimientoCajaTB = new MovimientoCajaTB();
                try (ResultSet result = statementVentas.executeQuery()) {
                    while (result.next()) {
                        movimientoCajaTB.setSaldo(movimientoCajaTB.getSaldo() + result.getDouble("Saldo"));
                    }
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
        return movimientoCajaTB;
    }

    public static MovimientoCajaTB EgresosEfectivoCompra(int idCaja) {
        MovimientoCajaTB movimientoCajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT * FROM MovimientoCajaTB WHERE IdCaja = ? AND Movimiento = ?");
            statementVentas.setInt(1, idCaja);
            statementVentas.setString(2, "COM");
            if (statementVentas.executeQuery().next()) {
                movimientoCajaTB = new MovimientoCajaTB();
                try (ResultSet result = statementVentas.executeQuery()) {
                    while (result.next()) {
                        movimientoCajaTB.setSaldo(movimientoCajaTB.getSaldo() + result.getDouble("Saldo"));
                    }
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
        return movimientoCajaTB;
    }

    public static MovimientoCajaTB IngresosEfectivo(int idCaja) {
        MovimientoCajaTB movimientoCajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT * FROM MovimientoCajaTB WHERE IdCaja = ? AND Movimiento = ?");
            statementVentas.setInt(1, idCaja);
            statementVentas.setString(2, "ENTR");
            if (statementVentas.executeQuery().next()) {
                movimientoCajaTB = new MovimientoCajaTB();
                try (ResultSet result = statementVentas.executeQuery()) {
                    while (result.next()) {
                        movimientoCajaTB.setSaldo(movimientoCajaTB.getSaldo() + result.getDouble("Saldo"));
                    }
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
        return movimientoCajaTB;
    }

    public static MovimientoCajaTB EgresosEfectivo(int idCaja) {
        MovimientoCajaTB movimientoCajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT * FROM MovimientoCajaTB WHERE IdCaja = ? AND Movimiento = ?");
            statementVentas.setInt(1, idCaja);
            statementVentas.setString(2, "SALI");
            if (statementVentas.executeQuery().next()) {
                movimientoCajaTB = new MovimientoCajaTB();
                try (ResultSet result = statementVentas.executeQuery()) {
                    while (result.next()) {
                        movimientoCajaTB.setSaldo(movimientoCajaTB.getSaldo() + result.getDouble("Saldo"));
                    }
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
        return movimientoCajaTB;
    }

    public static MovimientoCajaTB DevolucionesEfectivo(int idCaja) {
        MovimientoCajaTB movimientoCajaTB = null;
        PreparedStatement statementVentas = null;
        try {
            DBUtil.dbConnect();
            statementVentas = DBUtil.getConnection().prepareStatement("SELECT * FROM MovimientoCajaTB WHERE IdCaja = ? AND Movimiento = ?");
            statementVentas.setInt(1, idCaja);
            statementVentas.setString(2, "VENCAN");
            if (statementVentas.executeQuery().next()) {
                movimientoCajaTB = new MovimientoCajaTB();
                try (ResultSet result = statementVentas.executeQuery()) {
                    while (result.next()) {
                        movimientoCajaTB.setSaldo(movimientoCajaTB.getSaldo() + result.getDouble("Saldo"));
                    }
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
        return movimientoCajaTB;
    }

    public static String AperturarCaja_Movimiento(CajaTB cajaTB, MovimientoCajaTB movimientoCajaTB) {
        String result = "";
        PreparedStatement statementCaja = null;
        PreparedStatement statementMovimiento = null;
        final String queryCaja = "UPDATE CajaTB SET FechaApertura = ?,FechaCierre = ?  WHERE IdCaja = ?";
        final String queryMovimiento = "INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?)";
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            statementCaja = DBUtil.getConnection().prepareStatement(queryCaja);
            statementCaja.setTimestamp(1, Timestamp.valueOf(cajaTB.getFechaApertura()));
            statementCaja.setTimestamp(2, Timestamp.valueOf(cajaTB.getFechaApertura()));
            statementCaja.setInt(3, cajaTB.getIdCaja());
            statementCaja.addBatch();

            statementMovimiento = DBUtil.getConnection().prepareStatement(queryMovimiento);
            statementMovimiento.setInt(1, movimientoCajaTB.getIdCaja());
            statementMovimiento.setString(2, movimientoCajaTB.getIdUsuario());
            statementMovimiento.setTimestamp(3, Timestamp.valueOf(movimientoCajaTB.getFechaMovimiento()));
            statementMovimiento.setString(4, movimientoCajaTB.getComentario());
            statementMovimiento.setString(5, movimientoCajaTB.getMovimiento());
            statementMovimiento.setDouble(6, movimientoCajaTB.getEntrada());
            statementMovimiento.setDouble(7, movimientoCajaTB.getSalidas());
            statementMovimiento.setDouble(8, movimientoCajaTB.getSaldo());
            statementMovimiento.addBatch();

            statementCaja.executeBatch();
            statementMovimiento.executeBatch();
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
                if (statementMovimiento != null) {
                    statementMovimiento.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                result = ex.getLocalizedMessage();
            }
        }
        return result;
    }

    public static String Registrar_Movimiento(MovimientoCajaTB movimientoCajaTB) {
        String result = "";
        PreparedStatement statementMovimiento = null;
        final String queryMovimiento = "INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?)";
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            statementMovimiento = DBUtil.getConnection().prepareStatement(queryMovimiento);
            statementMovimiento.setInt(1, movimientoCajaTB.getIdCaja());
            statementMovimiento.setString(2, movimientoCajaTB.getIdUsuario());
            statementMovimiento.setTimestamp(3, Timestamp.valueOf(movimientoCajaTB.getFechaMovimiento()));
            statementMovimiento.setString(4, movimientoCajaTB.getComentario());
            statementMovimiento.setString(5, movimientoCajaTB.getMovimiento());
            statementMovimiento.setDouble(6, movimientoCajaTB.getEntrada());
            statementMovimiento.setDouble(7, movimientoCajaTB.getSalidas());
            statementMovimiento.setDouble(8, movimientoCajaTB.getSaldo());
            statementMovimiento.addBatch();

            statementMovimiento.executeBatch();
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
                if (statementMovimiento != null) {
                    statementMovimiento.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                result = ex.getLocalizedMessage();
            }
        }
        return result;
    }

    public static ObservableList<MovimientoCajaTB> ListarCajasAperturadas(int idCaja) {
        PreparedStatement statementLista = null;
        ObservableList<MovimientoCajaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            statementLista = DBUtil.getConnection().prepareStatement("{call Sp_Lista_Movimiento_Caja_ById(?)}");
            statementLista.setInt(1, idCaja);
            try (ResultSet result = statementLista.executeQuery()) {
                while (result.next()) {
                    MovimientoCajaTB movimientoCajaTB = new MovimientoCajaTB();
                    movimientoCajaTB.setFechaMovimiento(result.getTimestamp("FechaMovimiento").toLocalDateTime());
                    movimientoCajaTB.setComentario(result.getString("Comentario"));
                    movimientoCajaTB.setMovimiento(result.getString("Movimiento"));
                    movimientoCajaTB.setEntrada(result.getDouble("Entrada"));
                    movimientoCajaTB.setSalidas(result.getDouble("Salidas"));
                    empList.add(movimientoCajaTB);
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
