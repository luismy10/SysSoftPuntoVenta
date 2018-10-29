package model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArticuloADO {

    public static String CrudEntity(ArticuloTB articuloTB) {
        String selectStmt = "{call Sp_Crud_Articulo(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdArticulo", articuloTB.getIdArticulo());
            callableStatement.setString("Clave", articuloTB.getClave().get());
            callableStatement.setString("ClaveAlterna", articuloTB.getClaveAlterna());
            callableStatement.setString("NombreMarca", articuloTB.getNombre().get());
            callableStatement.setString("NombreGenerico", articuloTB.getNombreGenerico());
            callableStatement.setString("Descripcion", articuloTB.getDescripcion());
            callableStatement.setInt("Categoria", articuloTB.getCategoria());
            callableStatement.setInt("Marca", articuloTB.getMarcar());
            callableStatement.setInt("Presentacion", articuloTB.getPresentacion());
            callableStatement.setDouble("StockMinimo", articuloTB.getStockMinimo());
            callableStatement.setDouble("StockMaximo", articuloTB.getStockMaximo());
            callableStatement.setDouble("PrecioCompra", articuloTB.getPrecioCompra());
            callableStatement.setDouble("PrecioVenta", articuloTB.getPrecioVenta().get());
            callableStatement.setInt("Estado", articuloTB.getEstado());
            callableStatement.setObject("Lote", articuloTB.isLote());
            //--------------------------------------------------------------------------
            callableStatement.setBinaryStream("Imagen", articuloTB.getImagenTB().getFile());
            //---------------------------------------------------------------------------
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

    public static ObservableList<ArticuloTB> ListArticulos(String value) {
        String selectStmt = "{call Sp_Listar_Articulo(?)}";
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
                articuloTB.setNombre(rsEmps.getString("NombreMarca"));
                articuloTB.setMarcaName(rsEmps.getString("Marca"));
                articuloTB.setPresentacionName(rsEmps.getString("Presentacion"));
                articuloTB.setEstadoName(rsEmps.getString("Estado"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setPrecioVenta(rsEmps.getDouble("PrecioVenta"));
                articuloTB.setLote(rsEmps.getBoolean("Lote"));
                articuloTB.setImageLote(rsEmps.getBoolean("Lote")
                        ? new ImageView(new Image("/view/lote-box.png", 28, 28, false, false))
                        : null);
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

    public static ArrayList<ArticuloTB> GetArticulosById(String value) {
        String selectStmt = "{call Sp_Get_Articulo_By_Id(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ArrayList<ArticuloTB> empList = new ArrayList<>();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setIdArticulo(rsEmps.getString("IdArticulo"));
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setClaveAlterna(rsEmps.getString("ClaveAlterna"));
                articuloTB.setNombre(rsEmps.getString("NombreMarca"));
                articuloTB.setNombreGenerico(rsEmps.getString("NombreGenerico"));
                articuloTB.setDescripcion(rsEmps.getString("Descripcion"));
                articuloTB.setCategoria(rsEmps.getInt("Categoria"));
                articuloTB.setCategoriaName(rsEmps.getString("CategoriaNombre"));
                articuloTB.setMarcar(rsEmps.getInt("Marca"));
                articuloTB.setMarcaName(rsEmps.getString("MarcaNombre"));
                articuloTB.setPresentacion(rsEmps.getInt("Presentacion"));
                articuloTB.setPresentacionName(rsEmps.getString("PresentacionNombre"));
                articuloTB.setStockMinimo(rsEmps.getDouble("StockMinimo"));
                articuloTB.setStockMaximo(rsEmps.getDouble("StockMaximo"));
                articuloTB.setPrecioCompra(rsEmps.getDouble("PrecioCompra"));
                articuloTB.setPrecioVenta(rsEmps.getDouble("PrecioVenta"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setEstado(rsEmps.getInt("Estado"));
                articuloTB.setLote(rsEmps.getBoolean("Lote"));
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

    public static ArrayList<ArticuloTB> GetArticulosByIdView(String value) {
//        String selectStmt = "{call Sp_Get_Articulo_By_Id_View(?)}";
        String selectStmt = "select NombreMarca,NombreGenerico,PrecioVenta,Cantidad\n"
                + "		from ArticuloTB\n"
                + "		where IdArticulo=?";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ArrayList<ArticuloTB> empList = new ArrayList<>();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setNombre(rsEmps.getString("NombreMarca"));
                articuloTB.setNombreGenerico(rsEmps.getString("NombreGenerico"));
                articuloTB.setPrecioVenta(rsEmps.getDouble("PrecioVenta"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
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

    public static ObservableList<ArticuloTB> ListIniciarInventario() {
        String selectStmt = "SELECT IdArticulo,Clave,NombreMarca,Lote,PrecioVenta,Cantidad "
                + "FROM ArticuloTB WHERE Cantidad = 0";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<ArticuloTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setIdArticulo(rsEmps.getString("IdArticulo"));
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setNombre(rsEmps.getString("NombreMarca")); 
                articuloTB.setLote(rsEmps.getBoolean("Lote")); 
                articuloTB.setPrecioVenta(rsEmps.getDouble("PrecioVenta"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
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
