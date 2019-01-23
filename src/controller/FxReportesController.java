package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private FXMLLoader fXMLPrincipal;

    private VBox node;

    private FxArticuloReportesController controller;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_ARTICULOREPORTES));
            node = fXMLPrincipal.load();
            controller = fXMLPrincipal.getController();
        } catch (IOException ex) {
            Logger.getLogger(FxReportesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openWindowReportes() throws IOException {
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
