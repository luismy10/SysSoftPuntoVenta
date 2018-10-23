package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class FxMenuController implements Initializable {

    @FXML
    private HBox btnInicio;
    @FXML
    private ImageView ivImagen;
    @FXML
    private Text lblNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public ImageView getIvImagen() {
        return ivImagen;
    }

    public Text getLblNombre() {
        return lblNombre;
    }

}
