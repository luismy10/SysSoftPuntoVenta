package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class FxArticuloSeleccionadoController implements Initializable {

    @FXML
    private ImageView ivPrincipal;
    @FXML
    private Text lblName;
    @FXML
    private Text lblPrice;
    @FXML
    private Text lblQuantity;

    private FxArticulosController articuloController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public ImageView getIvPrincipal() {
        return ivPrincipal;
    }

    public Text getLblName() {
        return lblName;
    }

    public Text getLblPrice() {
        return lblPrice;
    }

    public Text getLblQuantity() {
        return lblQuantity;
    }

    @FXML
    private void onMouseClickedDetalle(MouseEvent event) {
        articuloController.changeViewArticuloDetalle();
        if (articuloController.getTvList().getSelectionModel().getSelectedIndex() >= 0) {
//            articuloController.onViewDetailArticulo();
        }
    }

    void setArticuloController(FxArticulosController articuloController) {
        this.articuloController = articuloController;
    }

}
