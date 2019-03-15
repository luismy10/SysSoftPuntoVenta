/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.CajaADO;
import model.CajaTB;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxAperturaCajaController implements Initializable {
    
    @FXML
    private AnchorPane window;
    @FXML
    private Label lblUser;
    @FXML
    private TextField txtMonto;
    @FXML
    private Label lblFecha;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnCancel;

    private FxLoginController fxLoginController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void initComponents(){
        this.lblUser.setText(Session.USER_NAME.toString());
        this.txtMonto.requestFocus();
        this.lblFecha.setText(Tools.getDate().toString());

    }
    
    private String aperturarCaja(){
        
        CajaTB cajaTB = new CajaTB();
        
        cajaTB.setMontoInicial(Double.parseDouble(this.txtMonto.getText()));
        cajaTB.setMontoFinal(0.00);
        cajaTB.setIngresos(0.00);
        cajaTB.setEgresos(0.00);
        cajaTB.setDevoluciones(0.00);
        cajaTB.setEntradas(0.00);
        cajaTB.setSalidas(0.00);
        cajaTB.setFechaApertura(Timestamp.valueOf(Tools.getDate() + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
        cajaTB.setFechaCierre(Timestamp.valueOf(Tools.getDate() + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
        cajaTB.setEstado("ACTIVO".toUpperCase());
        cajaTB.setIdEmpleado(Session.USER_ID);
        return CajaADO.CrudInsertar(cajaTB);
        
    }

    @FXML
    private void onActionRegister(ActionEvent event) {
        if (txtMonto.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Caja", "Ingrese el monto, por favor.", false);
            txtMonto.requestFocus();
        }
        else{
            String mensaje = this.aperturarCaja();
            Tools.Dispose(window);
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Caja", mensaje, false);
        }
    }
    
    @FXML
    private void onKeyPressedRegister(KeyEvent event) {
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
    }
    
    @FXML
    private void onKeyPressedCancel(KeyEvent event) {
    }
    
    public void setInitCajaController(FxLoginController fxLoginController) {
        this.fxLoginController = fxLoginController;
    }
    
}
