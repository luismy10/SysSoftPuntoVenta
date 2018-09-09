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
        String selectStmt = "{call Sp_Crud_Persona(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setLong("IdPersona", personaTB.getIdPersona());
            callableStatement.setInt("TipoDocumento", personaTB.getTipoDocumento());
            callableStatement.setString("NumeroDocumento", personaTB.getNumeroDocumento().get());
            callableStatement.setString("ApellidoPaterno", personaTB.getApellidoPaterno().get());
            callableStatement.setString("ApellidoMaterno", personaTB.getApellidoMaterno());
            callableStatement.setString("PrimerNombre", personaTB.getPrimerNombre());
            callableStatement.setString("SegundoNombre", personaTB.getSegundoNombre());
            callableStatement.setInt("Sexo", personaTB.getSexo());
            callableStatement.setDate("FechaNacimiento", personaTB.getFechaNacimiento());
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

    public static ObservableList<PersonaTB> ListPersonas(String... value) {
        String selectStmt = "{call Sp_Listar_Personas(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<PersonaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value[0]);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                PersonaTB personaTB = new PersonaTB();
                personaTB.setId(rsEmps.getRow());
                personaTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                personaTB.setApellidoPaterno(rsEmps.getString("Datos"));
                personaTB.setFechaRegistro(rsEmps.getDate("FRegistro").toLocalDate());
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

    public static ArrayList<PersonaTB> GetIdPersona(String documento) {
        String selectStmt = "{call Sp_Get_Persona_By_Id(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ArrayList<PersonaTB> arrayList = new ArrayList<>();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, documento);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                PersonaTB personaTB = new PersonaTB();
                personaTB.setIdPersona(rsEmps.getLong("IdPersona"));
                personaTB.setTipoDocumento(rsEmps.getInt("TipoDocumento"));
                personaTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                personaTB.setApellidoPaterno(rsEmps.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmps.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmps.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmps.getString("SegundoNombre"));
                personaTB.setSexo(rsEmps.getInt("Sexo"));
                //facturación
                personaTB.setTipoDocumentoFacturacion(rsEmps.getInt("TipoFactura"));
                personaTB.setNumeroDocumentoFacturacion(rsEmps.getString("NumeroFactura"));
                personaTB.setRazonSocial(rsEmps.getString("RazonSocial"));
                personaTB.setNombreComercial(rsEmps.getString("NombreComercial"));
                //
                arrayList.add(personaTB);
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
