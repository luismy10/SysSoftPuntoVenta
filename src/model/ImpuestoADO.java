
package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ImpuestoADO {   
    
    public static String CrudImpuesto(ImpuestoTB impuestoTB){
        String result = null;
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statementValidate = null;
            PreparedStatement statementImpuesto = null;
            try {
                DBUtil.getConnection().setAutoCommit(false);
                statementValidate = DBUtil.getConnection().prepareStatement("SELECT IdImpuesto FROM ImpuestoTB WHERE IdImpuesto = ?");
                statementValidate.setInt(1, impuestoTB.getIdImpuesto());
                if (statementValidate.executeQuery().next()) {
                    statementValidate = DBUtil.getConnection().prepareStatement("SELECT Nombre FROM ImpuestoTB WHERE IdImpuesto <> ? AND Nombre = ?");
                    statementValidate.setInt(1, impuestoTB.getIdImpuesto());
                    statementValidate.setString(2, impuestoTB.getNombre());
                    if (statementValidate.executeQuery().next()) {
                        DBUtil.getConnection().rollback();
                        result = "duplicated";
                    } else {
                        statementImpuesto = DBUtil.getConnection().prepareStatement("UPDATE ImpuestoTB SET Nombre=?,Valor=?,CodigoAlterno=? WHERE IdImpuesto = ?");
                        statementImpuesto.setString(1,impuestoTB.getNombre());
                        statementImpuesto.setDouble(2, impuestoTB.getValor());
                        statementImpuesto.setString(3, impuestoTB.getCodigoAlterno());
                        statementImpuesto.setInt(4, impuestoTB.getIdImpuesto());
                        statementImpuesto.addBatch();
                        statementImpuesto.executeBatch();
                        DBUtil.getConnection().commit();
                        result = "updated";
                    }
                } else {
                    statementValidate = DBUtil.getConnection().prepareStatement("SELECT Nombre FROM ImpuestoTB WHERE Nombre = ?");
                    statementValidate.setString(1, impuestoTB.getNombre());
                    if (statementValidate.executeQuery().next()) {
                        DBUtil.getConnection().rollback();
                        result = "duplicated";
                    } else {
                        statementImpuesto = DBUtil.getConnection().prepareStatement("insert into ImpuestoTB(Nombre,Valor,Predeterminado,CodigoAlterno) values(?,?,?,?)");
                        statementImpuesto.setString(1,impuestoTB.getNombre());
                        statementImpuesto.setDouble(2, impuestoTB.getValor());
                        statementImpuesto.setBoolean(3, impuestoTB.getPredeterminado());
                        statementImpuesto.setString(4, impuestoTB.getCodigoAlterno());
                        statementImpuesto.addBatch();
                        statementImpuesto.executeBatch();
                        DBUtil.getConnection().commit();
                        result = "inserted";
                    }
                }
            } catch (SQLException ex) {
                try {
                    DBUtil.getConnection().rollback();
                    result = ex.getLocalizedMessage();
                } catch (SQLException e) {
                    result = e.getLocalizedMessage();
                }
            } finally {
                try {
                    if (statementValidate != null) {
                        statementValidate.close();
                    }
                    if (statementImpuesto != null) {
                        statementImpuesto.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    result = ex.getLocalizedMessage();
                }
            }
        }
        return result;
    }
    
    public static ObservableList<ImpuestoTB> ListImpuestos(){
        ObservableList<ImpuestoTB> observableList = FXCollections.observableArrayList();
        DBUtil.dbConnect();        
        if(DBUtil.getConnection() != null){
            PreparedStatement statementList = null;
            try {
                statementList = DBUtil.getConnection().prepareStatement("{call Sp_Listar_Impuestos()}");
                ResultSet resultSet = statementList.executeQuery();
                while (resultSet.next()) {
                    ImpuestoTB impuestoTB = new ImpuestoTB();
                    impuestoTB.setIdImpuesto(resultSet.getInt("IdImpuesto"));
                    impuestoTB.setNombre(resultSet.getString("Nombre"));
                    impuestoTB.setValor(resultSet.getDouble("Valor"));
                    impuestoTB.setPredeterminado(resultSet.getBoolean("Predeterminado"));
                    impuestoTB.setCodigoAlterno(resultSet.getString("CodigoAlterno"));
                    impuestoTB.setImagePredeterminado(resultSet.getBoolean("Predeterminado")
                            ? new ImageView(new Image("/view/checked.png", 22, 22, false, false))
                            : new ImageView(new Image("/view/unchecked.png", 22, 22, false, false)));
                    observableList.add(impuestoTB);
                }
            } catch (SQLException ex) {
                System.out.println("Error en ImpuestoADO:" + ex.getLocalizedMessage());
            } finally {
                try {
                    if (statementList != null) {
                        statementList.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    System.out.println("Error en ImpuestoADO:" + ex.getLocalizedMessage());
                }
            }
        }
        
        return observableList;
    }
    
    public static List<ImpuestoTB> GetTipoImpuestoCombBox() {
        List<ImpuestoTB> list = new ArrayList<>();
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statement = null;
            ResultSet resultSet = null;            
            try {
                statement = DBUtil.getConnection().prepareStatement("SELECT IdImpuesto,Nombre,Valor,Predeterminado FROM ImpuestoTB");
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    ImpuestoTB impuestoTB = new ImpuestoTB();
                    impuestoTB.setIdImpuesto(resultSet.getInt("IdImpuesto"));
                    impuestoTB.setNombre(resultSet.getString("Nombre"));
                    impuestoTB.setValor(resultSet.getDouble("Valor"));
                    impuestoTB.setPredeterminado(resultSet.getBoolean("Predeterminado"));
                    list.add(impuestoTB);
                }
            } catch (SQLException ex) {
                System.out.println("Error Impuesto: " + ex.getLocalizedMessage());
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    System.out.println("Error Impuesto: " + ex.getLocalizedMessage());
                }
            }
        }
        return list;
    }
    
    public static String ChangeDefaultStateImpuesto(boolean state, int id) {
        String result = null;
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            PreparedStatement statementSelect = null;
            PreparedStatement statementUpdate = null;
            PreparedStatement statementState = null;
            try {
                System.out.println("Entre ImpuestoADO");
                DBUtil.getConnection().setAutoCommit(false);
                statementSelect = DBUtil.getConnection().prepareStatement("SELECT Predeterminado FROM ImpuestoTB WHERE Predeterminado = 1");
                if (statementSelect.executeQuery().next()) {
                    statementUpdate = DBUtil.getConnection().prepareStatement("UPDATE ImpuestoTB SET Predeterminado = 0 WHERE Predeterminado = 1");
                    statementUpdate.addBatch();

                    statementState = DBUtil.getConnection().prepareStatement("UPDATE ImpuestoTB SET Predeterminado = ? WHERE IdImpuesto = ?");
                    statementState.setBoolean(1, state);
                    statementState.setInt(2, id);
                    statementState.addBatch();

                    statementUpdate.executeBatch();
                    statementState.executeBatch();
                    DBUtil.getConnection().commit();
                    result = "updated";
                } else {
                    statementState = DBUtil.getConnection().prepareStatement("UPDATE ImpuestoTB SET Predeterminado = ? WHERE IdImpuesto = ?");
                    statementState.setBoolean(1, state);
                    statementState.setInt(2, id);
                    statementState.addBatch();
                    statementState.executeBatch();
                    DBUtil.getConnection().commit();
                    result = "updated";
                }

            } catch (SQLException ex) {
                try {
                    DBUtil.getConnection().rollback();
                    result = ex.getLocalizedMessage();
                } catch (SQLException e) {
                    result = e.getLocalizedMessage();
                }

            } finally {
                try {
                    if (statementSelect != null) {
                        statementSelect.close();
                    }
                    if (statementUpdate != null) {
                        statementUpdate.close();
                    }
                    if (statementState != null) {
                        statementState.close();
                    }
                    DBUtil.dbDisconnect();
                } catch (SQLException ex) {
                    result = ex.getLocalizedMessage();
                }
            }
        } else {
            result = "No se puedo establecer conexión con el servidor, revice y vuelva a intentarlo.";
        }
        return result;
    }
}
