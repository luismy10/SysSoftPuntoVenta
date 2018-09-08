
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class FxOperacionesController implements Initializable {

    @FXML
    private HBox window;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void openWindowCustomers() throws IOException{
        URL url = getClass().getResource(Tools.FX_FILE_CLIENTE);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Clientes", window.getScene().getWindow());
        stage.setMaximized(true);
        stage.show();
    }
    
    @FXML
    private void onKeyPressedCustomers(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.ENTER){
            openWindowCustomers();
        }
    }

    @FXML
    private void onActionCustomers(ActionEvent event) throws IOException {
        openWindowCustomers();
    }
    
}
