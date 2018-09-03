package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DirectorioADO {

    public static ObservableList<DirectorioTB> ListPrincipal() throws ClassNotFoundException, SQLException {
        String selectStmt = "select CONCAT(per.ApellidoPaterno,' ',per.ApellidoMaterno,' ',per.PrimerNombre,' ',per.SegundoNombre) as Persona ,\n"
                + "dic.Atributo,dic.Valor1,dic.Valor2,dic.Valor3 FROM \n"
                + "PersonaTB as per inner join DirectorioTB as dic on per.IdPersona = dic.IdPersona";
        try {
            ResultSet rsEmps = DBUtil.dbExecuteQuery(selectStmt);
            ObservableList<DirectorioTB> empList = getEntityList(rsEmps);
            return empList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            throw e;
        }

    }

    private static ObservableList<DirectorioTB> getEntityList(ResultSet rs) throws SQLException, ClassNotFoundException {
        ObservableList<DirectorioTB> empList = FXCollections.observableArrayList();
        long count = 0;
        while (rs.next()) {
            count++;
            DirectorioTB emp = new DirectorioTB();
            emp.setId(count);
            emp.setIdPersona(new PersonaTB(rs.getString("Persona")));
            empList.add(emp);
        }
        return empList;
    }
}
