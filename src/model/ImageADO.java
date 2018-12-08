package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;

public class ImageADO {

    public static String CrudImage(ImagenTB imagenTB) {
        PreparedStatement preparedImagen = null;
        PreparedStatement preparedValidation = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            preparedValidation = DBUtil.getConnection().prepareStatement("select IdImagen from ImagenTB where IdImagen=? and IdRelacionado=?");
            preparedValidation.setLong(1, imagenTB.getIdImage());
            preparedValidation.setString(2, imagenTB.getIdRelacionado());
            if (preparedValidation.executeQuery().next()) {
                preparedImagen = DBUtil.getConnection().prepareCall("update ImagenTB set Imagen=? where IdImagen=? and IdRelacionado=?");
                preparedImagen.setBinaryStream(1, imagenTB.getFile());
                preparedImagen.setLong(2, imagenTB.getIdImage());
                preparedImagen.setString(3, imagenTB.getIdRelacionado());
                preparedImagen.addBatch();
                preparedImagen.executeBatch();
                DBUtil.getConnection().commit();
                return "update";
            } else {
                preparedImagen = DBUtil.getConnection().prepareCall("insert into ImagenTB(Imagen,IdRelacionado) values(?,?)");
                preparedImagen.setBinaryStream(1, imagenTB.getFile());
                preparedImagen.setString(2, imagenTB.getIdRelacionado());
                preparedImagen.addBatch();
                preparedImagen.executeBatch();
                DBUtil.getConnection().commit();
                return "insert";
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
                if (preparedImagen != null) {
                    preparedImagen.close();
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

    public static ImagenTB GetImage(String idRepresentante, boolean complete) {
        String selectStmt;
        if (complete) {
            selectStmt = "SELECT IdImagenArticulo,Imagen FROM ImagenArticuloTB WHERE IdRelacionado = ?";

        } else {
            selectStmt = "SELECT Imagen FROM ImagenArticuloTB WHERE IdRelacionado = ?";
        }
        PreparedStatement preparedStatement = null;
        ResultSet rsEmps = null;
        ImagenTB imagenTB = new ImagenTB();
        try {
            DBUtil.dbConnect();
            preparedStatement = DBUtil.getConnection().prepareStatement(selectStmt);
            preparedStatement.setString(1, idRepresentante);
            rsEmps = preparedStatement.executeQuery();

            while (rsEmps.next()) {
                if (complete) {
                    imagenTB.setIdImage(rsEmps.getLong("IdImagenArticulo"));
                }
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

    public static String CrudImageArticulo(ImagenTB imagenTB) {
        PreparedStatement preparedImagen = null;
        PreparedStatement preparedValidation = null;
        try {
            DBUtil.dbConnect();
            DBUtil.getConnection().setAutoCommit(false);
            preparedValidation = DBUtil.getConnection().prepareStatement("select IdImagenArticulo from ImagenArticuloTB where IdRelacionado=?");
            preparedValidation.setString(1, imagenTB.getIdRelacionado());
            if (preparedValidation.executeQuery().next()) {
                preparedValidation = DBUtil.getConnection().prepareStatement("select IdImagenArticulo from ImagenArticuloTB where IdImagenArticulo=? and IdRelacionado=?");
                preparedValidation.setLong(1, imagenTB.getIdImage());
                preparedValidation.setString(2, imagenTB.getIdRelacionado());
                if (preparedValidation.executeQuery().next()) {
                    preparedImagen = DBUtil.getConnection().prepareCall("update ImagenArticuloTB set Imagen=? where IdImagenArticulo=? and IdRelacionado=?");
                    preparedImagen.setBinaryStream(1, imagenTB.getFile());
                    preparedImagen.setLong(2, imagenTB.getIdImage());
                    preparedImagen.setString(3, imagenTB.getIdRelacionado());
                    preparedImagen.addBatch();
                    preparedImagen.executeBatch();
                    DBUtil.getConnection().commit();
                    return "update";
                } else {
                    preparedImagen = DBUtil.getConnection().prepareCall("insert into ImagenArticuloTB(Imagen,IdRelacionado) values(?,?)");
                    preparedImagen.setBinaryStream(1, imagenTB.getFile());
                    preparedImagen.setString(2, imagenTB.getIdRelacionado());
                    preparedImagen.addBatch();
                    preparedImagen.executeBatch();
                    DBUtil.getConnection().commit();
                    return "insert";
                }
            } else {
                preparedImagen = DBUtil.getConnection().prepareCall("insert into ImagenArticuloTB(Imagen,IdRelacionado) values(?,?)");
                preparedImagen.setBinaryStream(1, imagenTB.getFile());
                preparedImagen.setString(2, imagenTB.getIdRelacionado());
                preparedImagen.addBatch();
                preparedImagen.executeBatch();
                DBUtil.getConnection().commit();
                return "insert";
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
                if (preparedImagen != null) {
                    preparedImagen.close();
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

}
