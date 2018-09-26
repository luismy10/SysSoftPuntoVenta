package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompraADO {

    public static String CrudEntity(CompraTB compraTB) {
        PreparedStatement compra = null;
        CallableStatement codigo_compra = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            codigo_compra = DBUtil.getConnection().prepareCall("{? = call Fc_Compra_Codigo_Alfanumerico()}");
            codigo_compra.registerOutParameter(1, java.sql.Types.VARCHAR);
            codigo_compra.execute();
            String id_compra = codigo_compra.getString(1);

            compra = DBUtil.getConnection().prepareStatement("INSERT INTO CompraTB(IdCompra,Proveedor,Representante,Comprobante,Numeracion,FechaCompra,SubTotal,Descuento,Gravada,Igv,Total) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            compra.setString(1, id_compra);
            compra.setString(2, compraTB.getProveedor());
            compra.setString(3, compraTB.getRepresentante());
            compra.setInt(4, compraTB.getComprobante());
            compra.setString(5, compraTB.getNumeracion().toUpperCase());
            compra.setTimestamp(6, compraTB.getFechaRegistro());
            compra.setDouble(7, compraTB.getSubTotal());
            compra.setDouble(8, compraTB.getDescuento());
            compra.setDouble(9, compraTB.getGravada());
            compra.setDouble(10, compraTB.getIgv());
            compra.setDouble(11, compraTB.getTotal().get());
            compra.addBatch();
            compra.executeBatch();
            DBUtil.getConnection().commit();
            return "register";
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
                return ex.getLocalizedMessage();
            } catch (SQLException ex1) {
                return ex1.getLocalizedMessage();
            }
        } finally {
            try {
                if (compra != null) {
                    compra.close();
                }
                if (codigo_compra != null) {
                    codigo_compra.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {

            }
        }

    }
    
     public static ObservableList<CompraTB> ListCompras(String... value) {
        String selectStmt = "{call Sp_Listar_Compras()}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<CompraTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
//            preparedStatement.setString(1, value[0]);
//            preparedStatement.setString(2, value[1]);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                CompraTB compraTB = new CompraTB();
                compraTB.setId(rsEmps.getRow());
                compraTB.setFechaCompra(rsEmps.getDate("Fecha").toLocalDate());
                compraTB.setProveedorTB(new ProveedorTB(rsEmps.getString("NumeroDocumento"),rsEmps.getString("RazonSocial")));
                compraTB.setTotal(rsEmps.getDouble("Total"));
                empList.add(compraTB);
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
