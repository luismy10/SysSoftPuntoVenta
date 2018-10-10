package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LoteADO {

    public static ObservableList<LoteTB> ListProveedor(String value) {
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

}
