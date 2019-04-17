package model;

import controller.Session;
import controller.Tools;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class VentaADO {

    public static String CrudVenta(VentaTB ventaTB, TableView<ArticuloTB> tvList, String tipo_comprobante, CuentasClienteTB cuentasClienteTB) {

        CallableStatement serie_numeracion = null;
        PreparedStatement venta = null;
        PreparedStatement comprobante = null;
        CallableStatement codigo_venta = null;
        PreparedStatement detalle_venta = null;
        PreparedStatement articulo_update_unidad = null;
        PreparedStatement articulo_update_granel = null;
        PreparedStatement cuentas_cliente = null;
        PreparedStatement movimiento_caja = null;
//        PreparedStatement preparedHistorialArticulo = null;

        try {

            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            serie_numeracion = DBUtil.getConnection().prepareCall("{? = call Fc_Serie_Numero(?)}");
            serie_numeracion.registerOutParameter(1, java.sql.Types.VARCHAR);
            serie_numeracion.setString(2, tipo_comprobante);
            serie_numeracion.execute();
            String[] id_comprabante = serie_numeracion.getString(1).split("-");

            codigo_venta = DBUtil.getConnection().prepareCall("{? = call Fc_Venta_Codigo_Alfanumerico()}");
            codigo_venta.registerOutParameter(1, java.sql.Types.VARCHAR);
            codigo_venta.execute();

            String id_venta = codigo_venta.getString(1);

            venta = DBUtil.getConnection().prepareStatement("INSERT INTO VentaTB\n"
                    + "           ([IdVenta]\n"
                    + "           ,[Cliente]\n"
                    + "           ,[Vendedor]\n"
                    + "           ,[Comprobante]\n"
                    + "           ,[Moneda]\n"
                    + "           ,[Serie]\n"
                    + "           ,[Numeracion]\n"
                    + "           ,[FechaVenta]\n"
                    + "           ,[SubTotal]\n"
                    + "           ,[Descuento]\n"
                    + "           ,[Total]"
                    + "           ,[Tipo]"
                    + "           ,[Estado]"
                    + "           ,[Observaciones]"
                    + "           ,[Efectivo]"
                    + "           ,[Vuelto])\n"
                    + "     VALUES\n"
                    + "           (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            movimiento_caja = DBUtil.getConnection().prepareStatement("INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?)");

            cuentas_cliente = DBUtil.getConnection().prepareStatement("INSERT INTO CuentasClienteTB(IdVenta,IdCliente,Plazos,FechaVencimiento,MontoInicial)VALUES(?,?,?,?,?)");

            if (tipo_comprobante.equalsIgnoreCase("boleta")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_b,Numeracion,FechaRegistro)VALUES(?,?,?)");
            } else if (tipo_comprobante.equalsIgnoreCase("factura")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_f,Numeracion,FechaRegistro)VALUES(?,?,?)");
            } else if (tipo_comprobante.equalsIgnoreCase("ticket")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_t,Numeracion,FechaRegistro)VALUES(?,?,?)");
            }

            detalle_venta = DBUtil.getConnection().prepareStatement("INSERT INTO DetalleVentaTB\n"
                    + "(IdVenta\n"
                    + ",IdArticulo\n"
                    + ",Cantidad\n"
                    + ",CantidadGranel\n"
                    + ",CostoVenta\n"
                    + ",PrecioVenta\n"
                    + ",Descuento\n"
                    + ",IdImpuesto\n"
                    + ",NombreImpuesto\n"
                    + ",ValorImpuesto\n"
                    + ",ImpuestoSumado\n"
                    + ",Importe)\n"
                    + "VALUES\n"
                    + "(?,?,?,?,?,?,?,?,?,?,?,?)");

            articulo_update_unidad = DBUtil.getConnection().prepareStatement("UPDATE ArticuloTB SET Cantidad = Cantidad - ? WHERE IdArticulo = ?");
            articulo_update_granel = DBUtil.getConnection().prepareStatement("UPDATE ArticuloTB SET Cantidad = Cantidad - (? / PrecioVentaGeneral) WHERE IdArticulo = ?");
