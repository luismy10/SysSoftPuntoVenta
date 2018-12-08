package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class FxReportesController implements Initializable {

    @FXML
    private TextField txtSearch;

    private AnchorPane windowinit;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void openWindowReportes() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_ARTICULOREPORTES));
        VBox node = fXMLPrincipal.load();
        FxArticuloReportesController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);       
    }

    @FXML
    private void onKeyPressedArticulos(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowReportes();
        }
    }

    @FXML
    private void onActionArticulos(ActionEvent event) throws IOException {
        openWindowReportes();
    }

    public void setContent(AnchorPane windowinit, AnchorPane content) {
        this.windowinit = windowinit;
        this.content = content;
    }
}
