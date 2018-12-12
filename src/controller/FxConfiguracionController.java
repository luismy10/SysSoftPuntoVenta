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
        controller.reloadListView();
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

    private void openWindowRoles() throws IOException {

        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_ROLES));
        VBox node = fXMLPrincipal.load();
        FxRolesController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
    }

    private void openWindowEmpleados() throws IOException {

        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_EMPLEADOS));
        VBox node = fXMLPrincipal.load();
        FxEmpleadosController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillEmpleadosTable("");
    }

    private void openWindowMoney() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_MONEDA));
        VBox node = fXMLPrincipal.load();
        FxMonedaController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillTableMonedas();
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
    private void onKeyPressedPrivileges(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowRoles();
        }
    }

    @FXML
    private void onActionPrivileges(ActionEvent event) throws IOException {
        openWindowRoles();
    }

    @FXML
    private void onKeyPressedEmployes(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowEmpleados();
        }
    }

    @FXML
    private void onActionEmployes(ActionEvent event) throws IOException {
        openWindowEmpleados();
    }

    @FXML
    private void onKeyPressedMoney(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowMoney();
        }
    }

    @FXML
    private void onActionMoney(ActionEvent event) throws IOException {
        openWindowMoney();
    }

    public void setContent(AnchorPane windowinit, AnchorPane content) {
        this.windowinit = windowinit;
        this.content = content;
    }

}
