
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.RepresentanteTB;


public class FxRepresentanteController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<RepresentanteTB> tvList;
    @FXML
    private TableColumn<RepresentanteTB, Integer> tcId;
    @FXML
    private TableColumn<RepresentanteTB, String> tcDocument;
    @FXML
    private TableColumn<RepresentanteTB, String> tcRepresentative;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }    

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
    }

    @FXML
    private void onActionToSearch(ActionEvent event) {
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
    }
    
}
