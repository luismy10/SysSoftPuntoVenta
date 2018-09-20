
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class FxArticulosController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcId;
    @FXML
    private TableColumn<?, ?> tcDocument;
    @FXML
    private TableColumn<?, ?> tcBusinessName;
    @FXML
    private TableColumn<?, ?> tcState;
    @FXML
    private TableColumn<?, ?> tcFechaRegistro;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void onActionAdd(ActionEvent event) {
    }

    @FXML
    private void onActionEdit(ActionEvent event) {
    }

    @FXML
    private void onActionReload(ActionEvent event) {
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
    }
    
}
