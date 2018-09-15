package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            emp.setPersona(new PersonaTB(rs.getString("Persona")));
            empList.add(emp);
        }
        return empList;
    }
    
    public static String CrudEntity(DirectorioTB directorioTB){
        String selectStmt = "{call Sp_Crud_Directorio(?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setLong("IdDirectorio", directorioTB.getIdDirectorio());
            callableStatement.setInt("Atributo", directorioTB.getAtributo());
            callableStatement.setString("Valor", directorioTB.getValor());
            callableStatement.setString("IdPersona", directorioTB.getIdPersona());
            
            callableStatement.registerOutParameter("Message", java.sql.Types.VARCHAR, 20);
            callableStatement.execute();
            return callableStatement.getString("Message");
        } catch (SQLException e) {
            return e.getLocalizedMessage();
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                return ex.getLocalizedMessage();
            }
        }
    }
    
     public static ArrayList<DirectorioTB> GetIdDirectorio(String documento) {
        String selectStmt = "{call Sp_Get_Directorio_By_Id(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ArrayList<DirectorioTB> arrayList = new ArrayList<>();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, documento);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                DirectorioTB directorioTB = new DirectorioTB();
                directorioTB.setIdDirectorio(rsEmps.getLong("IdDirectorio"));
                directorioTB.setAtributo(rsEmps.getInt("Atributo"));
                directorioTB.setNombre(rsEmps.getString("Nombre"));
                directorioTB.setValor(rsEmps.getString("Valor"));
                directorioTB.setIdPersona(rsEmps.getString("IdPersona"));
                arrayList.add(directorioTB);
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
        return arrayList;
    }
    
}
