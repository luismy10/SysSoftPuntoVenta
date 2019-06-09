/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class UtilidadADO {
    
    /*  @option tinyint,
        @fechaInicial Date,
        @fechaFinal Date,
        @busqueda varchar(120)
    */
    
    public static ObservableList<Utilidad> listUtilidadVenta(int opcion, String fechaInicial, String fechaFinal, String busqueda){
        String selectStmt = "{call Sp_Listar_Utilidad(?,?,?,?)}";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ObservableList<Utilidad> empList = FXCollections.observableArrayList();
        
        System.out.println("-- Fechas --");
        System.out.println("");
        System.out.println(fechaInicial);
        System.out.println(fechaFinal);
        
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
                utilidad.setId(rsEmps.getRow());
                utilidad.setIdArticulo(rsEmps.getString("IdArticulo"));
                utilidad.setClave(rsEmps.getString("Clave"));
                utilidad.setNombreMarca(rsEmps.getString("NombreMarca"));
                utilidad.setCantidad(rsEmps.getDouble("Cantidad"));
                utilidad.setCantidadGranel(rsEmps.getDouble("CantidadGranel"));
                utilidad.setCostoUnitario(rsEmps.getDouble("CostoVenta"));
                utilidad.setPrecioUnitario(rsEmps.getDouble("PrecioVenta"));
                utilidad.setCostoTotal(rsEmps.getDouble("CostoTotal"));
                utilidad.setPrecioTotal(rsEmps.getDouble("PrecioTotal"));
                utilidad.setUtilidad(rsEmps.getDouble("Utilidad"));
                utilidad.setValorInventario(rsEmps.getBoolean("ValorInventario"));
                utilidad.setUnidadCompra(rsEmps.getString("UnidadCompra"));
                utilidad.setSimboloMoneda(rsEmps.getString("Simbolo"));
                empList.add(utilidad);
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

}
