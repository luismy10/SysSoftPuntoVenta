/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Session;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Ruberfc
 */
public class VentaADO {

    public static String CrudVenta(VentaTB ventaTB, TableView<ArticuloTB> tvList) {

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

            serie_numeracion = DBUtil.getConnection().prepareCall("{? = call Fc_Serie_Numero_Generado()}");
            serie_numeracion.registerOutParameter(1, java.sql.Types.VARCHAR);
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
                    + "           ,[Total])\n"
                    + "     VALUES\n"
                    + "           (?,?,?,?,?,?,?,?,?,?,?,?)");

            comprobante = DBUtil.getConnection().prepareStatement("INSERT INTO ComprobanteTB(Serie,Numeracion,FechaRegistro)VALUES(?,?,?)");

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
            venta.addBatch();

            byte[] bytes = DatatypeConverter.parseHexBinary(id_comprabante[0]);

            comprobante.setBytes(1, bytes);
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

    private String InsertDetalleVenta(DetalleVentaTB detalleVentaTB, TableView<ArticuloTB> tableView, ObservableList<LoteTB> loteTBs) throws SQLException {

        PreparedStatement venta = null;

        CallableStatement codigo_venta = null;
        PreparedStatement detalle_venta = null;

        PreparedStatement articulo_update = null;

        PreparedStatement lote_compra = null;

        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            codigo_venta = DBUtil.getConnection().prepareCall("{? = call Fc_Serie_Numero_Generado()}");
            codigo_venta.registerOutParameter(1, java.sql.Types.VARCHAR);
            codigo_venta.execute();

            String id_venta = codigo_venta.getString(1);

            detalle_venta = DBUtil.getConnection().prepareStatement("INSERT INTO DetalleVentaTB(IdVenta,IdArticulo,Cantidad,PrecioUnitario,Descuento,Importe)"
                    + "VALUES(?,?,?,?,?,?)");
            articulo_update = DBUtil.getConnection().prepareStatement("UPDATE ArticuloTB SET Cantidad = Cantidad + ? WHERE IdArticulo = ?");

            for (int i = 0; i < tableView.getItems().size(); i++) {
                detalle_venta.setString(1, id_venta);
                detalle_venta.setString(2, tableView.getItems().get(i).getIdArticulo());
                detalle_venta.setInt(3, detalleVentaTB.getCantidad());
                detalle_venta.setDouble(4, detalleVentaTB.getPrecioUnitario());
                detalle_venta.setDouble(5, detalleVentaTB.getDescuento());
                detalle_venta.setDouble(6, detalleVentaTB.getImporte());
                detalle_venta.addBatch();

                articulo_update.setDouble(3, tableView.getItems().get(i).getCantidad());
                //articulo_update.setString(4, tableView.getItems().get(i).getIdArticulo());
                articulo_update.addBatch();

            }

            detalle_venta.executeBatch();
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
//                if (venta != null) {
//                    venta.close();
//                }
                if (detalle_venta != null) {
                    detalle_venta.close();
                }
                if (articulo_update != null) {
                    articulo_update.close();
                }
                if (codigo_venta != null) {
                    codigo_venta.close();
                }
//                if (lote_compra != null) {
//                    lote_compra.close();
//                }
                DBUtil.dbDisconnect();
            } catch (SQLException e) {
            }
        }
    }

}
