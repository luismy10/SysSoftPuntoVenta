package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProveedorADO {

    public static String CrudEntity(ProveedorTB proveedorTB) {
        String selectStmt = "{call Sp_Crud_Proveedor(?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdProveedor", proveedorTB.getIdProveedor().get());
            callableStatement.setInt("TipoDocumento", proveedorTB.getTipoDocumento());
            callableStatement.setString("NumeroDocumento", proveedorTB.getNumeroDocumento().get());
            callableStatement.setString("RazonSocial", proveedorTB.getRazonSocial().get());
            callableStatement.setString("NombreComercial", proveedorTB.getNombreComercial().get());
            callableStatement.setString("Pais", proveedorTB.getPais());
            callableStatement.setInt("Ciudad", proveedorTB.getCiudad());
            callableStatement.setInt("Ambito", proveedorTB.getAmbito());
            callableStatement.setInt("Estado", proveedorTB.getEstado());
            callableStatement.setString("UsuarioRegistro", proveedorTB.getUsuarioRegistro());

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

    public static ObservableList<ProveedorTB> ListProveedor(String... value) {
        String selectStmt = "{call Sp_Listar_Proveedor(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<ProveedorTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value[0]);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                ProveedorTB proveedorTB = new ProveedorTB();
                proveedorTB.setId(rsEmps.getRow());
                proveedorTB.setTipoDocumentoName(rsEmps.getString("Documento"));
                proveedorTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                proveedorTB.setRazonSocial(rsEmps.getString("RazonSocial"));
                proveedorTB.setNombreComercial(rsEmps.getString("NombreComercial"));
                proveedorTB.setEstadoName(rsEmps.getString("Estado"));
                proveedorTB.setFechaRegistro(rsEmps.getDate("FRegistro").toLocalDate());
                empList.add(proveedorTB);
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

    public static ArrayList<ProveedorTB> GetIdPersona(String documento) {
        String selectStmt = "{call Sp_Get_Proveedor_By_Id(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ArrayList<ProveedorTB> arrayList = new ArrayList<>();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, documento);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                ProveedorTB proveedorTB = new ProveedorTB();
                proveedorTB.setIdProveedor(rsEmps.getString("IdProveedor"));
                proveedorTB.setTipoDocumento(rsEmps.getInt("TipoDocumento"));
                proveedorTB.setRazonSocial(rsEmps.getString("RazonSocial"));
                proveedorTB.setNombreComercial(rsEmps.getString("NombreComercial"));
                proveedorTB.setPais(rsEmps.getString("Pais"));
                proveedorTB.setCiudad(rsEmps.getInt("Ciudad"));
                proveedorTB.setAmbito(rsEmps.getInt("Ambito"));
                proveedorTB.setEstado(rsEmps.getInt("Estado"));
                //
                arrayList.add(proveedorTB);
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

    public static String CrudRepresentante(RepresentanteTB representanteTB) {
        String selectStmt = "{call Sp_Crud_Proveedor(?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdProveedor",representanteTB.getIdProveedor());
            callableStatement.setString("IdPersona", representanteTB.getIdPersona());       

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

}
