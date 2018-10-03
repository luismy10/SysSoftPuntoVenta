package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class CompraADO {

    public static String CrudEntity(CompraTB compraTB, TableView<ArticuloTB> tableView) {
        PreparedStatement compra = null;
        CallableStatement codigo_compra = null;

        PreparedStatement detalle_compra = null;

        PreparedStatement articulo_update = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            codigo_compra = DBUtil.getConnection().prepareCall("{? = call Fc_Compra_Codigo_Alfanumerico()}");
            codigo_compra.registerOutParameter(1, java.sql.Types.VARCHAR);
            codigo_compra.execute();
            String id_compra = codigo_compra.getString(1);

            compra = DBUtil.getConnection().prepareStatement("INSERT INTO CompraTB(IdCompra,Proveedor,Representante,Comprobante,Numeracion,FechaCompra,SubTotal,Descuento,Gravada,Igv,Total) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?)");

            detalle_compra = DBUtil.getConnection().prepareStatement("INSERT INTO DetalleCompraTB(IdCompra,IdArticulo,Cantidad,PrecioCompra,PrecioVenta,Importe)"
                    + "VALUES(?,?,?,?,?,?)");

            articulo_update = DBUtil.getConnection().prepareStatement("update ArticuloTB SET PrecioCompra = ?, PrecioVenta = ?, Cantidad = Cantidad + ? WHERE IdArticulo = ?");

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

            for (int i = 0; i < tableView.getItems().size(); i++) {
                detalle_compra.setString(1, id_compra);
                detalle_compra.setString(2, tableView.getItems().get(i).getIdArticulo());
                detalle_compra.setDouble(3, tableView.getItems().get(i).getCantidad().get());
                detalle_compra.setDouble(4, tableView.getItems().get(i).getPrecioCompra());
                detalle_compra.setDouble(5, tableView.getItems().get(i).getPrecioVenta().get());
                detalle_compra.setDouble(6, tableView.getItems().get(i).getImporte().get());
                detalle_compra.addBatch();

                articulo_update.setDouble(1, tableView.getItems().get(i).getPrecioCompra());
                articulo_update.setDouble(2, tableView.getItems().get(i).getPrecioVenta().get());
                articulo_update.setDouble(3, tableView.getItems().get(i).getCantidad().get());
                articulo_update.setString(4, tableView.getItems().get(i).getIdArticulo());
                articulo_update.addBatch();

            }

            compra.executeBatch();
            detalle_compra.executeBatch();
            articulo_update.executeBatch();

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
                if (detalle_compra != null) {
                    detalle_compra.close();
                }
                if (articulo_update != null) {
                    articulo_update.close();
                }
                if (codigo_compra != null) {
                    codigo_compra.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {

            }
        }

    }

    public static ObservableList<CompraTB> ListComprasRealizadas(String... value) {
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
                compraTB.setIdCompra(rsEmps.getString("IdCompra"));
                compraTB.setFechaCompra(rsEmps.getDate("Fecha").toLocalDate());
                compraTB.setProveedorTB(new ProveedorTB(rsEmps.getString("NumeroDocumento"), rsEmps.getString("RazonSocial")));
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

    public static ArrayList<CompraTB> ListCompra(String value) {
        String selectStmtCompra = "select CAST(FechaCompra as date) as Fecha,dbo.Fc_Obtener_Nombre_Detalle(Comprobante,'0009') as Comprobante,Numeracion,Total from CompraTB\n"
                + "where IdCompra = ? ";

        PreparedStatement preparedStatementCompra = null;
        ResultSet rsEmps = null;

        ArrayList<CompraTB> listCompra = new ArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatementCompra = DBUtil.getConnection().prepareStatement(selectStmtCompra);
            preparedStatementCompra.setString(1, value);

            rsEmps = preparedStatementCompra.executeQuery();

            while (rsEmps.next()) {
                CompraTB compraTB = new CompraTB();
                compraTB.setFechaCompra(rsEmps.getDate("Fecha").toLocalDate());
                compraTB.setComprobanteName(rsEmps.getString("Comprobante"));
                compraTB.setNumeracion(rsEmps.getString("Numeracion"));
                compraTB.setTotal(rsEmps.getDouble("Total"));
                listCompra.add(compraTB);
            }

        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);

        } finally {
            try {
                if (preparedStatementCompra != null) {
                    preparedStatementCompra.close();
                }
                if (rsEmps != null) {
                    rsEmps.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return listCompra;
    }

    public static ArrayList<ProveedorTB> ListCompraProveedor(String value) {

        String selectStmtProveedor = "select p.NumeroDocumento,p.RazonSocial as Proveedor from CompraTB as c inner join ProveedorTB as p\n"
                + "on c.Proveedor = p.IdProveedor\n"
                + "where c.IdCompra = ?";

        PreparedStatement preparedStatementProveedor = null;
        ResultSet rsEmpsProveedor = null;

        ArrayList<ProveedorTB> listProveedor = new ArrayList();

        try {
            DBUtil.dbConnect();

            preparedStatementProveedor = DBUtil.getConnection().prepareStatement(selectStmtProveedor);
            preparedStatementProveedor.setString(1, value);

            rsEmpsProveedor = preparedStatementProveedor.executeQuery();

            while (rsEmpsProveedor.next()) {
                ProveedorTB proveedorTB = new ProveedorTB();
                proveedorTB.setNumeroDocumento(rsEmpsProveedor.getString("NumeroDocumento"));
                proveedorTB.setRazonSocial(rsEmpsProveedor.getString("Proveedor"));
                listProveedor.add(proveedorTB);
            }

        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);

        } finally {
            try {
                if (preparedStatementProveedor != null) {
                    preparedStatementProveedor.close();
                }
                if (rsEmpsProveedor != null) {
                    rsEmpsProveedor.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return listProveedor;
    }

    public static ArrayList<PersonaTB> ListCompraRepresentante(String value) {

        String selectStmtRepresentante = "select p.ApellidoPaterno,p.ApellidoMaterno,p.PrimerNombre,P.SegundoNombre from CompraTB as c inner join PersonaTB as p\n"
                + "on c.Representante = p.IdPersona\n"
                + "where c.IdCompra = ? ";

        PreparedStatement preparedStatementRepresentante = null;
        ResultSet rsEmpsRepresentante = null;

        ArrayList<PersonaTB> listRepresentante = new ArrayList();

        try {
            DBUtil.dbConnect();
            preparedStatementRepresentante = DBUtil.getConnection().prepareStatement(selectStmtRepresentante);
            preparedStatementRepresentante.setString(1, value);

            rsEmpsRepresentante = preparedStatementRepresentante.executeQuery();

            while (rsEmpsRepresentante.next()) {
                PersonaTB personaTB = new PersonaTB();
                personaTB.setApellidoPaterno(rsEmpsRepresentante.getString("ApellidoPaterno"));
                personaTB.setApellidoMaterno(rsEmpsRepresentante.getString("ApellidoMaterno"));
                personaTB.setPrimerNombre(rsEmpsRepresentante.getString("PrimerNombre"));
                personaTB.setSegundoNombre(rsEmpsRepresentante.getString("SegundoNombre"));
                listRepresentante.add(personaTB);
            }

        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);

        } finally {
            try {
                if (preparedStatementRepresentante != null) {
                    preparedStatementRepresentante.close();
                }

                if (rsEmpsRepresentante != null) {
                    rsEmpsRepresentante.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return listRepresentante;
    }

    public static ObservableList<ArticuloTB> ListDetalleCompra(String value) {
        String selectStmt = "select a.Clave,a.NombreMarca, d.Cantidad,d.PrecioCompra,d.Importe from DetalleCompraTB as d inner join ArticuloTB as a\n"
                + "on d.IdArticulo = a.IdArticulo\n"
                + "where IdCompra = ? ";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<ArticuloTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setId(rsEmps.getRow());
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setNombre(rsEmps.getString("NombreMarca"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setPrecioCompra(rsEmps.getDouble("PrecioCompra"));
                articuloTB.setImporte(rsEmps.getDouble("Importe"));
                empList.add(articuloTB);
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