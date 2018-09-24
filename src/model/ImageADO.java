package model;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;
import javafx.scene.image.Image;

public class ImageADO {

    public static String CrudImage(ImagenTB imagenTB) {
        String selectStmt = "{call Sp_Crud_Imagen(?,?,?,?)}";
        CallableStatement callableStatement = null;
        try {
            DBUtil.dbConnect();
            callableStatement = DBUtil.getConnection().prepareCall(selectStmt);
            callableStatement.setLong("IdImagen", imagenTB.getIdImage());
            callableStatement.setBinaryStream("Imagen", imagenTB.getFile());
            callableStatement.setString("IdRelacionado", imagenTB.getIdRelacionado());
            //
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

    public static ImagenTB GetImage(String idRepresentante) {
        String selectStmt = "SELECT IdImagen,Imagen FROM ImagenTB WHERE IdRelacionado = ?";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ImagenTB imagenTB = new ImagenTB();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, idRepresentante);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                imagenTB.setIdImage(rsEmps.getLong("IdImagen"));                
                imagenTB.setImagen(new Image(rsEmps.getBinaryStream("Imagen")));
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

        return imagenTB;
    }

    public static Stack<Image> GetImageList(String idRepresentante) {
        String selectStmt = "SELECT Imagen FROM ImagenTB WHERE IdRelacionado = ?";
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        Stack stacks = new Stack();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, idRepresentante);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                InputStream stream = rsEmps.getBinaryStream("Imagen");
                stacks.add(new Image(stream));
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

        return stacks;
    }
}
