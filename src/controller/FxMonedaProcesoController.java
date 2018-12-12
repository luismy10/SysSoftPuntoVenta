package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.MonedaADO;
import model.MonedaTB;

public class FxMonedaProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtSimbolo;
    @FXML
    private TextField txtTipoCambio;
    @FXML
    private Button btnGuardar;
    
    private FxMonedaController monedaController;

    private int idMoneda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        idMoneda = 0;
    }

    public void setUpdateMoney(int idMoneda,String nombre,String simbolo,double tcambio) {
        this.idMoneda = idMoneda;
        btnGuardar.setText("Actualizar");
        btnGuardar.getStyleClass().add("buttonLightWarning");
        txtNombre.setText(nombre);
        txtSimbolo.setText(simbolo);
        txtTipoCambio.setText(""+tcambio);
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
            MonedaTB monedaTB = new MonedaTB();
            monedaTB.setIdMoneda(idMoneda);
            monedaTB.setNombre(txtNombre.getText().trim().toUpperCase());
            monedaTB.setAbreviado(txtNombre.getText().trim().length() >= 3
                    ? txtNombre.getText().trim().substring(0, 3).toUpperCase()
                    : txtNombre.getText().trim().toUpperCase());
            monedaTB.setSimbolo(txtSimbolo.getText().trim().toUpperCase());
            monedaTB.setTipoCambio(Double.parseDouble(txtTipoCambio.getText().trim()));
            monedaTB.setPredeterminado(false);
            String result = MonedaADO.CrudMoneda(monedaTB);
            if (result.equalsIgnoreCase("inserted")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Moneda", "Se ingreso correctamente.", false);
                Tools.Dispose(window);
                monedaController.fillTableMonedas();
            } else if (result.equalsIgnoreCase("updated")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Moneda", "Se actualizo correctamente.", false);
                Tools.Dispose(window);
                monedaController.fillTableMonedas();
            } else if (result.equalsIgnoreCase("duplicated")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Moneda", "Hay una moneda con el mismo nombre.", false);
                txtNombre.requestFocus();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Moneda", result, false);
            }
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
