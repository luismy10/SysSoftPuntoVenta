/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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
import model.PagoProveedoresTB;
import model.PagoProveedoresADO;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxAmortizarPagosController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtValorCuota;
    @FXML
    private TextField txtObservacion;

    private FxHistorialPagosController historialPagosController;
    
    private PagoProveedoresTB pagoProveedoresTB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setInitTexField() {
        txtValorCuota.setText( String.valueOf(pagoProveedoresTB.getValorCuota()));
    }

    private void registerPagosProveedores() {

        if (!Tools.isNumeric(txtValorCuota.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Amortizar Pagos", "Ingrese el monto de la cuota, por favor.", false);
            txtValorCuota.requestFocus();
        } else {
            
            pagoProveedoresTB.setMontoActual( pagoProveedoresTB.getMontoActual() + Double.parseDouble(txtValorCuota.getText()));
            pagoProveedoresTB.setCuotaActual( (pagoProveedoresTB.getCuotaActual() < pagoProveedoresTB.getCuotaTotal()) ? pagoProveedoresTB.getCuotaActual()+1 : pagoProveedoresTB.getCuotaTotal() );
            pagoProveedoresTB.setFechaActual(Timestamp.valueOf(LocalDateTime.now()));
            pagoProveedoresTB.setObservacion(txtObservacion.getText().isEmpty() ? "ninguno".toUpperCase() : txtObservacion.getText());
            pagoProveedoresTB.setEstado("activo".toUpperCase());

            String result = PagoProveedoresADO.crudPagoProveedores(pagoProveedoresTB);
            if (result.equalsIgnoreCase("register")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Pago Proveedores", "Se registró correctamente el pago.", false);
                Tools.Dispose(window);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Pago Proveedores", result, false);
            }
            
        }

    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        registerPagosProveedores();
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            registerPagosProveedores();
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

    public void setInitAmortizarPagosController(FxHistorialPagosController historialPagosController, PagoProveedoresTB pagoProveedoresTB) {
        this.historialPagosController = historialPagosController;
        this.pagoProveedoresTB = pagoProveedoresTB;
        
    }

}
