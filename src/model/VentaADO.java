package model;

import controller.Session;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class VentaADO {

    public static String CrudVenta(VentaTB ventaTB, TableView<ArticuloTB> tvList, String tipo_comprobante) {

        CallableStatement serie_numeracion = null;
        PreparedStatement venta = null;
        PreparedStatement comprobante = null;
        CallableStatement codigo_venta = null;
        PreparedStatement detalle_venta = null;
        PreparedStatement articulo_update = null;
        PreparedStatement preparedHistorialArticulo = null;

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
                    + "           ,[Serie]\n"
                    + "           ,[Numeracion]\n"
                    + "           ,[FechaVenta]\n"
                    + "           ,[SubTotal]\n"
                    + "           ,[Gravada]\n"
                    + "           ,[Descuento]\n"
                    + "           ,[Igv]\n"
                    + "           ,[Total]"
                    + "           ,[Estado]"
                    + "           ,[Observaciones])\n"
                    + "     VALUES\n"
                    + "           (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            if (tipo_comprobante.equalsIgnoreCase("boleta")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_b,Numeracion,FechaRegistro)VALUES(?,?,?)");
            } else if (tipo_comprobante.equalsIgnoreCase("factura")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_f,Numeracion,FechaRegistro)VALUES(?,?,?)");
            } else if (tipo_comprobante.equalsIgnoreCase("ticket")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_t,Numeracion,FechaRegistro)VALUES(?,?,?)");
            }

            detalle_venta = DBUtil.getConnection().prepareStatement("INSERT INTO DetalleVentaTB(IdVenta,IdArticulo,Cantidad,PrecioUnitario,Descuento,Importe)VALUES(?,?,?,?,?,?)");

            articulo_update = DBUtil.getConnection().prepareStatement("UPDATE ArticuloTB SET Cantidad = Cantidad - ?,CantidadGranel = CantidadGranel - ? WHERE IdArticulo = ?");

            preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("INSERT INTO HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
                    + "VALUES(?,GETDATE(),?,?,?,?,?)");

            venta.setString(1, id_venta);
            venta.setString(2, ventaTB.getCliente());
            venta.setString(3, ventaTB.getVendedor());
            venta.setInt(4, ventaTB.getComprobante());
            venta.setString(5, id_comprabante[0]);
            venta.setString(6, id_comprabante[1]);
            venta.setTimestamp(7, ventaTB.getFechaVenta());
            venta.setDouble(8, ventaTB.getSubTotal());
            venta.setDouble(9, ventaTB.getGravada());
            venta.setDouble(10, ventaTB.getDescuento());
            venta.setDouble(11, ventaTB.getIgv());
            venta.setDouble(12, ventaTB.getTotal());
            venta.setString(13, ventaTB.getEstado());
            venta.setString(14, ventaTB.getObservaciones());
            venta.addBatch();

            System.out.println(tipo_comprobante);
            System.out.println(id_comprabante[0]);
            System.out.println(id_comprabante[1]);

            comprobante.setString(1, id_comprabante[0]);
            comprobante.setString(2, id_comprabante[1]);
            comprobante.setTimestamp(3, ventaTB.getFechaVenta());
            comprobante.addBatch();

            for (int i = 0; i < tvList.getItems().size(); i++) {
                detalle_venta.setString(1, id_venta);
                detalle_venta.setString(2, tvList.getItems().get(i).getIdArticulo());
                detalle_venta.setDouble(3, tvList.getItems().get(i).getCantidad());
                detalle_venta.setDouble(4, tvList.getItems().get(i).getPrecioVenta());
                detalle_venta.setDouble(5, tvList.getItems().get(i).getDescuento().get());
                detalle_venta.setDouble(6, tvList.getItems().get(i).getImporte().get());
                detalle_venta.addBatch();

                if (tvList.getItems().get(i).isInventario()) {
                    articulo_update.setDouble(1, tvList.getItems().get(i).getCantidad());
                    articulo_update.setDouble(2, tvList.getItems().get(i).getImporte().get());
                    articulo_update.setString(3, tvList.getItems().get(i).getIdArticulo());
                    articulo_update.addBatch();

                    preparedHistorialArticulo.setString(1, tvList.getItems().get(i).getIdArticulo());
                    preparedHistorialArticulo.setString(2, "Venta");
                    preparedHistorialArticulo.setDouble(3, 0);
                    preparedHistorialArticulo.setDouble(4, tvList.getItems().get(i).getUnidadVenta() == 1
                            ? tvList.getItems().get(i).getCantidad()
                            : tvList.getItems().get(i).getImporte().get()
                    );
                    preparedHistorialArticulo.setDouble(5, 0);
                    preparedHistorialArticulo.setString(6, Session.USER_ID);

                    preparedHistorialArticulo.addBatch();
                } else {
                    preparedHistorialArticulo.setString(1, tvList.getItems().get(i).getIdArticulo());
                    preparedHistorialArticulo.setString(2, "Venta");
                    preparedHistorialArticulo.setDouble(3, 0);
                    preparedHistorialArticulo.setDouble(4, tvList.getItems().get(i).getUnidadVenta() == 1
                            ? tvList.getItems().get(i).getCantidad()
                            : tvList.getItems().get(i).getImporte().get()
                    );
                    preparedHistorialArticulo.setDouble(5, 0);
                    preparedHistorialArticulo.setString(6, Session.USER_ID);

                    preparedHistorialArticulo.addBatch();
                }

            }

            venta.executeBatch();
            comprobante.executeBatch();
            detalle_venta.executeBatch();
            articulo_update.executeBatch();
            preparedHistorialArticulo.executeBatch();
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

                if (articulo_update != null) {
                    articulo_update.close();
                }

                if (codigo_venta != null) {
                    codigo_venta.close();
                }
                if (preparedHistorialArticulo != null) {
                    preparedHistorialArticulo.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }

    public static ObservableList<VentaTB> ListVentas(String value) {
        String selectStmt = "{call Sp_Listar_Ventas(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<VentaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                VentaTB ventaTB = new VentaTB();
                ventaTB.setId(rsEmps.getInt("Filas"));
                ventaTB.setIdVenta(rsEmps.getString("IdVenta"));
                ventaTB.setFechaRegistro(rsEmps.getTimestamp("FechaVenta").toLocalDateTime());
                ventaTB.setCliente(rsEmps.getString("Cliente"));
                ventaTB.setComprobanteName(rsEmps.getString("Comprobante"));
                ventaTB.setSerie(rsEmps.getString("Serie"));
                ventaTB.setNumeracion(rsEmps.getString("Numeracion"));
                ventaTB.setEstado(rsEmps.getString("Estado"));
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

    public static ObservableList<VentaTB> ListVentasByDate(String fechaInicial, String fechaFinal) {
        String selectStmt = "{call Sp_Listar_Ventas_By_Date(?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<VentaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, fechaInicial);
            preparedStatement.setString(2, fechaFinal);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                VentaTB ventaTB = new VentaTB();
                ventaTB.setId(rsEmps.getInt("Filas"));
                ventaTB.setIdVenta(rsEmps.getString("IdVenta"));
                ventaTB.setFechaRegistro(rsEmps.getTimestamp("FechaVenta").toLocalDateTime());
                ventaTB.setCliente(rsEmps.getString("Cliente"));
                ventaTB.setComprobanteName(rsEmps.getString("Comprobante"));
                ventaTB.setSerie(rsEmps.getString("Serie"));
                ventaTB.setNumeracion(rsEmps.getString("Numeracion"));
                ventaTB.setEstado(rsEmps.getString("Estado"));
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

    public static ObservableList<DetalleVentaTB> ListVentasDetalle(String value) {
        String selectStmt = "{call Sp_Listar_Ventas_Detalle_By_Id(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<DetalleVentaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                DetalleVentaTB detalleVentaTB = new DetalleVentaTB();
                detalleVentaTB.setId(rsEmps.getInt("Filas"));
                detalleVentaTB.setCantidad(rsEmps.getDouble("Cantidad"));
                detalleVentaTB.setPrecioUnitario(rsEmps.getDouble("PrecioUnitario"));
                detalleVentaTB.setDescuento(rsEmps.getDouble("Descuento"));
                detalleVentaTB.setImporte(rsEmps.getDouble("Importe"));
                detalleVentaTB.setArticuloTB(new ArticuloTB(rsEmps.getString("IdArticulo"), rsEmps.getString("NombreMarca"), rsEmps.getInt("UnidadVenta")));
                empList.add(detalleVentaTB);
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

    public static String CancelTheSale(String idVenta, TableView<DetalleVentaTB> tvList) {

        PreparedStatement statementVenta = null;
        PreparedStatement statementArticulo = null;
        PreparedStatement statementDetalleVenta = null;
        PreparedStatement statementValidar = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);

            statementValidar = DBUtil.getConnection().prepareStatement("select * from VentaTB where IdVenta = ? and Estado = ?");
            statementValidar.setString(1, idVenta);
            statementValidar.setString(2, "Devuelto");
            if (statementValidar.executeQuery().next()) {
                DBUtil.getConnection().rollback();
                return "scrambled";
            } else {
                statementVenta = DBUtil.getConnection().prepareStatement("update VentaTB set Estado = ? where IdVenta = ?");
                statementVenta.setString(1, "Devuelto");
                statementVenta.setString(2, idVenta);
                statementVenta.addBatch();

                statementDetalleVenta = DBUtil.getConnection().prepareStatement("update DetalleVentaTB set Importe = ? where IdVenta = ? and IdArticulo = ?");
                statementArticulo = DBUtil.getConnection().prepareStatement("update ArticuloTB set Cantidad = Cantidad + ? where IdArticulo = ?");
                for (int i = 0; i < tvList.getItems().size(); i++) {
                    statementDetalleVenta.setDouble(1, -tvList.getItems().get(i).getImporte());
                    statementDetalleVenta.setString(2, idVenta);
                    statementDetalleVenta.setString(3, tvList.getItems().get(i).getArticuloTB().getIdArticulo());
                    statementDetalleVenta.addBatch();

                    statementArticulo.setDouble(1, tvList.getItems().get(i).getCantidad());
                    statementArticulo.setString(2, tvList.getItems().get(i).getArticuloTB().getIdArticulo());
                    statementArticulo.addBatch();
                }

                statementVenta.executeBatch();
                statementDetalleVenta.executeBatch();
                statementArticulo.executeBatch();
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
                if (statementDetalleVenta != null) {
                    statementDetalleVenta.close();
                }
                if (statementArticulo != null) {
                    statementArticulo.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {

            }
        }
    }

    public static EmpleadoTB ListVentaDetalle(String value) {
        PreparedStatement statementVendedor = null;
        EmpleadoTB empleadoTB = null;
        try {
            DBUtil.dbConnect();
            statementVendedor = DBUtil.getConnection().prepareStatement("select e.Apellidos,e.Nombres \n"
                    + "from VentaTB as v inner join EmpleadoTB as e \n"
                    + "on v.Vendedor = e.IdEmpleado\n"
                    + "where v.IdVenta = ?");
            statementVendedor.setString(1, value);
            ResultSet resultSet = statementVendedor.executeQuery();
            if (resultSet.next()) {
                empleadoTB = new EmpleadoTB();
                empleadoTB.setApellidos(resultSet.getString("Apellidos"));
                empleadoTB.setNombres(resultSet.getString("Nombres"));
            }
        } catch (SQLException ex) {

        } finally {
            try {
                if (statementVendedor != null) {
                    statementVendedor.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }

        }
        return empleadoTB;
    }

}
