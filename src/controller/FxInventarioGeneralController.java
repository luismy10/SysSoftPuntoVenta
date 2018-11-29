
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class FxInventarioGeneralController implements Initializable {

    @FXML
    private Label lblLoad;
    @FXML
    private VBox vbCambios;
    
    private AnchorPane content;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    

    @FXML
    private void onActionView(ActionEvent event) {
        
    }
    
    public void setContent(AnchorPane content) {
        this.content = content;
    }
}
