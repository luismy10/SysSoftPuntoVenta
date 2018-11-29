package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class FxVentaRealizadasController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<?> tvList;
    @FXML
    private TableColumn<?, ?> tcId;
    @FXML
    private TableColumn<?, ?> tcTotal;
    @FXML
    private TableColumn<?, ?> tcFechaVenta;
    @FXML
    private TableColumn<?, ?> Cliente;
    @FXML
    private TableColumn<?, ?> tcEstado;
    @FXML
    private TableColumn<?, ?> tcObservacion;
    
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

}
