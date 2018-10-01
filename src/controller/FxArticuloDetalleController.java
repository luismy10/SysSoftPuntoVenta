package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class FxArticuloDetalleController implements Initializable {

    private FxArticulosController articuloController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void onMouseClickedSeleccionado(MouseEvent event) {
        articuloController.changeViewArticuloSeleccionado();
    }

    void setArticuloController(FxArticulosController articuloController) {
        this.articuloController = articuloController;
    }

}
