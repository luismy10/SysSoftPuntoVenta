
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class FxLoteController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<?> tvList;
    
    private AnchorPane content;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    void setContent(AnchorPane content) {
        this.content=content;
    }
    
}