//
//            preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("INSERT INTO HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
//                    + "VALUES(?,GETDATE(),?,?,?,?,?)");

            venta.setString(1, id_venta);
            venta.setString(2, ventaTB.getCliente());
            venta.setString(3, ventaTB.getVendedor());
            venta.setInt(4, ventaTB.getComprobante());
            venta.setInt(5, ventaTB.getMoneda());
            venta.setString(6, id_comprabante[0]);
            venta.setString(7, id_comprabante[1]);
            venta.setTimestamp(8, Timestamp.valueOf(ventaTB.getFechaVenta()));
            venta.setDouble(9, ventaTB.getSubTotal());
            venta.setDouble(10, ventaTB.getDescuento());
            venta.setDouble(11, ventaTB.getTotal());
            venta.setInt(12, ventaTB.getTipo());
            venta.setInt(13, ventaTB.getEstado());
            venta.setString(14, ventaTB.getObservaciones());
            venta.setDouble(15, ventaTB.getEfectivo());
            venta.setDouble(16, ventaTB.getVuelto());
            venta.addBatch();

            movimiento_caja.setInt(1, Session.CAJA_ID);
            movimiento_caja.setString(2, ventaTB.getVendedor());
            movimiento_caja.setTimestamp(3, Timestamp.valueOf(ventaTB.getFechaVenta()));
            movimiento_caja.setString(4, ventaTB.getEstado() == 2 ? "Venta al crédito" : "Venta al contado");
            movimiento_caja.setString(5, ventaTB.getEstado() == 2 ? "VENCRE" : "VEN");
            movimiento_caja.setDouble(6, ventaTB.getTotal());
            movimiento_caja.setDouble(7, 0);
            movimiento_caja.setDouble(8, ventaTB.getTotal() - 0);
            movimiento_caja.addBatch();

            if (ventaTB.getEstado() == 2) {
                cuentas_cliente.setString(1, id_venta);
                cuentas_cliente.setString(2, ventaTB.getCliente());
                cuentas_cliente.setInt(3, cuentasClienteTB.getPlazos());
                cuentas_cliente.setTimestamp(4, Timestamp.valueOf(cuentasClienteTB.getFechaVencimiento()));
                cuentas_cliente.setDouble(5, ventaTB.getTotal());
                cuentas_cliente.addBatch();
            }

            comprobante.setString(1, id_comprabante[0]);
            comprobante.setString(2, id_comprabante[1]);
            comprobante.setTimestamp(3, Timestamp.valueOf(ventaTB.getFechaVenta()));
            comprobante.addBatch();

            for (int i = 0; i < tvList.getItems().size(); i++) {
                detalle_venta.setString(1, id_venta);
                detalle_venta.setString(2, tvList.getItems().get(i).getIdArticulo());
                detalle_venta.setDouble(3, tvList.getItems().get(i).getCantidad());
                detalle_venta.setDouble(4, tvList.getItems().get(i).getPrecioVentaAuxiliar() <= 0 ? 0
                        : tvList.getItems().get(i).getPrecioVentaGeneralReal() / tvList.getItems().get(i).getPrecioVentaAuxiliar()
                );
                detalle_venta.setDouble(5, tvList.getItems().get(i).getCostoCompra());
                detalle_venta.setDouble(6, tvList.getItems().get(i).getPrecioVentaGeneralReal());
                detalle_venta.setDouble(7, tvList.getItems().get(i).getDescuento());
                detalle_venta.setDouble(8, tvList.getItems().get(i).getImpuestoArticulo());
                detalle_venta.setString(9, tvList.getItems().get(i).getImpuestoArticuloName());
                detalle_venta.setDouble(10, tvList.getItems().get(i).getImpuestoValor());
                detalle_venta.setDouble(11, tvList.getItems().get(i).getImpuestoSumado());
                detalle_venta.setDouble(12, tvList.getItems().get(i).getTotalImporte());
                detalle_venta.addBatch();

                if (tvList.getItems().get(i).isValorInventario() && tvList.getItems().get(i).isInventario()) {
                    articulo_update_unidad.setDouble(1, tvList.getItems().get(i).getCantidad());
                    articulo_update_unidad.setString(2, tvList.getItems().get(i).getIdArticulo());
                    articulo_update_unidad.addBatch();

//                    preparedHistorialArticulo.setString(1, tvList.getItems().get(i).getIdArticulo());
//                    preparedHistorialArticulo.setString(2, "Venta");
//                    preparedHistorialArticulo.setDouble(3, 0);
//                    preparedHistorialArticulo.setDouble(4, tvList.getItems().get(i).getUnidadVenta() == 1
//                            ? tvList.getItems().get(i).getCantidad()
//                            : tvList.getItems().get(i).getTotalImporte()
//                    );
//                    preparedHistorialArticulo.setDouble(5, 0);
//                    preparedHistorialArticulo.setString(6, Session.USER_ID);
//
//                    preparedHistorialArticulo.addBatch();
                } else if (!tvList.getItems().get(i).isValorInventario() && tvList.getItems().get(i).isInventario()) {
                    articulo_update_granel.setDouble(1, tvList.getItems().get(i).getTotalImporte());
                    articulo_update_granel.setString(2, tvList.getItems().get(i).getIdArticulo());
                    articulo_update_granel.addBatch();
//                    preparedHistorialArticulo.setString(1, tvList.getItems().get(i).getIdArticulo());
//                    preparedHistorialArticulo.setString(2, "Venta");
//                    preparedHistorialArticulo.setDouble(3, 0);
//                    preparedHistorialArticulo.setDouble(4, tvList.getItems().get(i).getUnidadVenta() == 1
//                            ? tvList.getItems().get(i).getCantidad()
//                            : tvList.getItems().get(i).getTotalImporte()
//                    );
//                    preparedHistorialArticulo.setDouble(5, 0);
//                    preparedHistorialArticulo.setString(6, Session.USER_ID);
//
//                    preparedHistorialArticulo.addBatch();
                }

            }

            venta.executeBatch();
            cuentas_cliente.executeBatch();
            comprobante.executeBatch();
            detalle_venta.executeBatch();
            articulo_update_unidad.executeBatch();
            articulo_update_granel.executeBatch();
            movimiento_caja.executeBatch();
