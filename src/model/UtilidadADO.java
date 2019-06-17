package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class UtilidadADO {

    public static ObservableList<Utilidad> listUtilidadVenta(int opcion, String fechaInicial, String fechaFinal, String busqueda) {
        String selectStmt = "{call Sp_Listar_Utilidad(?,?,?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<Utilidad> empList = FXCollections.observableArrayList();

        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setInt(1, opcion);
            preparedStatement.setString(2, fechaInicial);
            preparedStatement.setString(3, fechaFinal);
            preparedStatement.setString(4, busqueda);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                Utilidad utilidad = new Utilidad();
                //rsEmps.getRow()

                double cantidadKilogramos = 0;

                utilidad.setId(rsEmps.getRow());
                utilidad.setIdArticulo(rsEmps.getString("IdArticulo"));
                utilidad.setClave(rsEmps.getString("Clave"));
                utilidad.setNombreMarca(rsEmps.getString("NombreMarca"));
                utilidad.setCantidad(rsEmps.getDouble("Cantidad"));
                utilidad.setCantidadGranel(rsEmps.getDouble("CantidadGranel"));
                utilidad.setCostoVenta(rsEmps.getDouble("CostoVenta"));
                utilidad.setPrecioVenta(rsEmps.getDouble("PrecioVenta"));
                utilidad.setPrecioVentaGranel(rsEmps.getDouble("PrecioVentaGranel"));
                utilidad.setValorInventario(rsEmps.getBoolean("ValorInventario"));
                utilidad.setUnidadCompra(rsEmps.getString("UnidadCompra"));
                utilidad.setSimboloMoneda(rsEmps.getString("Simbolo"));

                cantidadKilogramos = utilidad.getPrecioVenta() / utilidad.getPrecioVentaGranel();

                utilidad.setUtilidad(
                        utilidad.isValorInventario()
                        ? (utilidad.getPrecioVenta() * utilidad.getCantidad()) - (utilidad.getCostoVenta() * utilidad.getCantidad())
                        : (utilidad.getPrecioVentaGranel() * cantidadKilogramos) - (utilidad.getCostoVenta() * cantidadKilogramos)
                );

                if (validateDuplicateArticulo(empList, utilidad)) {
                    for (int i = 0; i < empList.size(); i++) {
                        if (empList.get(i).getIdArticulo().equalsIgnoreCase(utilidad.getIdArticulo())) {
//                            double newU = utilidad.getUtilidad();
//                            Utilidad newUtilidad = utilidad;
//                            newUtilidad.setUtilidad(newUtilidad.getUtilidad()+newU);
//                            empList.set(i, newUtilidad);
                            System.out.println(utilidad.getNombreMarca());
                            System.out.println(utilidad.getUtilidad());
                        }
                    }
                } else {
                    empList.add(utilidad);
                }
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

    private static boolean validateDuplicateArticulo(ObservableList<Utilidad> view, Utilidad utilidad) {
        boolean ret = false;
        for (int i = 0; i < view.size(); i++) {
            if (view.get(i).getClave().equals(utilidad.getClave())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

}
