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
import model.CajaTB;
import model.MovimientoCajaADO;
import model.MovimientoCajaTB;

public class FxVentaFondoInicialController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtImporte;

    private FxVentaController ventaController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
    }

    private void eventAceptar() {
        if (Tools.isNumeric(txtImporte.getText().trim())) {

            CajaTB cajaTB = new CajaTB();
            cajaTB.setFechaApertura(LocalDateTime.now());
            cajaTB.setIdCaja(Session.CAJA_ID);

            MovimientoCajaTB movimientoCaja = new MovimientoCajaTB();
            movimientoCaja.setIdCaja(Session.CAJA_ID);
            movimientoCaja.setIdUsuario(Session.USER_ID);
            movimientoCaja.setFechaMovimiento(LocalDateTime.now());
            movimientoCaja.setComentario("Apertura de caja");
            movimientoCaja.setMovimiento("FONC");
            movimientoCaja.setEntrada(Double.parseDouble(txtImporte.getText()));
            movimientoCaja.setSalidas(0);
            movimientoCaja.setSaldo(Double.parseDouble(txtImporte.getText()));

            String result = MovimientoCajaADO.AperturarCaja_Movimiento(cajaTB, movimientoCaja);
            if (result.equalsIgnoreCase("registrado")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Ventas", "Se aperturo correctamento la caja.", false);
                ventaController.setAperturaCaja(true);
                Tools.Dispose(window);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Ventas", result, false);
            }
        }
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        eventAceptar();
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            eventAceptar();
        }
    }

    @FXML
    private void onKeyTypedImporte(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtImporte.getText().contains(".") || c == '-' && txtImporte.getText().contains("-")) {
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

    public void setInitVentaController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

}
