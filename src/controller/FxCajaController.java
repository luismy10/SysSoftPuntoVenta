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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.CajaADO;
import model.CajaTB;

/**
 * FXML Controller class
 *
 * @author Ruberfc
 */
public class FxCajaController implements Initializable {

    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtMonto;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnCancel;
    @FXML
    private DatePicker dtpFecha;
    
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }

    @FXML
    private void onKeyPressedRegister(KeyEvent event) {
    }

    @FXML
    private void onActionRegister(ActionEvent event) {
    }

    @FXML
    private void onActionCancel(ActionEvent event) {
    }

    private void cargarDatos(){
        
        CajaTB cajaTB = new CajaTB();
        
        //IdCajaTrabajador,MontoInicial,MontoFinal,Estado,Fecha
       
        cajaTB.setIdCajaTrabajador(1);
        cajaTB.setMontoInicial(100);
        cajaTB.setMontoFinal(150);
        cajaTB.setEstado("Activo");
        cajaTB.setFecha(Timestamp.valueOf(Tools.getDatePicker(dtpFecha) + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
        
        CajaADO.CrudInsertar(cajaTB);
    
    }

}
