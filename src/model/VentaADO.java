package model;

import controller.Session;
import controller.Tools;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
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
                    + "           ,[Moneda]\n"
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
                    + "           (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            if (tipo_comprobante.equalsIgnoreCase("boleta")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_b,Numeracion,FechaRegistro)VALUES(?,?,?)");
            } else if (tipo_comprobante.equalsIgnoreCase("factura")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_f,Numeracion,FechaRegistro)VALUES(?,?,?)");
            } else if (tipo_comprobante.equalsIgnoreCase("ticket")) {
                comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(serie_t,Numeracion,FechaRegistro)VALUES(?,?,?)");
            }

            detalle_venta = DBUtil.getConnection().prepareStatement("INSERT INTO DetalleVentaTB(IdVenta,IdArticulo,Cantidad,PrecioUnitario,Descuento,Importe)VALUES(?,?,?,?,?,?)");

            articulo_update = DBUtil.getConnection().prepareStatement("UPDATE ArticuloTB SET Cantidad = Cantidad - ? WHERE IdArticulo = ?");

            preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("INSERT INTO HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
                    + "VALUES(?,GETDATE(),?,?,?,?,?)");

            venta.setString(1, id_venta);
            venta.setString(2, ventaTB.getCliente());
            venta.setString(3, ventaTB.getVendedor());
            venta.setInt(4, ventaTB.getComprobante());
            venta.setInt(5, ventaTB.getMoneda());
            venta.setString(6, id_comprabante[0]);
            venta.setString(7, id_comprabante[1]);
            venta.setTimestamp(8, ventaTB.getFechaVenta());
            venta.setDouble(9, ventaTB.getSubTotal());
            venta.setDouble(10, ventaTB.getGravada());
            venta.setDouble(11, ventaTB.getDescuento());
            venta.setDouble(12, ventaTB.getIgv());
            venta.setDouble(13, ventaTB.getTotal());
            venta.setInt(14, ventaTB.getEstado());
            venta.setString(15, ventaTB.getObservaciones());
            venta.addBatch();

            comprobante.setString(1, id_comprabante[0]);
            comprobante.setString(2, id_comprabante[1]);
            comprobante.setTimestamp(3, ventaTB.getFechaVenta());
            comprobante.addBatch();

            for (int i = 0; i < tvList.getItems().size(); i++) {
                detalle_venta.setString(1, id_venta);
                detalle_venta.setString(2, tvList.getItems().get(i).getIdArticulo());
                detalle_venta.setDouble(3, tvList.getItems().get(i).getCantidad());
                detalle_venta.setDouble(4, tvList.getItems().get(i).getPrecioVenta());
                detalle_venta.setDouble(5, tvList.getItems().get(i).getDescuento());
                detalle_venta.setDouble(6, tvList.getItems().get(i).getTotalImporte());
                detalle_venta.addBatch();

                if (tvList.getItems().get(i).isInventario()) {
                    articulo_update.setDouble(1, tvList.getItems().get(i).getCantidad());
//                    articulo_update.setDouble(2, tvList.getItems().get(i).getTotalImporte());
                    articulo_update.setString(2, tvList.getItems().get(i).getIdArticulo());
                    articulo_update.addBatch();

                    preparedHistorialArticulo.setString(1, tvList.getItems().get(i).getIdArticulo());
                    preparedHistorialArticulo.setString(2, "Venta");
                    preparedHistorialArticulo.setDouble(3, 0);
                    preparedHistorialArticulo.setDouble(4, tvList.getItems().get(i).getUnidadVenta() == 1
                            ? tvList.getItems().get(i).getCantidad()
                            : tvList.getItems().get(i).getTotalImporte()
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
                            : tvList.getItems().get(i).getTotalImporte()
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
                ventaTB.setEstadoName(rsEmps.getString("Estado"));
                ventaTB.setMonedaName(rsEmps.getString("Abreviado"));
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

    public static ObservableList<VentaTB> ListVentasByDate(String fechaInicial, String fechaFinal, int comprobante, int estado) {
        String selectStmt = "{call Sp_Listar_Ventas_By_Date(?,?,?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<VentaTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, fechaInicial);
            preparedStatement.setString(2, fechaFinal);
            preparedStatement.setInt(3, comprobante);
            preparedStatement.setInt(4, estado);
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
                ventaTB.setEstadoName(rsEmps.getString("Estado"));
                ventaTB.setMonedaName(rsEmps.getString("Abreviado"));
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

    public static ObservableList<DetalleVentaTB> ListVentasDetalle(String value) throws SQLException {
        String selectStmt = "{call Sp_Listar_Ventas_Detalle_By_Id(?)}";
        PreparedStatement preparedStatement;
        ResultSet rsEmps;
        ObservableList<DetalleVentaTB> empList = FXCollections.observableArrayList();
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
        preparedStatement.close();
        rsEmps.close();
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

    public static VentaTB GetVenta(String value) throws SQLException {
        PreparedStatement statementVendedor;
        VentaTB ventaTB = null;
        statementVendedor = DBUtil.getConnection().prepareStatement("select  dbo.Fc_Obtener_Nombre_Detalle(v.Estado,'0009') Estado,m.Simbolo,v.Total\n"
                + "from VentaTB as v inner join MonedaTB as m on v.Moneda = m.IdMoneda\n"
                + "where v.IdVenta = ?");
        statementVendedor.setString(1, value);
        try (ResultSet resultSet = statementVendedor.executeQuery()) {
            if (resultSet.next()) {
                ventaTB = new VentaTB();
                ventaTB.setEstadoName(resultSet.getString("Estado"));
                ventaTB.setMonedaName(resultSet.getString("Simbolo"));
                ventaTB.setTotal(resultSet.getDouble("Total"));
            }
            statementVendedor.close();
        }
        statementVendedor.close();
        return ventaTB;
    }

    public static EmpleadoTB GetEmpleadoVenta(String value) throws SQLException {
        PreparedStatement statementVendedor;
        EmpleadoTB empleadoTB = null;
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
        statementVendedor.close();
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
