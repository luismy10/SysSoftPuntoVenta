package model;

import controller.Session;
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

    public static String CrudCompra(CompraTB compraTB, TableView<ArticuloTB> tableView, ObservableList<LoteTB> loteTBs, PagoProveedoresTB pagoProveedoresTB) {

        CallableStatement codigo_compra = null;
        PreparedStatement compra = null;
        PreparedStatement detalle_compra = null;
        PreparedStatement articulo_update = null;
        PreparedStatement pago_Proveedores = null;
        PreparedStatement movimiento_caja = null;
//        PreparedStatement preparedHistorialArticulo = null;
        PreparedStatement lote_compra = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            codigo_compra = DBUtil.getConnection().prepareCall("{? = call Fc_Compra_Codigo_Alfanumerico()}");
            codigo_compra.registerOutParameter(1, java.sql.Types.VARCHAR);
            codigo_compra.execute();
            String id_compra = codigo_compra.getString(1);

            compra = DBUtil.getConnection().prepareStatement("INSERT INTO "
                    + "CompraTB("
                    + "IdCompra,"
                    + "Proveedor,"
                    + "Comprobante,"
                    + "Numeracion,"
                    + "TipoMoneda,"
                    + "FechaCompra,"
                    + "SubTotal,"
                    + "Descuento,"
                    + "Total,"
                    + "Observaciones,"
                    + "Notas,"
                    + "TipoCompra,"
                    + "EstadoCompra) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            movimiento_caja = DBUtil.getConnection().prepareStatement("INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?)");

            detalle_compra = DBUtil.getConnection().prepareStatement("INSERT INTO "
                    + "DetalleCompraTB("
                    + "IdCompra,"
                    + "IdArticulo,"
                    + "Cantidad,"
                    + "PrecioCompra,"
                    + "Descuento,"
                    + "PrecioVenta1,"
                    + "Margen1,"
                    + "Utilidad1,"
                    + "IdImpuesto,"
                    + "NombreImpuesto,"
                    + "ValorImpuesto,"
                    + "ImpuestoSumado,"
                    + "Importe,"
                    + "Descripcion)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            articulo_update = DBUtil.getConnection().prepareStatement("UPDATE "
                    + "ArticuloTB SET "
                    + "Cantidad = Cantidad + ?,"
                    + "PrecioCompra = ?,"
                    + "PrecioVentaGeneral = ?, "
                    + "PrecioMargenGeneral = ?, "
                    + "PrecioUtilidadGeneral = ?, "
                    + "Impuesto = ? "
                    + "WHERE IdArticulo = ?");

            pago_Proveedores = DBUtil.getConnection().prepareStatement("INSERT INTO PagoProveedoresTB(MontoTotal,MontoActual,CuotaTotal,CuotaActual,ValorCuota,Plazos,FechaInicial,FechaActual,FechaFinal,Observacion,Estado,IdProveedor,IdCompra) "
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

//           preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("INSERT INTO HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
//                    + "VALUES(?,GETDATE(),?,?,?,?,?)");
//
            lote_compra = DBUtil.getConnection().prepareStatement("INSERT INTO LoteTB(NumeroLote,FechaCaducidad,ExistenciaInicial,ExistenciaActual,IdArticulo,IdCompra) "
                    + "VALUES(?,?,?,?,?,?)");

            compra.setString(1, id_compra);
            compra.setString(2, compraTB.getProveedor());
            compra.setInt(3, compraTB.getComprobante());
            compra.setString(4, compraTB.getNumeracion().toUpperCase());
            compra.setInt(5, compraTB.getTipoMoneda());
            compra.setTimestamp(6, compraTB.getFechaRegistro());
            compra.setDouble(7, compraTB.getSubTotal());
            compra.setDouble(8, compraTB.getDescuento());
            compra.setDouble(9, compraTB.getTotal().get());
            compra.setString(10, compraTB.getObservaciones());
            compra.setString(11, compraTB.getNotas());
            compra.setInt(12, compraTB.getTipo());
            compra.setInt(13, compraTB.getEstado());
            compra.addBatch();

            movimiento_caja.setInt(1, Session.CAJA_ID);
            movimiento_caja.setString(2, Session.USER_ID);
            movimiento_caja.setTimestamp(3, compraTB.getFechaRegistro());
            movimiento_caja.setString(4, compraTB.getTipo() == 2 ? "Compra al crédito" : "Compra al contado");
            movimiento_caja.setString(5, compraTB.getTipo() == 2 ? "COMCRE" : "COM");
            movimiento_caja.setDouble(6, 0);
            movimiento_caja.setDouble(7, compraTB.getTotal().get());
            movimiento_caja.setDouble(8, compraTB.getTotal().get() - 0);
            movimiento_caja.addBatch();

            for (int i = 0; i < tableView.getItems().size(); i++) {
                detalle_compra.setString(1, id_compra);
                detalle_compra.setString(2, tableView.getItems().get(i).getIdArticulo());
                detalle_compra.setDouble(3, tableView.getItems().get(i).getCantidad());
                detalle_compra.setDouble(4, tableView.getItems().get(i).getCostoCompraReal());
                detalle_compra.setDouble(5, tableView.getItems().get(i).getDescuento());

                detalle_compra.setDouble(6, tableView.getItems().get(i).getPrecioVentaGeneral());
                detalle_compra.setShort(7, tableView.getItems().get(i).getPrecioMargenGeneral());
                detalle_compra.setDouble(8, tableView.getItems().get(i).getPrecioUtilidadGeneral());

                detalle_compra.setInt(9, tableView.getItems().get(i).getImpuestoArticulo());
                detalle_compra.setString(10, tableView.getItems().get(i).getImpuestoArticuloName());
                detalle_compra.setDouble(11, tableView.getItems().get(i).getImpuestoValor());
                detalle_compra.setDouble(12, tableView.getItems().get(i).getImpuestoSumado());
                detalle_compra.setDouble(13, tableView.getItems().get(i).getTotalImporte());
                detalle_compra.setString(14, tableView.getItems().get(i).getDescripcion());
                detalle_compra.addBatch();

                articulo_update.setDouble(1, tableView.getItems().get(i).getCantidad());
                articulo_update.setDouble(2, tableView.getItems().get(i).getCostoCompraReal());
                articulo_update.setDouble(3, tableView.getItems().get(i).getPrecioVentaGeneral());
                articulo_update.setInt(4, tableView.getItems().get(i).getPrecioMargenGeneral());
                articulo_update.setDouble(5, tableView.getItems().get(i).getPrecioUtilidadGeneral());
                articulo_update.setInt(6, tableView.getItems().get(i).getImpuestoArticulo());
                articulo_update.setString(7, tableView.getItems().get(i).getIdArticulo());
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

            if (compraTB.getTipo() == 2) {
                pago_Proveedores.setDouble(1, pagoProveedoresTB.getMontoTotal());
                pago_Proveedores.setDouble(2, pagoProveedoresTB.getMontoActual());
                pago_Proveedores.setInt(3, pagoProveedoresTB.getCuotaTotal());
                pago_Proveedores.setInt(4, pagoProveedoresTB.getCuotaActual());
                pago_Proveedores.setDouble(5, pagoProveedoresTB.getValorCuota());
                pago_Proveedores.setString(6, pagoProveedoresTB.getPlazos());
                pago_Proveedores.setTimestamp(7, pagoProveedoresTB.getFechaInicial());
                pago_Proveedores.setTimestamp(8, pagoProveedoresTB.getFechaActual());
                pago_Proveedores.setTimestamp(9, pagoProveedoresTB.getFechaFinal());
                pago_Proveedores.setString(10, pagoProveedoresTB.getObservacion());
                pago_Proveedores.setString(11, pagoProveedoresTB.getEstado());
                pago_Proveedores.setString(12, pagoProveedoresTB.getIdProveedor());
                pago_Proveedores.setString(13, id_compra);
                pago_Proveedores.addBatch();
            }

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
            movimiento_caja.executeBatch();
            detalle_compra.executeBatch();
            articulo_update.executeBatch();
            pago_Proveedores.executeBatch();
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
                if (movimiento_caja != null) {
                    movimiento_caja.close();
                }
//              
//                if (preparedHistorialArticulo != null) {
//                    preparedHistorialArticulo.close();
//                }
                if (lote_compra != null) {
                    lote_compra.close();
                }
                if (pago_Proveedores != null) {
                    pago_Proveedores.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }

    public static ObservableList<CompraTB> ListComprasRealizadas(short opcion, String value, String fechaInicial, String fechaFinal, int estadoCompra) {
        String selectStmt = "{call Sp_Listar_Compras(?,?,?,?,?)}";
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
            preparedStatement.setInt(5, estadoCompra);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                CompraTB compraTB = new CompraTB();
                compraTB.setId(rsEmps.getInt("Filas"));
                compraTB.setIdCompra(rsEmps.getString("IdCompra"));
                compraTB.setFechaCompra(rsEmps.getTimestamp("Fecha").toLocalDateTime());
                compraTB.setNumeracion(rsEmps.getString("Numeracion"));
                compraTB.setProveedorTB(new ProveedorTB(rsEmps.getString("NumeroDocumento"), rsEmps.getString("RazonSocial")));
                compraTB.setTipoName(rsEmps.getString("Tipo"));
                compraTB.setEstadoName(rsEmps.getString("Estado"));
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
                articuloTB.setCostoCompra(rsEmps.getDouble("PrecioCompra"));
                articuloTB.setDescuento(rsEmps.getDouble("Descuento"));
                articuloTB.setImpuestoArticulo(rsEmps.getInt("IdImpuesto"));
                articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getCostoCompra());
                double porcentajeDecimal = articuloTB.getDescuento() / 100.00;
                double porcentajeRestante = articuloTB.getCostoCompra() * porcentajeDecimal;
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
        ArrayList<Object> objects = new ArrayList<>();

        try {
            DBUtil.dbConnect();

            statementCompra = DBUtil.getConnection().prepareStatement("{call Sp_Obtener_Compra_ById(?)}");
            statementCompra.setString(1, value);
            ResultSet resultSet = statementCompra.executeQuery();
            CompraTB compraTB = null;
            if (resultSet.next()) {
                compraTB = new CompraTB();
                compraTB.setFechaCompra(resultSet.getTimestamp("Fecha").toLocalDateTime());
                compraTB.setComprobanteName(resultSet.getString("Nombre"));
                compraTB.setNumeracion(resultSet.getString("Numeracion"));
                compraTB.setTipoMonedaName(resultSet.getString("Simbolo"));
                compraTB.setTipoName(resultSet.getString("Tipo"));
                compraTB.setEstadoName(resultSet.getString("Estado"));
                compraTB.setTotal(resultSet.getDouble("Total"));
                compraTB.setObservaciones(resultSet.getString("Observaciones"));
                compraTB.setNotas(resultSet.getString("Notas"));
                objects.add(compraTB);
            } else {
                objects.add(compraTB);
            }
            statementProveedor = DBUtil.getConnection().prepareStatement("select p.IdProveedor,p.NumeroDocumento,p.RazonSocial as Proveedor,p.Telefono,p.Celular,p.Direccion \n"
                    + "from CompraTB as c inner join ProveedorTB as p\n"
                    + "on c.Proveedor = p.IdProveedor\n"
                    + "where c.IdCompra = ?");
            statementProveedor.setString(1, value);
            ResultSet resultSetProveedor = statementProveedor.executeQuery();
            ProveedorTB proveedorTB = null;
            if (resultSetProveedor.next()) {
                proveedorTB = new ProveedorTB();

                proveedorTB.setIdProveedor(resultSetProveedor.getString("IdProveedor"));
                proveedorTB.setNumeroDocumento(resultSetProveedor.getString("NumeroDocumento"));
                proveedorTB.setRazonSocial(resultSetProveedor.getString("Proveedor"));
                proveedorTB.setTelefono(resultSetProveedor.getString("Telefono"));
                proveedorTB.setCelular(resultSetProveedor.getString("Celular"));
                proveedorTB.setDireccion(resultSetProveedor.getString("Direccion"));
                objects.add(proveedorTB);
            } else {
                objects.add(proveedorTB);
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

                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return objects;
    }
}
