package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.CuentasHistorialClienteADO;
import model.CuentasHistorialClienteTB;
import model.MovimientoCajaTB;

public class FxVentaAbonoProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtValorCuota;
    @FXML
    private TextField txtObservacion;

    private int idCuentasCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);

    }

    public void setInitLoadVentaAbono(int idCuentasCliente) {
        this.idCuentasCliente = idCuentasCliente;
    }

    private void saveAbono() {
        if (!Tools.isNumeric(txtValorCuota.getText().trim())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Abonar", "Ingrese el abono, por favor.", false);
            txtValorCuota.requestFocus();
        } else if (Double.parseDouble(txtValorCuota.getText().trim()) <= 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Abonar", "El abono no puede ser 0, por favor.", false);
            txtValorCuota.requestFocus();
        } else if (txtObservacion.getText().trim().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Abonar", "Ingrese una referencia del abono, por favor.", false);
            txtObservacion.requestFocus();
        } else {
            CuentasHistorialClienteTB historialClienteTB = new CuentasHistorialClienteTB();
            historialClienteTB.setIdCuentaClientes(idCuentasCliente);
            historialClienteTB.setAbono(Double.parseDouble(txtValorCuota.getText()));
            historialClienteTB.setFechaAbono(LocalDateTime.now());
            historialClienteTB.setReferencia(txtObservacion.getText().trim());

            MovimientoCajaTB movimientoCaja = new MovimientoCajaTB();
            movimientoCaja.setIdCaja(Session.CAJA_ID);
            movimientoCaja.setIdUsuario(Session.USER_ID);
            movimientoCaja.setFechaMovimiento(LocalDateTime.now());
            movimientoCaja.setComentario("Abono");
            movimientoCaja.setMovimiento("ABON");
            movimientoCaja.setEntrada(Double.parseDouble(txtValorCuota.getText()));
            movimientoCaja.setSalidas(0);
            movimientoCaja.setSaldo(Double.parseDouble(txtValorCuota.getText()));

            String result = CuentasHistorialClienteADO.Crud_CuentasHistorialCliente(historialClienteTB, movimientoCaja);
            if (result.equalsIgnoreCase("register")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Abonar", "Se registro correctamente el abono.", false);
                Tools.Dispose(window);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Abonar", result, false);
            }
        }
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        saveAbono();
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            saveAbono();
        }
    }

    @FXML
    private void onKeyTypedValorCuotas(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtValorCuota.getText().contains(".") || c == '-' && txtValorCuota.getText().contains("-")) {
            event.consume();
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

}
