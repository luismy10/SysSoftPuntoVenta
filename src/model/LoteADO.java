package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LoteADO {

    public static ObservableList<LoteTB> ListByIdLote(String value) {
        String selectStmt = "select l.ExistenciaInicial,l.ExistenciaActual,l.FechaCaducidad from LoteTB as l\n"
                + "where l.IdArticulo = ? ";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<LoteTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                LoteTB loteTB = new LoteTB();
                loteTB.setExistenciaInicial(rsEmps.getDouble("ExistenciaInicial"));
                loteTB.setExistenciaActual(rsEmps.getDouble("ExistenciaActual"));
                loteTB.setFechaCaducidad(rsEmps.getDate("FechaCaducidad").toLocalDate());
                empList.add(loteTB);
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

    public static ObservableList<LoteTB> ListLote(String value) {
        String selectStmt = "{call Sp_Listar_Lote(?)}";
        CallableStatement callableStatement = null;
        ResultSet rsEmps = null;
        ObservableList<LoteTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString(1, value);
            rsEmps = callableStatement.executeQuery();

            while (rsEmps.next()) {
                LoteTB loteTB = new LoteTB();
                loteTB.setId(rsEmps.getInt("Filas"));
                loteTB.setNumeroLote(rsEmps.getString("NumeroLote"));
                loteTB.setArticuloTB(new ArticuloTB(rsEmps.getString("Clave"), rsEmps.getString("NombreMarca")));
                loteTB.setFechaFabricacion(rsEmps.getDate("FechaFabricacion").toLocalDate());
                loteTB.setFechaCaducidad(rsEmps.getDate("FechaCaducidad").toLocalDate());
                loteTB.setExistenciaInicial(rsEmps.getDouble("ExistenciaInicial"));
                loteTB.setExistenciaActual(rsEmps.getDouble("ExistenciaActual"));
                empList.add(loteTB);
            }
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);

        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
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
