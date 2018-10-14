
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;


public class FxPrincipalController implements Initializable {

    @FXML
    private ScrollPane window;
    @FXML
    private Text lblFechaActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       lblFechaActual.setText(Tools.getDate("EEEE d 'de' MMMM 'de' yyyy")); 
    }    
    
}
