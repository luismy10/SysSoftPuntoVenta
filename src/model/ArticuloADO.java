package model;

import controller.Session;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArticuloADO {

    public static String CrudArticulo(ArticuloTB articuloTB) {
        PreparedStatement preparedArticulo = null;
        PreparedStatement preparedImagen = null;
        PreparedStatement preparedHistorialArticulo = null;
        PreparedStatement preparedValidation = null;
        CallableStatement codigoArticulo = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            preparedValidation = DBUtil.getConnection().prepareStatement("select IdArticulo from ArticuloTB where IdArticulo = ?");
            preparedValidation.setString(1, articuloTB.getIdArticulo());
            if (preparedValidation.executeQuery().next()) {
                preparedValidation = DBUtil.getConnection().prepareStatement("select Clave from ArticuloTB where IdArticulo <> ? and Clave = ?");
                preparedValidation.setString(1, articuloTB.getIdArticulo());
                preparedValidation.setString(2, articuloTB.getClave().get());
                if (preparedValidation.executeQuery().next()) {
                    DBUtil.getConnection().rollback();
                    return "duplicate";
                } else {
                    preparedArticulo = DBUtil.getConnection().prepareStatement("update ArticuloTB set Clave = ?,ClaveAlterna=?, NombreMarca=UPPER(?),NombreGenerico=UPPER(?), Descripcion=UPPER(?),Categoria=?,Marca=?,Presentacion=?, StockMinimo=?,StockMaximo=?,PrecioCompra=?, PrecioVenta=?,Estado=?,Lote=? where IdArticulo = ?");
                    preparedArticulo.setString(1, articuloTB.getClave().get());
                    preparedArticulo.setString(2, articuloTB.getClaveAlterna());
                    preparedArticulo.setString(3, articuloTB.getNombreMarca().get());
                    preparedArticulo.setString(4, articuloTB.getNombreGenerico());
                    preparedArticulo.setString(5, articuloTB.getDescripcion());
                    preparedArticulo.setInt(6, articuloTB.getCategoria());
                    preparedArticulo.setInt(7, articuloTB.getMarcar());
                    preparedArticulo.setInt(8, articuloTB.getPresentacion());
                    preparedArticulo.setDouble(9, articuloTB.getStockMinimo());
                    preparedArticulo.setDouble(10, articuloTB.getStockMaximo());
                    preparedArticulo.setDouble(11, articuloTB.getPrecioCompra());
                    preparedArticulo.setDouble(12, articuloTB.getPrecioVenta().get());
                    preparedArticulo.setInt(13, articuloTB.getEstado());
                    preparedArticulo.setBoolean(14, articuloTB.isLote());
                    preparedArticulo.setString(15, articuloTB.getIdArticulo());
                    preparedArticulo.addBatch();

                    preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("insert into HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
                            + "								values(?,GETDATE(),?,?,?,?,?)");
                    preparedHistorialArticulo.setString(1, articuloTB.getIdArticulo());
                    preparedHistorialArticulo.setString(2, "Actualización del artículo");
                    preparedHistorialArticulo.setDouble(3, 0);
                    preparedHistorialArticulo.setDouble(4, 0);
                    preparedHistorialArticulo.setDouble(5, 0);
                    preparedHistorialArticulo.setString(6, Session.USER_ID);

                    preparedHistorialArticulo.addBatch();

                    preparedArticulo.executeBatch();
                    preparedHistorialArticulo.executeBatch();
                    DBUtil.getConnection().commit();
                    return "updated";
                }
            } else {
                preparedValidation = DBUtil.getConnection().prepareStatement("select Clave from ArticuloTB where Clave = ?");
                preparedValidation.setString(1, articuloTB.getClave().get());
                if (preparedValidation.executeQuery().next()) {
                    DBUtil.getConnection().rollback();
                    return "duplicate";
                } else {
                    codigoArticulo = DBUtil.getConnection().prepareCall("{? = call Fc_Articulo_Codigo_Alfanumerico()}");
                    codigoArticulo.registerOutParameter(1, java.sql.Types.VARCHAR);
                    codigoArticulo.execute();
                    String idArticulo = codigoArticulo.getString(1);

                    preparedArticulo = DBUtil.getConnection().prepareStatement("insert into ArticuloTB(IdArticulo,Clave,ClaveAlterna,NombreMarca,NombreGenerico,Descripcion,Categoria,Marca,Presentacion,StockMinimo,StockMaximo,PrecioCompra,PrecioVenta,Cantidad,Estado,Lote)values(?,?,?,UPPER(?),UPPER(?),UPPER(?),?,?,?,?,?,?,?,0,?,?)");
                    preparedArticulo.setString(1, idArticulo);
                    preparedArticulo.setString(2, articuloTB.getClave().get());
                    preparedArticulo.setString(3, articuloTB.getClaveAlterna());
                    preparedArticulo.setString(4, articuloTB.getNombreMarca().get());
                    preparedArticulo.setString(5, articuloTB.getNombreGenerico());
                    preparedArticulo.setString(6, articuloTB.getDescripcion());
                    preparedArticulo.setInt(7, articuloTB.getCategoria());
                    preparedArticulo.setInt(8, articuloTB.getMarcar());
                    preparedArticulo.setInt(9, articuloTB.getPresentacion());
                    preparedArticulo.setDouble(10, articuloTB.getStockMinimo());
                    preparedArticulo.setDouble(11, articuloTB.getStockMaximo());
                    preparedArticulo.setDouble(12, articuloTB.getPrecioCompra());
                    preparedArticulo.setDouble(13, articuloTB.getPrecioVenta().get());
                    preparedArticulo.setInt(14, articuloTB.getEstado());
                    preparedArticulo.setBoolean(15, articuloTB.isLote());
                    preparedArticulo.addBatch();

                    preparedImagen = DBUtil.getConnection().prepareStatement("insert into ImagenTB(Imagen,IdRelacionado)values(?,?)");
                    preparedImagen.setBinaryStream(1, articuloTB.getImagenTB().getFile());
                    preparedImagen.setString(2, idArticulo);
                    preparedImagen.addBatch();

                    preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("insert into HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
                            + "								values(?,GETDATE(),'Alta de Artículo',0,0,0,?)");
                    preparedHistorialArticulo.setString(1, idArticulo);
                    preparedHistorialArticulo.setString(2, Session.USER_ID);
                    preparedHistorialArticulo.addBatch();

                    preparedArticulo.executeBatch();
                    preparedImagen.executeBatch();
                    preparedHistorialArticulo.executeBatch();
                    DBUtil.getConnection().commit();
                    return "registered";
                }
            }
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
            } catch (SQLException exr) {
                return exr.getLocalizedMessage();
            }
            return ex.getLocalizedMessage();
        } finally {
            try {
                if (preparedArticulo != null) {
                    preparedArticulo.close();
                }
                if (preparedImagen != null) {
                    preparedImagen.close();
                }
                if (preparedHistorialArticulo != null) {
                    preparedHistorialArticulo.close();
                }
                if (codigoArticulo != null) {
                    codigoArticulo.close();
                }
                if (preparedValidation != null) {
                    preparedValidation.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {
                return ex.getLocalizedMessage();
            }
        }
    }

    private static String CrudEntity(ArticuloTB articuloTB) {
        String selectStmt = "{call Sp_Crud_Articulo(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setString("IdArticulo", articuloTB.getIdArticulo());
            callableStatement.setString("Clave", articuloTB.getClave().get());
            callableStatement.setString("ClaveAlterna", articuloTB.getClaveAlterna());
            callableStatement.setString("NombreMarca", articuloTB.getNombreMarca().get());
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

    public static String ImportarArticulos(TableView<ArticuloTB> tvList) {
        PreparedStatement preparedArticulo = null;
        PreparedStatement preparedHistorialArticulo = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            preparedArticulo = DBUtil.getConnection().prepareStatement("update ArticuloTB set PrecioCompra = ?,PrecioVenta = ?,Cantidad = ? where IdArticulo = ?");

            preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("insert into HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro) values(?,GETDATE(),?,?,?,?,?)");

            for (int i = 0; i < tvList.getItems().size(); i++) {
                preparedArticulo.setDouble(1, tvList.getItems().get(i).getPrecioCompra());
                preparedArticulo.setDouble(2, tvList.getItems().get(i).getPrecioVenta().get());
                preparedArticulo.setDouble(3, tvList.getItems().get(i).getCantidad().get());
                preparedArticulo.setString(4, tvList.getItems().get(i).getIdArticulo());
                preparedArticulo.addBatch();

                preparedHistorialArticulo.setString(1, tvList.getItems().get(i).getIdArticulo());
                preparedHistorialArticulo.setString(2, "Actualización del artículo");
                preparedHistorialArticulo.setDouble(3, tvList.getItems().get(i).getCantidad().get());
                preparedHistorialArticulo.setDouble(4, 0);
                preparedHistorialArticulo.setDouble(5, tvList.getItems().get(i).getCantidad().get());
                preparedHistorialArticulo.setString(6, Session.USER_ID);
                preparedHistorialArticulo.addBatch();
            }

            preparedArticulo.executeBatch();
            preparedHistorialArticulo.executeBatch();
            DBUtil.getConnection().commit();
            return "register";
        } catch (SQLException ex) {
            try {
                DBUtil.getConnection().rollback();
            } catch (SQLException exr) {
                return exr.getLocalizedMessage();
            }
            return ex.getLocalizedMessage();
        } finally {
            try {
                if (preparedArticulo != null) {
                    preparedArticulo.close();
                }
                if (preparedHistorialArticulo != null) {
                    preparedHistorialArticulo.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

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
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
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
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
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
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
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
        String selectStmt = "SELECT IdArticulo,Clave,NombreMarca,Lote,PrecioCompra,PrecioVenta,Cantidad "
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
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
                articuloTB.setLote(rsEmps.getBoolean("Lote"));
                articuloTB.setPrecioCompra(rsEmps.getDouble("PrecioCompra"));
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
