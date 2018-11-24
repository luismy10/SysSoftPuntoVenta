/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class FxArticuloActualizarStockController implements Initializable {

    @FXML
    private VBox window;
    private FxArticulosController articulosController;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void initComponents(){
               Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.NONE, "Actualizar stock artículo","Vista iniciada", false);

    }

    public void setInitArticuloUpdateStock(FxArticulosController articulosController) {
        this.articulosController = articulosController;
    }
    
    
    
    
    
}