//            preparedHistorialArticulo.executeBatch();
            DBUtil.getConnection().commit();
            return "register/" + id_comprabante[0] + "-" + id_comprabante[1];
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
                return ex.getLocalizedMessage() + "/";
            } catch (SQLException ex1) {
                return ex1.getLocalizedMessage() + "/";
            }
        } finally {
            try {
                if (serie_numeracion != null) {
                    serie_numeracion.close();
                }
                if (venta != null) {
                    venta.close();
                }
                if (comprobante != null) {
                    comprobante.close();
                }

                if (detalle_venta != null) {
                    detalle_venta.close();
                }

                if (articulo_update_unidad != null) {
                    articulo_update_unidad.close();
                }
                if (articulo_update_granel != null) {
                    articulo_update_granel.close();
                }

                if (codigo_venta != null) {
                    codigo_venta.close();
                }

                if (cuentas_cliente != null) {
                    cuentas_cliente.close();
                }
                if (movimiento_caja != null) {
                    movimiento_caja.close();
                }
//                if (preparedHistorialArticulo != null) {
//                    preparedHistorialArticulo.close();
//                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }

    public static ObservableList<VentaTB> ListVentas(short opcion, String value, String fechaInicial, String fechaFinal, int comprobante, int estado, String usuario) {
        String selectStmt = "{call Sp_Listar_Ventas(?,?,?,?,?,?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<VentaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setShort(1, opcion);
            preparedStatement.setString(2, value);
            preparedStatement.setString(3, fechaInicial);
            preparedStatement.setString(4, fechaFinal);
            preparedStatement.setInt(5, comprobante);
            preparedStatement.setInt(6, estado);
            preparedStatement.setString(7, usuario);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                VentaTB ventaTB = new VentaTB();
                ventaTB.setId(rsEmps.getInt("Filas"));
                ventaTB.setIdVenta(rsEmps.getString("IdVenta"));
                ventaTB.setFechaVenta(rsEmps.getTimestamp("FechaVenta").toLocalDateTime());
                ventaTB.setCliente(rsEmps.getString("Cliente"));
                ventaTB.setComprobanteName(rsEmps.getString("Comprobante"));
                ventaTB.setSerie(rsEmps.getString("Serie"));
                ventaTB.setNumeracion(rsEmps.getString("Numeracion"));
                ventaTB.setTipoName(rsEmps.getString("Tipo"));
                ventaTB.setEstadoName(rsEmps.getString("Estado"));
                ventaTB.setMonedaName(rsEmps.getString("Simbolo"));
                ventaTB.setTotal(rsEmps.getDouble("Total"));
                ventaTB.setObservaciones(rsEmps.getString("Observaciones"));
                empList.add(ventaTB);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (rsEmps != null) {
                    rsEmps.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
        return empList;
    }

    public static ObservableList<ArticuloTB> ListVentasDetalle(String value) {
        String selectStmt = "{call Sp_Listar_Ventas_Detalle_By_Id(?)}";
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
                articuloTB.setIdArticulo(rsEmps.getString("IdArticulo"));
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
                articuloTB.setInventario(rsEmps.getBoolean("Inventario"));
                articuloTB.setValorInventario(rsEmps.getBoolean("ValorInventario"));
                articuloTB.setUnidadCompraName(rsEmps.getString("UnidadCompra"));
                articuloTB.setImpuestoArticulo(rsEmps.getInt("IdImpuesto"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setCantidadGranel(rsEmps.getDouble("CantidadGranel"));
                articuloTB.setPrecioVentaGeneral(rsEmps.getDouble("PrecioVenta"));
                articuloTB.setDescuento(rsEmps.getDouble("Descuento"));
                articuloTB.setSubImporte(articuloTB.getCantidad() * articuloTB.getPrecioVentaGeneral());
                double porcentajeDecimal = articuloTB.getDescuento() / 100.00;
                double porcentajeRestante = articuloTB.getPrecioVentaGeneral() * porcentajeDecimal;
                articuloTB.setDescuentoSumado(porcentajeRestante * articuloTB.getCantidad());
                articuloTB.setImpuestoValor(rsEmps.getDouble("ValorImpuesto"));
                articuloTB.setImpuestoSumado(rsEmps.getDouble("ImpuestoSumado"));
                articuloTB.setSubImporteDescuento(articuloTB.getSubImporte() - articuloTB.getDescuentoSumado());
                articuloTB.setTotalImporte(rsEmps.getDouble("Importe"));

                empList.add(articuloTB);
            }
        } catch (SQLException e) {
            System.out.println("ListVentasDetalle:La operación de selección de SQL ha fallado: " + e);
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

    public static String CancelTheSale(String idVenta, ObservableList<ArticuloTB> tvList,double total) {

        PreparedStatement statementVenta = null;
        PreparedStatement statementArticulo = null;
        PreparedStatement statementValidar = null;
        PreparedStatement movimiento_caja = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            statementValidar = DBUtil.getConnection().prepareStatement("select * from VentaTB where IdVenta = ? and Estado = ?");
            statementValidar.setString(1, idVenta);
            statementValidar.setInt(2, 3);
            if (statementValidar.executeQuery().next()) {
                DBUtil.getConnection().rollback();
                return "scrambled";
            } else {
                statementVenta = DBUtil.getConnection().prepareStatement("update VentaTB set Estado = ? where IdVenta = ?");
                statementVenta.setInt(1, 3);
                statementVenta.setString(2, idVenta);
                statementVenta.addBatch();

                statementArticulo = DBUtil.getConnection().prepareStatement("update ArticuloTB set Cantidad = Cantidad + ? where IdArticulo = ?");
                for (int i = 0; i < tvList.size(); i++) {
                    if (tvList.get(i).isInventario() && tvList.get(i).isValorInventario()) {
                        statementArticulo.setDouble(1, tvList.get(i).getCantidad());
                        statementArticulo.setString(2, tvList.get(i).getIdArticulo());
                        statementArticulo.addBatch();
                    } else if (tvList.get(i).isInventario() && !tvList.get(i).isValorInventario()) {
                        statementArticulo.setDouble(1, tvList.get(i).getCantidadGranel());
                        statementArticulo.setString(2, tvList.get(i).getIdArticulo());
                        statementArticulo.addBatch();
                    }

                }

                movimiento_caja = DBUtil.getConnection().prepareStatement("INSERT INTO MovimientoCajaTB(IdCaja,IdUsuario,FechaMovimiento,Comentario,Movimiento,Entrada,Salidas,Saldo)VALUES(?,?,?,?,?,?,?,?)");
                movimiento_caja.setInt(1, Session.CAJA_ID);
                movimiento_caja.setString(2, Session.USER_ID);
                movimiento_caja.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                movimiento_caja.setString(4, "Venta cancelada");
                movimiento_caja.setString(5, "VENCAN");
                movimiento_caja.setDouble(6, 0);
                movimiento_caja.setDouble(7, total);
                movimiento_caja.setDouble(8, total - 0);
                movimiento_caja.addBatch();

                statementVenta.executeBatch();
                statementArticulo.executeBatch();
                movimiento_caja.executeBatch();
                DBUtil.getConnection().commit();
                return "update";
            }

        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
            } catch (SQLException e) {
                return ex.getLocalizedMessage();
            }
            return ex.getLocalizedMessage();
        } finally {
            try {
                if (statementValidar != null) {
                    statementValidar.close();
                }
                if (statementVenta != null) {
                    statementVenta.close();
                }
                if (statementArticulo != null) {
                    statementArticulo.close();
                }
                if (movimiento_caja != null) {
                    movimiento_caja.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {

            }
        }
    }

    public static VentaTB GetVenta(String value) {
        PreparedStatement statementVendedor = null;
        VentaTB ventaTB = null;
        try {
            DBUtil.dbConnect();
            statementVendedor = DBUtil.getConnection().prepareStatement("{call Sp_Obtener_Venta_ById(?)}");
            statementVendedor.setString(1, value);
            try (ResultSet resultSet = statementVendedor.executeQuery()) {
                if (resultSet.next()) {
                    ventaTB = new VentaTB();
                    ventaTB.setFechaVenta(resultSet.getTimestamp("FechaVenta").toLocalDateTime());
                    ventaTB.setCliente(resultSet.getString("Apellidos") + " " + resultSet.getString("Nombres"));
                    ventaTB.setComprobanteName(resultSet.getString("Comprobante"));
                    ventaTB.setComproabanteNameImpresion(resultSet.getString("NombreImpresion"));
                    ventaTB.setSerie(resultSet.getString("Serie"));
                    ventaTB.setNumeracion(resultSet.getString("Numeracion"));
                    ventaTB.setObservaciones(resultSet.getString("Observaciones"));
                    ventaTB.setTipoName(resultSet.getString("Tipo"));
                    ventaTB.setEstadoName(resultSet.getString("Estado"));
                    ventaTB.setMonedaName(resultSet.getString("Simbolo"));
                    ventaTB.setEfectivo(resultSet.getDouble("Efectivo"));
                    ventaTB.setVuelto(resultSet.getDouble("Vuelto"));

                }
                statementVendedor.close();
            }
        } catch (SQLException ex) {

        } finally {
            try {
                if (statementVendedor != null) {
                    statementVendedor.close();
                }
            } catch (SQLException ex) {

            }
        }

        return ventaTB;
    }

    public static EmpleadoTB GetEmpleadoVenta(String value) {
        PreparedStatement statementVendedor = null;
        EmpleadoTB empleadoTB = null;
        try {
            DBUtil.dbConnect();
            statementVendedor = DBUtil.getConnection().prepareStatement("select e.Apellidos,e.Nombres \n"
                    + "from VentaTB as v inner join EmpleadoTB as e \n"
                    + "on v.Vendedor = e.IdEmpleado\n"
                    + "where v.IdVenta = ?");
            statementVendedor.setString(1, value);
            try (ResultSet resultSet = statementVendedor.executeQuery()) {
                if (resultSet.next()) {
                    empleadoTB = new EmpleadoTB();
                    empleadoTB.setApellidos(resultSet.getString("Apellidos"));
                    empleadoTB.setNombres(resultSet.getString("Nombres"));
                }
                statementVendedor.close();
            }
        } catch (SQLException ex) {

        } finally {
            try {
                if (statementVendedor != null) {
                    statementVendedor.close();
                }
            } catch (SQLException ex) {

            }
        }

        return empleadoTB;
    }

    public static ArrayList<VentaTB> GetReporteGenetalVentas(String fechaInicial, String fechaFinal, int tipoDocumento) {
        CallableStatement callableStatement = null;
        ResultSet resultSet = null;
        ArrayList<VentaTB> arrayList = new ArrayList<>();
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall("{call Sp_Reporte_General_Ventas(?,?,?)}");
            callableStatement.setString(1, fechaInicial);
            callableStatement.setString(2, fechaFinal);
            callableStatement.setInt(3, tipoDocumento);
            resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                VentaTB ventaTB = new VentaTB();
                ventaTB.setDocumentoReporte(resultSet.getString("Nombre"));
                ventaTB.setFechaVentaReporte(resultSet.getDate("FechaVenta").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
                ventaTB.setClienteReporte(resultSet.getString("Apellidos") + " " + resultSet.getString("Nombres"));
                ventaTB.setTotalReporte(resultSet.getString("Simbolo") + " " + Tools.roundingValue(resultSet.getDouble("Total"), 2));
                arrayList.add(ventaTB);
            }
        } catch (SQLException ex) {

        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }

        }
        return arrayList;
    }

}
