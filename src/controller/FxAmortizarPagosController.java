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
    @FXML
    private Label lblPagado;
    @FXML
    private Label lblPendiente;
    @FXML
    private Label lblTotal;

    private FxHistorialPagosController historialPagosController;
    
    private PagoProveedoresTB pagoProveedoresTB;
    
    private double pagado;
    
    private double pendiente;
    
    private String simboloMoneda;
    
    private FxCompraDetalleController compraDetalleController;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);

    }

    public void setInitValues() {
        this.lblPagado.setText(simboloMoneda + " " + Tools.roundingValue(pagado, 2));
        pendiente = pagoProveedoresTB.getMontoTotal() - pagado;
        this.lblPendiente.setText(simboloMoneda + " " + Tools.roundingValue(pendiente, 2));
        this.lblTotal.setText(simboloMoneda + " " + Tools.roundingValue(pagoProveedoresTB.getMontoTotal(), 2));
    }

    private void registerPagosProveedores() {

        if (!Tools.isNumeric(txtValorCuota.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Amortizar Pagos", "Ingrese el monto de la cuota, por favor.", false);
            txtValorCuota.requestFocus();
        }else if(Double.parseDouble(txtValorCuota.getText())<=0){
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Amortizar Pagos", "El monto no puede ser 0.", false);
            txtValorCuota.requestFocus();
        } else {
            
            if( (pagado + Double.parseDouble(txtValorCuota.getText())) <= pagoProveedoresTB.getMontoTotal() ){
                pagoProveedoresTB.setMontoActual(Double.parseDouble(txtValorCuota.getText()));
                pagoProveedoresTB.setFechaActual(Timestamp.valueOf(LocalDateTime.now()));
                pagoProveedoresTB.setCuotaActual(pagoProveedoresTB.getCuotaActual() + 1);
                pagoProveedoresTB.setObservacion(txtObservacion.getText().isEmpty() ? "ninguno".toUpperCase() : txtObservacion.getText().toUpperCase());

                String result = PagoProveedoresADO.crudPagoProveedores(pagoProveedoresTB, pagado, pendiente);
                if (result.equalsIgnoreCase("register")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Pago Proveedores", "Se registró correctamente el pago.", false);
                    compraDetalleController.setLoadDetalle( pagoProveedoresTB.getIdCompra(), pagoProveedoresTB.getMontoTotal());
                    Tools.Dispose(window);
                    
                } else if (result.equalsIgnoreCase("update")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Pago Proveedores", "Se registró correctamente el pago y se actualizo el estado de la compra.", false);
                    compraDetalleController.setLoadDetalle( pagoProveedoresTB.getIdCompra(), pagoProveedoresTB.getMontoTotal());
                    Tools.Dispose(window);
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Pago Proveedores", result, false);
                }
                
            }else{
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Amortizar Pagos", "El monto a pagar mas el acumulado no debe exceder al total.", false);
                txtValorCuota.requestFocus();
            }
            
            
            
        }

    }
    
    private void validarValorCuota(){
        if(Tools.isNumeric(txtValorCuota.getText())){
            if((Double.parseDouble(txtValorCuota.getText())+pagado) <= pagoProveedoresTB.getMontoTotal()){
                this.lblPagado.setText(simboloMoneda + " " + Tools.roundingValue((pagado+Double.parseDouble(txtValorCuota.getText())), 2));
                this.lblPendiente.setText(simboloMoneda + " " + Tools.roundingValue((pagoProveedoresTB.getMontoTotal()-(pagado+Double.parseDouble(txtValorCuota.getText()))), 2));
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
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtValorCuota.getText().contains(".")) {
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
    
    @FXML
    private void onKeyReleasedValorCuota(KeyEvent event) {
        validarValorCuota();
    }

    public void setInitAmortizarPagosController(FxHistorialPagosController historialPagosController, PagoProveedoresTB pagoProveedoresTB, double pagado, String simboloMoneda, FxCompraDetalleController compraDetalleController) {
        this.historialPagosController = historialPagosController;
        this.pagoProveedoresTB = pagoProveedoresTB;
        this.pagado = pagado;
        this.simboloMoneda = simboloMoneda;
        this.compraDetalleController = compraDetalleController;
        
    }  

}
