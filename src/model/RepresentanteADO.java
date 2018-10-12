package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepresentanteADO {

    public static String CrudRepresentante(RepresentanteTB representanteTB) {
        String selectStmt = "{call Sp_Crud_Representante(?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdRepresentante", representanteTB.getIdRepresentante());
            callableStatement.setInt("TipoDocumento", representanteTB.getTipoDocumento());
            callableStatement.setString("NumeroDocumento", representanteTB.getNumeroDocumento());
            callableStatement.setString("Apellidos", representanteTB.getApellidos());
            callableStatement.setString("Nombres", representanteTB.getNombres());
            callableStatement.setString("Telefono", representanteTB.getTelefono());
            callableStatement.setString("Celular", representanteTB.getCelular());
            callableStatement.setString("Email", representanteTB.getEmail());
            callableStatement.setString("Direccion", representanteTB.getDireccion());
            callableStatement.setString("IdProveedor", representanteTB.getIdProveedor());
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

    public static String DeleteRepresentante(String idProveedor, String idPersona) {
        String selectStmt = "delete from ProveedorPersonaTB where IdProveedor = ? and IdPersona = ?";
        PreparedStatement preparedStatement = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, idProveedor);
            preparedStatement.setString(2, idPersona);
            return preparedStatement.executeUpdate() == 1 ? "eliminado" : "error";
        } catch (SQLException e) {
            return "La operación de selección de SQL ha fallado: " + e.getLocalizedMessage();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                return "La consulta se cerro antes de finalizar: " + ex.getLocalizedMessage();
            }
        }
    }

    public static ObservableList<RepresentanteTB> ListRepresentantes(String... value) {
        String selectStmt = "{call Sp_Listar_Representante(?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<RepresentanteTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value[0]);
            preparedStatement.setString(2, value[1]);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                RepresentanteTB representanteTB = new RepresentanteTB();
                representanteTB.setId(rsEmps.getInt("Filas"));
                representanteTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento")); 
                representanteTB.setApellidos(rsEmps.getString("Apellidos"));
                representanteTB.setNombres(rsEmps.getString("Nombres"));
                representanteTB.setTelefono(rsEmps.getString("Telefono"));
                representanteTB.setCelular(rsEmps.getString("Celular"));
                empList.add(representanteTB);
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
