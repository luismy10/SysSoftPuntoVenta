package model;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class CompraADO {

    public static String CrudCompra(CompraTB compraTB, TableView<ArticuloTB> tableView, ObservableList<LoteTB> loteTBs) {

        CallableStatement codigo_compra = null;
        PreparedStatement compra = null;
        PreparedStatement detalle_compra = null;
        PreparedStatement articulo_update = null;
//        PreparedStatement preparedHistorialArticulo = null;
        PreparedStatement lote_compra = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            codigo_compra = DBUtil.getConnection().prepareCall("{? = call Fc_Compra_Codigo_Alfanumerico()}");
            codigo_compra.registerOutParameter(1, java.sql.Types.VARCHAR);
            codigo_compra.execute();
            String id_compra = codigo_compra.getString(1);

            compra = DBUtil.getConnection().prepareStatement("INSERT INTO CompraTB(IdCompra,Proveedor,Representante,Comprobante,Numeracion,TipoMoneda,FechaCompra,SubTotal,Descuento,Total,Observaciones,Notas) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

            detalle_compra = DBUtil.getConnection().prepareStatement("INSERT INTO DetalleCompraTB(IdCompra,IdArticulo,Cantidad,PrecioCompra,Descuento,PrecioVenta1,Margen1,Utilidad1,PrecioVenta2,Margen2,Utilidad2,PrecioVenta3,Margen3,Utilidad3,IdImpuesto,NombreImpuesto,ValorImpuesto,ImpuestoSumado,Importe)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            articulo_update = DBUtil.getConnection().prepareStatement("UPDATE ArticuloTB SET Cantidad = Cantidad + ?, PrecioCompra = ?, PrecioVenta1 = ?, Margen1 = ?, Utilidad1 = ?, PrecioVenta2 = ?, Margen2 = ?, Utilidad2 = ?, PrecioVenta3 = ?, Margen3 = ?, Utilidad3 = ?, Impuesto = ? WHERE IdArticulo = ?");

//           preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("INSERT INTO HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
//                    + "VALUES(?,GETDATE(),?,?,?,?,?)");
//
            lote_compra = DBUtil.getConnection().prepareStatement("INSERT INTO LoteTB(NumeroLote,FechaCaducidad,ExistenciaInicial,ExistenciaActual,IdArticulo,IdCompra) "
                    + "VALUES(?,?,?,?,?,?)");

            compra.setString(1, id_compra);
            compra.setString(2, compraTB.getProveedor());
            compra.setString(3, compraTB.getRepresentante());
            compra.setInt(4, compraTB.getComprobante());
            compra.setString(5, compraTB.getNumeracion().toUpperCase());
            compra.setInt(6, compraTB.getTipoMoneda());
            compra.setTimestamp(7, compraTB.getFechaRegistro());
            compra.setDouble(8, compraTB.getSubTotal());
            compra.setDouble(9, compraTB.getDescuento());
            compra.setDouble(10, compraTB.getTotal().get());
            compra.setString(11, compraTB.getObservaciones());
            compra.setString(12, compraTB.getNotas());
            compra.addBatch();

            for (int i = 0; i < tableView.getItems().size(); i++) {
                detalle_compra.setString(1, id_compra);
                detalle_compra.setString(2, tableView.getItems().get(i).getIdArticulo());
                detalle_compra.setDouble(3, tableView.getItems().get(i).getCantidad());
                detalle_compra.setDouble(4, tableView.getItems().get(i).getPrecioCompraReal());
                detalle_compra.setDouble(5, tableView.getItems().get(i).getDescuento());
                
                detalle_compra.setDouble(6, tableView.getItems().get(i).getPrecioVenta());
                detalle_compra.setShort(7, tableView.getItems().get(i).getMargen());
                detalle_compra.setDouble(8, tableView.getItems().get(i).getUtilidad());
                detalle_compra.setDouble(9, tableView.getItems().get(i).getPrecioVenta2());
                detalle_compra.setShort(10, tableView.getItems().get(i).getMargen2());
                detalle_compra.setDouble(11, tableView.getItems().get(i).getUtilidad2());
                detalle_compra.setDouble(12, tableView.getItems().get(i).getPrecioVenta3());
                detalle_compra.setShort(13, tableView.getItems().get(i).getMargen3());
                detalle_compra.setDouble(14, tableView.getItems().get(i).getUtilidad3());
                
                detalle_compra.setInt(15, tableView.getItems().get(i).getImpuestoArticulo());
                detalle_compra.setString(16, tableView.getItems().get(i).getImpuestoArticuloName());
                detalle_compra.setDouble(17, tableView.getItems().get(i).getImpuestoValor());
                detalle_compra.setDouble(18, tableView.getItems().get(i).getImpuestoSumado());
                detalle_compra.setDouble(19, tableView.getItems().get(i).getTotalImporte());
                detalle_compra.addBatch();

                articulo_update.setDouble(1, tableView.getItems().get(i).getCantidad());
                articulo_update.setDouble(2, tableView.getItems().get(i).getPrecioCompraReal());
                
                articulo_update.setDouble(3, tableView.getItems().get(i).getPrecioVenta());
                articulo_update.setInt(4, tableView.getItems().get(i).getMargen());
                articulo_update.setDouble(5, tableView.getItems().get(i).getUtilidad());               
                articulo_update.setDouble(6, tableView.getItems().get(i).getPrecioVenta2());
                articulo_update.setInt(7, tableView.getItems().get(i).getMargen2());
                articulo_update.setDouble(8, tableView.getItems().get(i).getUtilidad2());
                articulo_update.setDouble(9, tableView.getItems().get(i).getPrecioVenta3());
                articulo_update.setInt(10, tableView.getItems().get(i).getMargen3());
                articulo_update.setDouble(11, tableView.getItems().get(i).getUtilidad3());
                
                articulo_update.setInt(12, tableView.getItems().get(i).getImpuestoArticulo());
                articulo_update.setString(13, tableView.getItems().get(i).getIdArticulo());
                articulo_update.addBatch();

//                preparedHistorialArticulo.setString(1, tableView.getItems().get(i).getIdArticulo());
//                preparedHistorialArticulo.setString(2, "Compra");
//                preparedHistorialArticulo.setDouble(3, tableView.getItems().get(i).getUnidadVenta() == 1
//                        ? tableView.getItems().get(i).getCantidad()
//                        : tableView.getItems().get(i).getCantidadGranel()
//                );
//                preparedHistorialArticulo.setDouble(4, 0);
//                preparedHistorialArticulo.setDouble(5, 0);
//                preparedHistorialArticulo.setString(6, Session.USER_ID);
//
//                preparedHistorialArticulo.addBatch();
            }
//
            for (int i = 0; i < loteTBs.size(); i++) {
                lote_compra.setString(1, loteTBs.get(i).getNumeroLote().equalsIgnoreCase("") ? id_compra + loteTBs.get(i).getIdArticulo() : loteTBs.get(i).getNumeroLote());
                lote_compra.setDate(2, Date.valueOf(loteTBs.get(i).getFechaCaducidad()));
                lote_compra.setDouble(3, loteTBs.get(i).getExistenciaInicial());
                lote_compra.setDouble(4, loteTBs.get(i).getExistenciaActual());
                lote_compra.setString(5, loteTBs.get(i).getIdArticulo());
                lote_compra.setString(6, id_compra);
                lote_compra.addBatch();
            }

            compra.executeBatch();
            detalle_compra.executeBatch();
            articulo_update.executeBatch();
//            preparedHistorialArticulo.executeBatch();
            lote_compra.executeBatch();
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
                if (codigo_compra != null) {
                    codigo_compra.close();
                }
                if (compra != null) {
                    compra.close();
                }
                if (detalle_compra != null) {
                    detalle_compra.close();
                }
                if (articulo_update != null) {
                    articulo_update.close();
                }
//              
//                if (preparedHistorialArticulo != null) {
//                    preparedHistorialArticulo.close();
//                }
                if (lote_compra != null) {
                    lote_compra.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }

    public static ObservableList<CompraTB> ListComprasRealizadas(short opcion, String value, String fechaInicial, String fechaFinal) {
        String selectStmt = "{call Sp_Listar_Compras(?,?,?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<CompraTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setShort(1, opcion);
            preparedStatement.setString(2, value);
            preparedStatement.setString(3, fechaInicial);
            preparedStatement.setString(4, fechaFinal);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                CompraTB compraTB = new CompraTB();
                compraTB.setId(rsEmps.getInt("Filas"));
                compraTB.setIdCompra(rsEmps.getString("IdCompra"));
                compraTB.setFechaCompra(rsEmps.getDate("Fecha").toLocalDate());
                compraTB.setNumeracion(rsEmps.getString("Numeracion"));
                compraTB.setProveedorTB(new ProveedorTB(rsEmps.getString("NumeroDocumento"), rsEmps.getString("RazonSocial")));
                compraTB.setTipoMonedaName(rsEmps.getString("Simbolo"));
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

    private static ObservableList<CompraTB> ListComprasRealizadasByFecha(String inicialDate, String finalDate) {
        String selectStmt = "{call Sp_Listar_Compras_By_Fecha(?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<CompraTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, inicialDate);
            preparedStatement.setString(2, finalDate);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                CompraTB compraTB = new CompraTB();
                compraTB.setId(rsEmps.getInt("Filas"));
                compraTB.setIdCompra(rsEmps.getString("IdCompra"));
                compraTB.setFechaCompra(rsEmps.getDate("Fecha").toLocalDate());
                compraTB.setNumeracion(rsEmps.getString("Numeracion"));
                compraTB.setProveedorTB(new ProveedorTB(rsEmps.getString("NumeroDocumento"), rsEmps.getString("RazonSocial")));
                compraTB.setTipoMonedaName(rsEmps.getString("Simbolo"));
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

    public static ObservableList<ArticuloTB> ListDetalleCompra(String value) {
        String selectStmt = "{call Sp_Listar_Detalle_Compra(?)}";
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
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setUnidadVenta(rsEmps.getInt("UnidadVenta"));
                articuloTB.setUnidadCompraName(rsEmps.getString("UnidadCompra"));
                articuloTB.setPrecioCompra(rsEmps.getDouble("PrecioCompra"));
                articuloTB.setDescuento(rsEmps.getDouble("Descuento"));
                articuloTB.setImpuestoArticulo(rsEmps.getInt("IdImpuesto"));
                articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioCompra());
                double porcentajeDecimal = articuloTB.getDescuento() / 100.00;
                double porcentajeRestante = articuloTB.getPrecioCompra() * porcentajeDecimal;
                articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());
                articuloTB.setImpuestoValor(rsEmps.getDouble("ValorImpuesto"));
                articuloTB.setImpuestoSumado(rsEmps.getDouble("ImpuestoSumado"));
                articuloTB.setSubImporteDescuento(articuloTB.getSubImporte() - articuloTB.getDescuentoSumado());
                articuloTB.setTotalImporte(rsEmps.getDouble("Importe"));
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

    public static ArrayList<Object> ListCompletaDetalleCompra(String value) {
        PreparedStatement statementCompra = null;
        PreparedStatement statementProveedor = null;
        PreparedStatement statementRepresentante = null;
        ArrayList<Object> objects = new ArrayList<>();

        try {
            DBUtil.dbConnect();
//            statementCompra = DBUtil.getConnection().prepareStatement("select CAST(FechaCompra as date) as Fecha,\n"
//                    + "dbo.Fc_Obtener_Nombre_Detalle(Comprobante,'0009') as Comprobante,\n"
//                    + "Numeracion,Total from CompraTB\n"
//                    + "where IdCompra = ? ");

            statementCompra = DBUtil.getConnection().prepareStatement("select CAST(c.FechaCompra as date) as Fecha, c.Comprobante, c.Numeracion, dbo.Fc_Obtener_Simbolo_Moneda(c.TipoMoneda) as Simbolo,c.Total,c.Observaciones,c.Notas,td.Nombre from CompraTB as c inner join TipoDocumentoTB as td on c.Comprobante=td.IdTipoDocumento where c.IdCompra = ?");
            statementCompra.setString(1, value);
            ResultSet resultSet = statementCompra.executeQuery();
            CompraTB compraTB = null;
            if (resultSet.next()) {
                compraTB = new CompraTB();
                compraTB.setFechaCompra(resultSet.getDate("Fecha").toLocalDate());
                compraTB.setComprobanteName(resultSet.getString("Nombre"));
                compraTB.setNumeracion(resultSet.getString("Numeracion"));
                compraTB.setTipoMonedaName(resultSet.getString("Simbolo"));
                compraTB.setTotal(resultSet.getDouble("Total"));
                compraTB.setObservaciones(resultSet.getString("Observaciones"));
                compraTB.setNotas(resultSet.getString("Notas"));
                objects.add(compraTB);
            } else {
                objects.add(compraTB);
            }
            statementProveedor = DBUtil.getConnection().prepareStatement("select p.NumeroDocumento,p.RazonSocial as Proveedor,p.Telefono,p.Celular,p.Direccion \n"
                    + "from CompraTB as c inner join ProveedorTB as p\n"
                    + "on c.Proveedor = p.IdProveedor\n"
                    + "where c.IdCompra = ?");
            statementProveedor.setString(1, value);
            ResultSet resultSetProveedor = statementProveedor.executeQuery();
            ProveedorTB proveedorTB = null;
            if (resultSetProveedor.next()) {
                proveedorTB = new ProveedorTB();
                proveedorTB.setNumeroDocumento(resultSetProveedor.getString("NumeroDocumento"));
                proveedorTB.setRazonSocial(resultSetProveedor.getString("Proveedor"));
                proveedorTB.setTelefono(resultSetProveedor.getString("Telefono"));
                proveedorTB.setCelular(resultSetProveedor.getString("Celular"));
                proveedorTB.setDireccion(resultSetProveedor.getString("Direccion"));
                objects.add(proveedorTB);
            } else {
                objects.add(proveedorTB);
            }

            statementRepresentante = DBUtil.getConnection().prepareStatement("select r.Apellidos,r.Nombres,r.Telefono,r.Celular\n"
                    + "from CompraTB as c inner join RepresentanteTB as r\n"
                    + "on c.Representante = r.IdRepresentante\n"
                    + "where c.IdCompra = ?");
            statementRepresentante.setString(1, value);
            ResultSet resultSetRepresentante = statementRepresentante.executeQuery();
            RepresentanteTB representanteTB = null;
            if (resultSetRepresentante.next()) {
                representanteTB = new RepresentanteTB();
                representanteTB.setApellidos(resultSetRepresentante.getString("Apellidos"));
                representanteTB.setNombres(resultSetRepresentante.getString("Nombres"));
                representanteTB.setTelefono(resultSetRepresentante.getString("Telefono"));
                representanteTB.setCelular(resultSetRepresentante.getString("Celular"));
                objects.add(representanteTB);
            } else {
                objects.add(representanteTB);
            }

        } catch (SQLException ex) {

        } finally {
            try {
                if (statementCompra != null) {
                    statementCompra.close();
                }
                if (statementProveedor != null) {
                    statementProveedor.close();
                }
                if (statementRepresentante != null) {
                    statementRepresentante.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return objects;
    }
}
