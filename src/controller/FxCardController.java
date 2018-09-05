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
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class FxCardController implements Initializable {

    @FXML
    private Label lblSubTitle;
    @FXML
    private Text lblTitle;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public Label getLblSubTitle() {
        return lblSubTitle;
    }

    public Text getLblTitle() {
        return lblTitle;
    }
    
    
    
}
