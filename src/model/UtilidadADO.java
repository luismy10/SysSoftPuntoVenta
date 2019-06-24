package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UtilidadADO {

    public static ObservableList<Utilidad> listUtilidadVenta(short opcion, String fechaInicial, String fechaFinal, String busqueda) {
        String selectStmt = "{call Sp_Listar_Utilidad(?,?,?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<Utilidad> empList = FXCollections.observableArrayList();
        ArrayList<Utilidad> list = new ArrayList<>();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setShort(1, opcion);
            preparedStatement.setString(2, fechaInicial);
            preparedStatement.setString(3, fechaFinal);
            preparedStatement.setString(4, busqueda);
            rsEmps = preparedStatement.executeQuery();
            while (rsEmps.next()) {
                Utilidad utilidad = new Utilidad();
                utilidad.setIdArticulo(rsEmps.getString("IdArticulo"));
                utilidad.setClave(rsEmps.getString("Clave"));
                utilidad.setNombreMarca(rsEmps.getString("NombreMarca"));
                utilidad.setCantidad(rsEmps.getDouble("Cantidad"));
                utilidad.setCantidadGranel(rsEmps.getDouble("CantidadGranel"));
                utilidad.setCostoVenta(rsEmps.getDouble("CostoVenta"));
                //utilidad.setCostoVentaGranel(rsEmps.getDouble("CostoVentaGranel"));
                utilidad.setPrecioVenta(rsEmps.getDouble("PrecioVenta"));
                utilidad.setPrecioVentaGranel(rsEmps.getDouble("PrecioVentaGranel"));
                utilidad.setPrecioVentaTotal(utilidad.getCantidad() * utilidad.getPrecioVenta());
                utilidad.setValorInventario(rsEmps.getBoolean("ValorInventario"));
                utilidad.setUnidadCompra(rsEmps.getString("UnidadCompra"));
                utilidad.setSimboloMoneda(rsEmps.getString("Simbolo"));

                double cantidadKilogramos = utilidad.getPrecioVenta() / utilidad.getPrecioVentaGranel();

                utilidad.setCostoVentaTotal(
                        utilidad.isValorInventario()
                        ? utilidad.getCantidad() * utilidad.getCostoVenta()
                        : utilidad.getCantidadGranel() * utilidad.getCostoVenta()
                );
                utilidad.setUtilidad(
                        utilidad.isValorInventario()
                        ? (utilidad.getPrecioVenta() * utilidad.getCantidad()) - (utilidad.getCostoVenta() * utilidad.getCantidad())
                        : (utilidad.getPrecioVentaGranel() * cantidadKilogramos) - (utilidad.getCostoVenta() * cantidadKilogramos)
                );
                list.add(utilidad);

            }
            
            int count = 0;
            
            for (int i = 0; i < list.size(); i++) {
                
                if (validateDuplicateArticulo(empList, list.get(i))) {
                    for (int j = 0; j < empList.size(); j++) {
                        if (empList.get(j).getIdArticulo().equalsIgnoreCase(list.get(i).getIdArticulo())) {
                            Utilidad newUtilidad = empList.get(j);
                            newUtilidad.setCantidad(newUtilidad.getCantidad() + list.get(i).getCantidad());
                            newUtilidad.setCostoVentaTotal(newUtilidad.getCostoVentaTotal() + list.get(i).getCostoVentaTotal());
                            newUtilidad.setCantidadGranel(newUtilidad.getCantidadGranel() + list.get(i).getCantidadGranel());
                            newUtilidad.setPrecioVentaTotal(newUtilidad.getPrecioVentaTotal() + list.get(i).getPrecioVentaTotal());
                            newUtilidad.setUtilidad(newUtilidad.getUtilidad() + list.get(i).getUtilidad());
                            empList.set(j, newUtilidad);
                        }
                    }
                } else {
                    count++;
                    Utilidad donotexist = list.get(i);
                    donotexist.setId(count);
                    empList.add(donotexist);
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
            if (view.get(i).getIdArticulo().equals(utilidad.getIdArticulo())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

}
