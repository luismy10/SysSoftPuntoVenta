package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonaADO {

    public static String CrudEntity(PersonaTB personaTB) {
        String selectStmt = "{call Sp_Crud_Persona(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdPersona", personaTB.getIdPersona().get());
            callableStatement.setInt("TipoDocumento", personaTB.getTipoDocumento());
            callableStatement.setString("NumeroDocumento", personaTB.getNumeroDocumento().get());
            callableStatement.setString("ApellidoPaterno", personaTB.getApellidoPaterno());
            callableStatement.setString("ApellidoMaterno", personaTB.getApellidoMaterno());
            callableStatement.setString("PrimerNombre", personaTB.getPrimerNombre());
            callableStatement.setString("SegundoNombre", personaTB.getSegundoNombre());
            callableStatement.setInt("Sexo", personaTB.getSexo());
            callableStatement.setDate("FechaNacimiento", personaTB.getFechaNacimiento());
//            callableStatement.setInt("Estado", personaTB.getEstado());
            callableStatement.setString("UsuarioRegistro", personaTB.getUsuarioRegistro());
            //facturación
            callableStatement.setInt("TipoDocumentoFactura", personaTB.getTipoDocumentoFacturacion());
            callableStatement.setString("NumeroDocumentoFactura", personaTB.getNumeroDocumentoFacturacion());
            callableStatement.setString("RazonSocial", personaTB.getRazonSocial());
            callableStatement.setString("NombreComercial", personaTB.getNombreComercial());
            //
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

    public static String GetPersonasId(String value) {
        String selectStmt = "SELECT IdPersona FROM PersonaTB WHERE NumeroDocumento = ?";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        String IdPersona = "";
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                IdPersona = rsEmps.getString("IdPersona");
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
        return IdPersona;
    }

    
    public static ObservableList<PersonaTB> ListPersonasRepresentantes(String value) {
        String selectStmt = "{call Sp_Listar_Persona_Representante(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<PersonaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                PersonaTB personaTB = new PersonaTB();
                personaTB.setId(rsEmps.getRow());
                personaTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                personaTB.setApellidoPaterno(rsEmps.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmps.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmps.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmps.getString("SegundoNombre"));
                empList.add(personaTB);
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

    public static ObservableList<PersonaTB> ListRepresentantes(String... value) {
        String selectStmt = "{call Sp_Listar_Representantes(?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<PersonaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value[0]);
            preparedStatement.setString(2, value[1]);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                PersonaTB personaTB = new PersonaTB();
                personaTB.setId(rsEmps.getRow());
                personaTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                personaTB.setApellidoPaterno(rsEmps.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmps.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmps.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmps.getString("SegundoNombre"));
//                personaTB.setEstadoName(rsEmps.getString("Estado")); 
                empList.add(personaTB);
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
