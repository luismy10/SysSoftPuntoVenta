
package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class RolADO {
    
     public static ObservableList<RolTB> RolList() {
        String selectStmt = "SELECT * FROM RolTB";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<RolTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RolTB rolTB = new RolTB();
                rolTB.setIdRol(resultSet.getInt("IdRol"));
                rolTB.setNombre(resultSet.getString("Nombre"));
                empList.add(rolTB);
            }
        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                DBUtil.dbDisconnect();

            } catch (SQLException ex) {

            }

        }
        return empList;
    }
    
}
