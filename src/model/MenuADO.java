package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MenuADO {

    public static ObservableList<MenuTB> GetMenus(int idRol) {
        String selectStmt = "select m.IdMenu,m.Nombre,pm.Estado from \n"
                + "PermisoMenusTB as pm inner join RolTB as r \n"
                + "on pm.IdRol = r.IdRol\n"
                + "inner join MenuTB as m \n"
                + "on pm.IdMenus = m.IdMenu\n"
                + "where pm.IdRol = ? ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<MenuTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setInt(1, idRol);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                MenuTB menuTB = new MenuTB();
                menuTB.setIdMenu(resultSet.getInt("IdMenu"));
                menuTB.setNombre(resultSet.getString("Nombre"));
                menuTB.setEstado(resultSet.getBoolean("Estado"));
                empList.add(menuTB);
            }

        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return empList;
    }

    public static ObservableList<SubMenusTB> GetSubMenus(int rol, int menu) {
        String selectStmt = "select sm.IdSubmenu,sm.Nombre,psm.Estado from PermisoSubMenusTB as psm inner join RolTB as r \n"
                + "on psm.IdRol = r.IdRol\n"
                + "inner join SubmenuTB as sm\n"
                + "on psm.IdSubMenus = sm.IdSubmenu\n"
                + "where psm.IdRol = ? and psm.IdMenus = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<SubMenusTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setInt(1, rol);
            preparedStatement.setInt(2, menu);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                SubMenusTB menuTB = new SubMenusTB();
                menuTB.setIdSubMenu(resultSet.getInt("IdSubmenu"));
                menuTB.setNombre(resultSet.getString("Nombre"));
                menuTB.setEstado(resultSet.getBoolean("Estado"));
                empList.add(menuTB);
            }

        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return empList;
    }
    
    public static ObservableList<MenuTB> ListMenus() {
        String selectStmt = "select IdMenu,Nombre from MenuTB";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<MenuTB> empList = FXCollections.observableArrayList();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);        
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MenuTB menuTB = new MenuTB();
                menuTB.setIdMenu(resultSet.getInt("IdMenu"));
                menuTB.setNombre(resultSet.getString("Nombre"));
                empList.add(menuTB);
            }

        } catch (SQLException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                DBUtil.dbDisconnect();
            } catch (SQLException ex) {

            }
        }
        return empList;
    }

}
