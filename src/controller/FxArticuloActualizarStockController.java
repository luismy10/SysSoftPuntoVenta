package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.ArticuloADO;

public class FxArticuloActualizarStockController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblArticulo;
    @FXML
    private TextField txtExistencia;

    private FxArticulosController articulosController;

    private String idArticulo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }

    public void loadArticuloStock(String idArticulo, String nombre) {
        this.idArticulo = idArticulo;
        lblArticulo.setText(nombre);
    }

    private void updateExistenciaArticulo() {
        if (txtExistencia.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "Ingrese una cantidad mayor a 0 para el ajuste de inventario", false);
            txtExistencia.requestFocus();
        } else if (!Tools.isNumeric(txtExistencia.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "El valor ingresado no tiene el formato de un número", false);
            txtExistencia.requestFocus();
        } else {
            short validate = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Artículo", "¿Está seguro de continuar?", true);
            if (validate == 1) {
                String result = ArticuloADO.AjusteInventarioByIid(idArticulo, Double.parseDouble(txtExistencia.getText()));
                if (result.equalsIgnoreCase("updated")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Artículo", "Se aplico los cambios correctamente", false);
                    Tools.Dispose(window);
                    articulosController.getTxtSearch().requestFocus();
                    articulosController.getTxtSearch().selectAll();
                    articulosController.fillArticlesTable(articulosController.getTxtSearch().getText());
                } else if (result.equalsIgnoreCase("inventario")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Artículo", "El artículo no tiene la opción de inventario", false);
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Artículo", result, false);
                }
            }
        }
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        updateExistenciaArticulo();
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            updateExistenciaArticulo();
        }
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    @FXML
    private void onKeyTypedExistencia(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtExistencia.getText().contains(".")) {
            event.consume();
        }
    }

    public void setInitArticuloController(FxArticulosController articulosController) {
        this.articulosController = articulosController;
    }

}
