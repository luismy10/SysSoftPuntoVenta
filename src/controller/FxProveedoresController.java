
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class FxProveedoresController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView imState;
    @FXML
    private Text lblEstado;
    @FXML
    private TextField txtSearch;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcId;
    @FXML
    private TableColumn<?, ?> tcDocument;
    @FXML
    private TableColumn<?, ?> tcDocumentType;
    @FXML
    private TableColumn<?, ?> tcBusinessName;
    @FXML
    private TableColumn<?, ?> tcState;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
    }    

    @FXML
    private void onActionSearch(ActionEvent event) {
    }
    
}
