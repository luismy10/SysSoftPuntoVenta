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

public class FxConsultasController implements Initializable {

    @FXML
    private TextField txtSearch;

    private AnchorPane content;

    private AnchorPane windowinit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void openWindowPurchasesMade() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_COMPRASREALIZADAS));
        VBox node = fXMLPrincipal.load();
        FxComprasRealizadasController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillPurchasesTable("");

    }

    @FXML
    private void onKeyPressedCompras(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowPurchasesMade();
        }
    }

    @FXML
    private void onActionCompras(ActionEvent event) throws IOException {
        openWindowPurchasesMade();
    }

    void setContent(AnchorPane windowinit, AnchorPane content) {
        this.windowinit = windowinit;
        this.content = content;
    }

}
