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

public class ArticuloADO {

    public static String CrudArticulo(ArticuloTB articuloTB) {
        PreparedStatement preparedArticulo = null;
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
                preparedValidation.setString(2, articuloTB.getClave());
                if (preparedValidation.executeQuery().next()) {
                    DBUtil.getConnection().rollback();
                    return "duplicate";
                } else {
                    preparedValidation = DBUtil.getConnection().prepareStatement("select NombreMarca from ArticuloTB where IdArticulo <> ? and NombreMarca = ?");
                    preparedValidation.setString(1, articuloTB.getIdArticulo());
                    preparedValidation.setString(2, articuloTB.getNombreMarca());
                    if (preparedValidation.executeQuery().next()) {
                        DBUtil.getConnection().rollback();
                        return "duplicatename";
                    } else {
                        preparedArticulo = DBUtil.getConnection().prepareStatement("update ArticuloTB set "
                                + "Clave=?, "
                                + "ClaveAlterna=?, "
                                + "NombreMarca=UPPER(?), "
                                + "NombreGenerico=UPPER(?), "
                                + "Categoria=?, "
                                + "Marca=?, "
                                + "Presentacion=?, "
                                + "StockMinimo=?, "
                                + "StockMaximo=?, "
                                + "PrecioCompra=?, "
                                + "PrecioVentaGeneral=?, "
                                + "PrecioMargenGeneral=?, "
                                + "PrecioUtilidadGeneral=?, "
                                + "UnidadCompra=?, "
                                + "UnidadVenta = ?, "
                                + "Estado=?, "
                                + "Lote=?, "
                                + "Inventario=?, "
                                + "Imagen=?, "
                                + "Impuesto=? "
                                + "where IdArticulo = ?");
                        preparedArticulo.setString(1, articuloTB.getClave());
                        preparedArticulo.setString(2, articuloTB.getClaveAlterna());
                        preparedArticulo.setString(3, articuloTB.getNombreMarca());
                        preparedArticulo.setString(4, articuloTB.getNombreGenerico());
                        preparedArticulo.setInt(5, articuloTB.getCategoria());
                        preparedArticulo.setInt(6, articuloTB.getMarcar());
                        preparedArticulo.setInt(7, articuloTB.getPresentacion());
                        preparedArticulo.setDouble(8, articuloTB.getStockMinimo());
                        preparedArticulo.setDouble(9, articuloTB.getStockMaximo());
                        preparedArticulo.setDouble(10, articuloTB.getPrecioCompra());

                        preparedArticulo.setDouble(11, articuloTB.getPrecioVentaGeneral());
                        preparedArticulo.setShort(12, articuloTB.getPrecioMargenGeneral());
                        preparedArticulo.setDouble(13, articuloTB.getPrecioUtilidadGeneral());

                        preparedArticulo.setInt(14, articuloTB.getUnidadCompra());
                        preparedArticulo.setInt(15, articuloTB.getUnidadVenta());
                        preparedArticulo.setInt(16, articuloTB.getEstado());
                        preparedArticulo.setBoolean(17, articuloTB.isLote());
                        preparedArticulo.setBoolean(18, articuloTB.isInventario());
                        preparedArticulo.setString(19, articuloTB.getImagenTB());
                        preparedArticulo.setInt(20, articuloTB.getImpuestoArticulo());
                        preparedArticulo.setString(21, articuloTB.getIdArticulo());

                        preparedArticulo.addBatch();
                        preparedArticulo.executeBatch();

                        DBUtil.getConnection().commit();
                        return "updated";
                    }
                }
            } else {
                preparedValidation = DBUtil.getConnection().prepareStatement("select Clave from ArticuloTB where Clave = ?");
                preparedValidation.setString(1, articuloTB.getClave());
                if (preparedValidation.executeQuery().next()) {
                    DBUtil.getConnection().rollback();
                    return "duplicate";
                } else {
                    preparedValidation = DBUtil.getConnection().prepareStatement("select NombreMarca from ArticuloTB where NombreMarca = ?");
                    preparedValidation.setString(1, articuloTB.getNombreMarca());
                    if (preparedValidation.executeQuery().next()) {
                        DBUtil.getConnection().rollback();
                        return "duplicatename";
                    } else {
                        codigoArticulo = DBUtil.getConnection().prepareCall("{? = call Fc_Articulo_Codigo_Alfanumerico()}");
                        codigoArticulo.registerOutParameter(1, java.sql.Types.VARCHAR);
                        codigoArticulo.execute();
                        String idArticulo = codigoArticulo.getString(1);

                        preparedArticulo = DBUtil.getConnection().prepareStatement("INSERT INTO "
                                + "ArticuloTB"
                                + "(IdArticulo,"
                                + "Clave,"
                                + "ClaveAlterna,"
                                + "NombreMarca,"
                                + "NombreGenerico,"
                                + "Categoria,"
                                + "Marca,"
                                + "Presentacion,"
                                + "StockMinimo,"
                                + "StockMaximo,"
                                + "PrecioCompra,"
                                + "PrecioVentaGeneral,"
                                + "PrecioMargenGeneral,"
                                + "PrecioUtilidadGeneral,"
                                + "Cantidad,"
                                + "UnidadCompra,"
                                + "UnidadVenta,"
                                + "Estado,"
                                + "Lote,"
                                + "Inventario,"
                                + "Imagen,"
                                + "Impuesto)"
                                + "values(?,?,?,UPPER(?),UPPER(?),UPPER(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                        preparedArticulo.setString(1, idArticulo);
                        preparedArticulo.setString(2, articuloTB.getClave());
                        preparedArticulo.setString(3, articuloTB.getClaveAlterna());
                        preparedArticulo.setString(4, articuloTB.getNombreMarca());
                        preparedArticulo.setString(5, articuloTB.getNombreGenerico());
                        preparedArticulo.setInt(6, articuloTB.getCategoria());
                        preparedArticulo.setInt(7, articuloTB.getMarcar());
                        preparedArticulo.setInt(8, articuloTB.getPresentacion());
                        preparedArticulo.setDouble(9, articuloTB.getStockMinimo());
                        preparedArticulo.setDouble(10, articuloTB.getStockMaximo());
                        preparedArticulo.setDouble(11, articuloTB.getPrecioCompra());

                        preparedArticulo.setDouble(12, articuloTB.getPrecioVentaGeneral());
                        preparedArticulo.setShort(13, articuloTB.getPrecioMargenGeneral());
                        preparedArticulo.setDouble(14, articuloTB.getPrecioUtilidadGeneral());

                        preparedArticulo.setDouble(15, 0);
                        preparedArticulo.setInt(16, articuloTB.getUnidadCompra());
                        preparedArticulo.setInt(17, articuloTB.getUnidadVenta());
                        preparedArticulo.setInt(18, articuloTB.getEstado());
                        preparedArticulo.setBoolean(19, articuloTB.isLote());
                        preparedArticulo.setBoolean(20, articuloTB.isInventario());
//                    preparedArticulo.setBinaryStream(24, articuloTB.getImagenTB().getFile());
                        preparedArticulo.setString(21, articuloTB.getImagenTB());
                        preparedArticulo.setInt(22, articuloTB.getImpuestoArticulo());
                        preparedArticulo.addBatch();

                        preparedHistorialArticulo = DBUtil.getConnection().prepareStatement("insert into HistorialArticuloTB(IdArticulo,FechaRegistro,TipoOperacion,Entrada,Salida,Saldo,UsuarioRegistro)\n"
                                + "								values(?,GETDATE(),'Alta de Artículo',0,0,0,?)");
                        preparedHistorialArticulo.setString(1, idArticulo);
                        preparedHistorialArticulo.setString(2, Session.USER_ID);
                        preparedHistorialArticulo.addBatch();

                        preparedArticulo.executeBatch();
                        preparedHistorialArticulo.executeBatch();
                        DBUtil.getConnection().commit();
                        return "registered";
                    }
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
                preparedArticulo.setDouble(2, tvList.getItems().get(i).getPrecioVentaGeneral());
                preparedArticulo.setDouble(3, tvList.getItems().get(i).getCantidad());
                preparedArticulo.setString(4, tvList.getItems().get(i).getIdArticulo());
                preparedArticulo.addBatch();

                preparedHistorialArticulo.setString(1, tvList.getItems().get(i).getIdArticulo());
                preparedHistorialArticulo.setString(2, "Actualización del artículo");
                preparedHistorialArticulo.setDouble(3, tvList.getItems().get(i).getCantidad());
                preparedHistorialArticulo.setDouble(4, 0);
                preparedHistorialArticulo.setDouble(5, tvList.getItems().get(i).getCantidad());
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
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setPrecioVentaGeneral(rsEmps.getDouble("PrecioVentaGeneral"));
                articuloTB.setUnidadVenta(rsEmps.getInt("UnidadVenta"));
                articuloTB.setCategoriaName(rsEmps.getString("Categoria"));
                articuloTB.setEstadoName(rsEmps.getString("Estado"));
                articuloTB.setImagenTB(rsEmps.getString("Imagen"));
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

    public static ObservableList<ArticuloTB> ListArticulosCategoria(int value) {
        String selectStmt = "{call Sp_Listar_Articulo_Categoria(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<ArticuloTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setInt(1, value);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setId(rsEmps.getRow());
                articuloTB.setIdArticulo(rsEmps.getString("IdArticulo"));
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
                articuloTB.setMarcaName(rsEmps.getString("Marca"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setPrecioVentaGeneral(rsEmps.getDouble("PrecioVenta1"));
                articuloTB.setUnidadVenta(rsEmps.getInt("UnidadVenta"));
                articuloTB.setCategoriaName(rsEmps.getString("Categoria"));
                articuloTB.setEstadoName(rsEmps.getString("Estado"));
                articuloTB.setImagenTB(rsEmps.getString("Imagen"));
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

    public static ObservableList<ArticuloTB> ListArticulosListaView(String value) {
        String selectStmt = "{call Sp_Listar_Articulo_Lista_View(?)}";
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
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setPrecioCompra(rsEmps.getDouble("PrecioCompra"));

                articuloTB.setPrecioVentaGeneral(rsEmps.getDouble("PrecioVentaGeneral"));
                articuloTB.setPrecioMargenGeneral(rsEmps.getShort("PrecioMargenGeneral"));
                articuloTB.setPrecioUtilidadGeneral(rsEmps.getDouble("PrecioUtilidadGeneral"));

                articuloTB.setUnidadVenta(rsEmps.getInt("UnidadVenta"));
                articuloTB.setInventario(rsEmps.getBoolean("Inventario"));
                articuloTB.setImpuestoArticulo(rsEmps.getInt("Impuesto"));
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
                articuloTB.setCategoria(rsEmps.getInt("Categoria"));
                articuloTB.setCategoriaName(rsEmps.getString("CategoriaNombre"));
                articuloTB.setMarcar(rsEmps.getInt("Marca"));
                articuloTB.setMarcaName(rsEmps.getString("MarcaNombre"));
                articuloTB.setPresentacion(rsEmps.getInt("Presentacion"));
                articuloTB.setPresentacionName(rsEmps.getString("PresentacionNombre"));
                articuloTB.setUnidadCompra(rsEmps.getInt("UnidadCompra"));
                articuloTB.setUnidadCompraName(rsEmps.getString("UnidadCompraNombre"));
                articuloTB.setUnidadVenta(rsEmps.getInt("UnidadVenta"));
                articuloTB.setStockMinimo(rsEmps.getDouble("StockMinimo"));
                articuloTB.setStockMaximo(rsEmps.getDouble("StockMaximo"));
                articuloTB.setPrecioCompra(rsEmps.getDouble("PrecioCompra"));

                articuloTB.setPrecioVentaGeneral(rsEmps.getDouble("PrecioVentaGeneral"));
                articuloTB.setPrecioMargenGeneral(rsEmps.getShort("PrecioMargenGeneral"));
                articuloTB.setPrecioUtilidadGeneral(rsEmps.getDouble("PrecioUtilidadGeneral"));

                articuloTB.setEstado(rsEmps.getInt("Estado"));
                articuloTB.setLote(rsEmps.getBoolean("Lote"));
                articuloTB.setInventario(rsEmps.getBoolean("Inventario"));
                articuloTB.setImagenTB(rsEmps.getString("Imagen"));
                articuloTB.setImpuestoArticulo(rsEmps.getInt("Impuesto"));
                articuloTB.setImpuestoArticuloName(rsEmps.getString("ImpuestoNombre"));
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
                + "FROM ArticuloTB WHERE Cantidad = 0 and UnidadVenta = 1 and Inventario = 1";
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
                articuloTB.setPrecioVentaGeneral(rsEmps.getDouble("PrecioVenta"));
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

    public static ArrayList<ArticuloTB> ListInventario() {
        String selectStmt = "{call Sp_Listar_Inventario_Articulos()}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ArrayList<ArticuloTB> empList = new ArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setId(rsEmps.getRow());
                articuloTB.setIdArticulo(rsEmps.getString("IdArticulo"));
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
                articuloTB.setPrecioCompra(rsEmps.getDouble("PrecioCompra"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setUnidadCompraName(rsEmps.getString("UnidadCompra"));
                articuloTB.setEstadoName(rsEmps.getString("Estado"));
                articuloTB.setTotalImporte(rsEmps.getDouble("Total"));
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

    public static ArticuloTB Get_Articulo_By_Search(String value) {
        String selectStmt = "{call Sp_Listar_Articulo_By_Search(?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ArticuloTB articuloTB = null;
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, value);
            rsEmps = preparedStatement.executeQuery();
            if (rsEmps.next()) {
                articuloTB = new ArticuloTB();
                articuloTB.setIdArticulo(rsEmps.getString("IdArticulo"));
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
                articuloTB.setMarcaName(rsEmps.getString("Marca"));
                articuloTB.setPresentacionName(rsEmps.getString("Presentacion"));
                articuloTB.setCantidad(rsEmps.getDouble("Cantidad"));
                articuloTB.setPrecioVentaGeneral(rsEmps.getDouble("PrecioVenta1"));
                articuloTB.setUnidadVenta(rsEmps.getInt("UnidadVenta"));
                articuloTB.setLote(rsEmps.getBoolean("Lote"));
                articuloTB.setInventario(rsEmps.getBoolean("Inventario"));
                articuloTB.setImpuestoArticulo(rsEmps.getInt("Impuesto"));
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
        return articuloTB;
    }

    public static void ListArticulosPaginacion() {
        PreparedStatement statementTotal = null;
        ResultSet resultSet = null;
        try {
            DBUtil.dbConnect();
            statementTotal = DBUtil.getConnection().prepareStatement("select count(*) as Total from ArticuloTB");
            resultSet = statementTotal.executeQuery();
            int total = 0;
            if (resultSet.next()) {
                total = resultSet.getInt("Total");
            }
            System.out.println(total);
        } catch (SQLException e) {

        } finally {
            try {
                if (statementTotal != null) {
                    statementTotal.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }

        }
    }

    public static ObservableList<ArticuloTB> ListArticulosCodBar(int unidadVenta, int categoria) {
        String selectStmt = "{call Sp_Generar_Listardo_CodBar(?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<ArticuloTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setInt(1, unidadVenta);
            preparedStatement.setInt(2, categoria);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                ArticuloTB articuloTB = new ArticuloTB();
                articuloTB.setClave(rsEmps.getString("Clave"));
                articuloTB.setClaveAlterna(rsEmps.getString("ClaveAlterna"));
                articuloTB.setNombreMarca(rsEmps.getString("NombreMarca"));
                articuloTB.setUnidadVenta(rsEmps.getInt("UnidadVenta"));
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

    public static String RemoveArticulo(String idArticulo) {
        String result = "";
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statementArticulo = null;
            PreparedStatement statementHistorial = null;

            PreparedStatement statementCompra = null;
            PreparedStatement statementVenta = null;
            try {
                DBUtil.getConnection().setAutoCommit(false);

                statementCompra = DBUtil.getConnection().prepareStatement("SELECT * FROM DetalleCompraTB WHERE IdArticulo = ?");
                statementCompra.setString(1, idArticulo);

                statementVenta = DBUtil.getConnection().prepareStatement("SELECT * FROM DetalleVentaTB WHERE IdArticulo = ?");
                statementVenta.setString(1, idArticulo);

                if (statementCompra.executeQuery().next()) {
                    DBUtil.getConnection().rollback();
                    result = "compra";
                } else if (statementVenta.executeQuery().next()) {
                    DBUtil.getConnection().rollback();
                    result = "venta";
                } else {
                    statementArticulo = DBUtil.getConnection().prepareStatement("DELETE FROM ArticuloTB WHERE IdArticulo = ?");
                    statementArticulo.setString(1, idArticulo);
                    statementArticulo.addBatch();

                    statementHistorial = DBUtil.getConnection().prepareStatement("DELETE FROM HistorialArticuloTB WHERE IdArticulo = ?");
                    statementHistorial.setString(1, idArticulo);
                    statementHistorial.addBatch();

                    statementArticulo.executeBatch();
                    statementHistorial.executeBatch();
                    DBUtil.getConnection().commit();
                    result = "removed";
                }

            } catch (SQLException ex) {
                try {
                    result = ex.getLocalizedMessage();
                    DBUtil.getConnection().rollback();
                } catch (SQLException e) {
                    result = e.getLocalizedMessage();
                }
            } finally {
                try {
                    if (statementCompra != null) {
                        statementCompra.close();
                    }
                    if (statementVenta != null) {
                        statementVenta.close();
                    }
                    if (statementArticulo != null) {
                        statementArticulo.close();
                    }
                    if (statementHistorial != null) {
                        statementHistorial.close();
                    }

                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    result = ex.getLocalizedMessage();
                }
            }
        } else {
            result = "No se pudo establecer la conexión, revise el estado.";
        }
        return result;

    }

    public static ArticuloTB GetItemPriceList(String idArticulo) {
        PreparedStatement statementVendedor = null;
        ArticuloTB articuloTB = null;
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            try {
                statementVendedor = DBUtil.getConnection().prepareStatement("SELECT IdArticulo,PrecioVenta1,PrecioVenta2,PrecioVenta3 FROM ArticuloTB WHERE IdArticulo = ?");
                statementVendedor.setString(1, idArticulo);
                try (ResultSet resultSet = statementVendedor.executeQuery()) {
                    if (resultSet.next()) {
                        articuloTB = new ArticuloTB();
                        articuloTB.setIdArticulo(resultSet.getString("IdArticulo"));
//                        articuloTB.setPrecioVenta(resultSet.getDouble("PrecioVenta1"));
//                        articuloTB.setPrecioVenta2(resultSet.getDouble("PrecioVenta2"));
//                        articuloTB.setPrecioVenta3(resultSet.getDouble("PrecioVenta3"));
                    }
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

        }

        return articuloTB;
    }

    public static String AjusteInventarioByIid(String idArticulo, double parseDouble) {
        String result = "";
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statementValidate = null;
            PreparedStatement statementUpdate = null;
            try {
                DBUtil.getConnection().setAutoCommit(false);
                statementValidate = DBUtil.getConnection().prepareStatement("SELECT * FROM ArticuloTB WHERE IdArticulo = ? AND Inventario = 0");
                statementValidate.setString(1, idArticulo);
                if (statementValidate.executeQuery().next()) {
                    DBUtil.getConnection().rollback();
                    result = "inventario";
                } else {
                    statementUpdate = DBUtil.getConnection().prepareStatement("UPDATE ArticuloTB SET Cantidad = ? WHERE IdArticulo = ?");
                    statementUpdate.setDouble(1, parseDouble);
                    statementUpdate.setString(2, idArticulo);
                    statementUpdate.addBatch();
                    statementUpdate.executeBatch();
                    DBUtil.getConnection().commit();
                    result = "updated";
                }
            } catch (SQLException ex) {
                try {
                    DBUtil.getConnection().rollback();
                } catch (SQLException e) {

                }
                result = ex.getLocalizedMessage();
            } finally {
                try {
                    if (statementUpdate != null) {
                        statementUpdate.close();
                    }
                    if (statementValidate != null) {
                        statementValidate.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    result = ex.getLocalizedMessage();
                }
            }
        }
        return result;
    }

}
