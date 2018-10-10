package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FxConfiguracionController implements Initializable {

    @FXML
    private HBox window;

    private AnchorPane content;

    private AnchorPane windowinit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void openWindowTablasBasicas() throws IOException {

        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_DETALLE_MATENIMIENTO));
        VBox node = fXMLPrincipal.load();
        FxDetalleMantenimientoController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.initWindow();
    }

    private void openWindowMiEmpresa() throws IOException {

        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_MIEMPRESA));
        ScrollPane node = fXMLPrincipal.load();
        FxMiEmpresaController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
    }

    @FXML
    private void onKeyPressedTablasBasicas(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowTablasBasicas();
        }
    }

    @FXML
    private void onActionTablasBasicas(ActionEvent event) throws IOException {
        openWindowTablasBasicas();

    }

    @FXML
    private void onKeyPressedCompany(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowMiEmpresa();
        }
    }

    @FXML
    private void onActionCompany(ActionEvent event) throws IOException {
        openWindowMiEmpresa();
    }

    @FXML
    private void onKeyPressedPrivileges(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionPrivileges(ActionEvent event) {
    }

    void setContent(AnchorPane windowinit, AnchorPane content) {
        this.windowinit = windowinit;
        this.content = content;
    }

}
