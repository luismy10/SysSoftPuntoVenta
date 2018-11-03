package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClienteADO {

    public static String CrudCliente(ClienteTB clienteTB) {
        String selectStmt = "{call Sp_Crud_Persona_Cliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            //persona
            callableStatement.setString("IdPersona", clienteTB.getPersonaTB().getIdPersona().get());
            callableStatement.setInt("TipoDocumento", clienteTB.getPersonaTB().getTipoDocumento());
            callableStatement.setString("NumeroDocumento", clienteTB.getPersonaTB().getNumeroDocumento().get());
            callableStatement.setString("ApellidoPaterno", clienteTB.getPersonaTB().getApellidoPaterno());
            callableStatement.setString("ApellidoMaterno", clienteTB.getPersonaTB().getApellidoMaterno());
            callableStatement.setString("PrimerNombre", clienteTB.getPersonaTB().getPrimerNombre());
            callableStatement.setString("SegundoNombre", clienteTB.getPersonaTB().getSegundoNombre());
            callableStatement.setInt("Sexo", clienteTB.getPersonaTB().getSexo());
            callableStatement.setDate("FechaNacimiento", clienteTB.getPersonaTB().getFechaNacimiento());
            //cliente
            callableStatement.setString("IdCliente", clienteTB.getIdCliente());
            callableStatement.setString("Telefono", clienteTB.getTelefono());
            callableStatement.setString("Celular", clienteTB.getCelular());
            callableStatement.setString("Email", clienteTB.getEmail());
            callableStatement.setString("Direccion", clienteTB.getDireccion());
            callableStatement.setInt("Estado", clienteTB.getEstado());
            callableStatement.setString("UsuarioRegistro", clienteTB.getUsuarioRegistro());
            //facturación
            callableStatement.setInt("TipoDocumentoFactura", clienteTB.getFacturacionTB().getTipoDocumentoFacturacion());
            callableStatement.setString("NumeroDocumentoFactura", clienteTB.getFacturacionTB().getNumeroDocumentoFacturacion());
            callableStatement.setString("RazonSocial", clienteTB.getFacturacionTB().getRazonSocial());
            callableStatement.setString("NombreComercial", clienteTB.getFacturacionTB().getNombreComercial());
            callableStatement.setString("Pais", clienteTB.getFacturacionTB().getPais());
            callableStatement.setInt("Ciudad", clienteTB.getFacturacionTB().getDepartamento());
            callableStatement.setInt("Provincia", clienteTB.getFacturacionTB().getProvincia());
            callableStatement.setInt("Distrito", clienteTB.getFacturacionTB().getDistrito());
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

    public static ObservableList<ClienteTB> ListCliente(String... value) {
        String selectStmt = "{call Sp_Listar_Clientes(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<ClienteTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value[0]);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                ClienteTB clienteTB = new ClienteTB();
                clienteTB.setId(rsEmps.getLong("Filas"));
                clienteTB.setIdPersona(rsEmps.getString("IdPersona"));
                PersonaTB personaTB = new PersonaTB();
                personaTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                personaTB.setApellidoPaterno(rsEmps.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmps.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmps.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmps.getString("SegundoNombre"));
                clienteTB.setFechaRegistro(rsEmps.getDate("FRegistro").toLocalDate());
                clienteTB.setTelefono(rsEmps.getString("Telefono"));
                clienteTB.setCelular(rsEmps.getString("Celular"));
                clienteTB.setEstadoName(rsEmps.getString("Estado"));
                clienteTB.setPersonaTB(personaTB);
                empList.add(clienteTB);
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

    public static ObservableList<ClienteTB> ListClienteVenta(String value) {
        String selectStmt = "{call Sp_Listar_Clientes_Venta(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<ClienteTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                ClienteTB clienteTB = new ClienteTB();
                clienteTB.setId(rsEmps.getLong("Filas"));
                clienteTB.setIdCliente(rsEmps.getString("IdCliente"));
                PersonaTB personaTB = new PersonaTB();
                personaTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                personaTB.setApellidoPaterno(rsEmps.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmps.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmps.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmps.getString("SegundoNombre"));
                clienteTB.setPersonaTB(personaTB);
                empList.add(clienteTB);
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

    public static ClienteTB GetByIdCliente(String documento) {
        String selectStmt = "{call Sp_Get_Cliente_By_Id(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ClienteTB clienteTB = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, documento);
            rsEmps = preparedStatement.executeQuery();

            if (rsEmps.next()) {
                clienteTB = new ClienteTB();
                clienteTB.setIdCliente(rsEmps.getString("IdCliente"));
                clienteTB.setTelefono(rsEmps.getString("Telefono"));
                clienteTB.setCelular(rsEmps.getString("Celular"));
                clienteTB.setEmail(rsEmps.getString("Email"));
                clienteTB.setDireccion(rsEmps.getString("Direccion"));
                clienteTB.setEstado(rsEmps.getInt("Estado"));
                //persona
                PersonaTB personaTB = new PersonaTB();
                personaTB.setIdPersona(rsEmps.getString("IdPersona"));
                personaTB.setTipoDocumento(rsEmps.getInt("TipoDocumento"));
                personaTB.setNumeroDocumento(rsEmps.getString("NumeroDocumento"));
                personaTB.setApellidoPaterno(rsEmps.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmps.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmps.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmps.getString("SegundoNombre"));
                personaTB.setSexo(rsEmps.getInt("Sexo"));
                personaTB.setFechaNacimiento(rsEmps.getDate("FechaNacimiento"));
                //facturación
                FacturacionTB facturacionTB = new FacturacionTB();
                facturacionTB.setTipoDocumentoFacturacion(rsEmps.getInt("TipoFactura"));
                facturacionTB.setNumeroDocumentoFacturacion(rsEmps.getString("NumeroFactura"));
                facturacionTB.setRazonSocial(rsEmps.getString("RazonSocial"));
                facturacionTB.setNombreComercial(rsEmps.getString("NombreComercial"));
                facturacionTB.setPais(rsEmps.getString("Pais"));
                facturacionTB.setDepartamento(rsEmps.getInt("Ciudad"));
                facturacionTB.setProvincia(rsEmps.getInt("Provincia"));
                facturacionTB.setDistrito(rsEmps.getInt("Distrito"));
                clienteTB.setPersonaTB(personaTB);
                clienteTB.setFacturacionTB(facturacionTB);
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
        return clienteTB;
    }

    public static ClienteTB GetByIdClienteVenta(String documento) {
        String selectStmt = "select ci.IdCliente,\n"
                + "		p.ApellidoPaterno,p.ApellidoMaterno,p.PrimerNombre,p.SegundoNombre\n"
                + "		from ClienteTB as ci inner join  PersonaTB as p on ci.IdPersona = p.IdPersona inner join FacturacionTB as f\n"
                + "		on ci.IdCliente = f.IdCliente\n"
                + "		where p.NumeroDocumento = ?";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ClienteTB clienteTB = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, documento);
            rsEmps = preparedStatement.executeQuery();

            if (rsEmps.next()) {
                clienteTB = new ClienteTB();
                clienteTB.setIdCliente(rsEmps.getString("IdCliente"));
                //persona
                PersonaTB personaTB = new PersonaTB();
                personaTB.setApellidoPaterno(rsEmps.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmps.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmps.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmps.getString("SegundoNombre"));
                clienteTB.setPersonaTB(personaTB);

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
        return clienteTB;
    }

}
