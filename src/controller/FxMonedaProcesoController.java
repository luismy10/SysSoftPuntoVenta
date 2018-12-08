package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class FxMonedaProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtSimbolo;
    @FXML
    private TextField txtTipoCambio;

    private FxMonedaController monedaController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }

    public void setUpdateMoney() {

    }

    private void processTheForm() {
        if (txtNombre.getText().trim().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "El nombre no puede estar vacío", false);
            txtNombre.requestFocus();
        } else if (txtSimbolo.getText().trim().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "El simbolo no puede estar vacío", false);
            txtSimbolo.requestFocus();
        } else if (!Tools.isNumeric(txtTipoCambio.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "El tipo de cambio no puede estar vacío", false);
            txtTipoCambio.requestFocus();
        } else {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionGuardar(ActionEvent event) {
        processTheForm();
    }

    @FXML
    private void onKeyPressedGuardar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            processTheForm();
        }
    }

    @FXML
    private void onKeyTypedTipoCambio(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtTipoCambio.getText().contains(".")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    public void setInitMoneyController(FxMonedaController monedaController) {
        this.monedaController = monedaController;
    }

}
