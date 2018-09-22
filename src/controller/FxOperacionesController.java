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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FxOperacionesController implements Initializable {

    @FXML
    private HBox window;
    @FXML
    private TextField txtSearch;

    private AnchorPane content;
    
    private AnchorPane windowinit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void openWindowCustomers() throws IOException {
//        URL url = getClass().getResource(Tools.FX_FILE_CLIENTE);
//        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
//        Parent parent = fXMLLoader.load(url.openStream());
//        //Controlller here
//        FxClienteController controller = fXMLLoader.getController();
//        //
//        Stage stage = FxWindow.StageLoaderModal(parent, "Clientes", window.getScene().getWindow(), Session.WIDTH_WINDOW, Session.HEIGHT_WINDOW);
//        stage.setMaximized(true);
//        stage.show();
//        controller.fillCustomersTable("");
        
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_CLIENTE));
        VBox node = fXMLPrincipal.load();
        FxClienteController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillCustomersTable("");
    }

    private void openWindowProviders() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_PROVEEDORES));
        VBox node = fXMLPrincipal.load();
        FxProveedoresController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillCustomersTable("");

    }

    private void openWindowDirectory() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_DIRECTORIO));
        VBox node = fXMLPrincipal.load();
        FxDirectorioController controller = fXMLPrincipal.getController();
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillEmployeeTable();

    }
    
    private void openWindowArticulos() throws IOException {
        FXMLLoader fXMLPrincipal = new FXMLLoader(getClass().getResource(Tools.FX_FILE_ARTICULO));
        VBox node = fXMLPrincipal.load();
        FxArticulosController controller = fXMLPrincipal.getController();
        controller.setContent(windowinit);
        content.getChildren().clear();
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        content.getChildren().add(node);
        controller.fillArticlesTable("");

    }

    @FXML
    private void onKeyPressedCustomers(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowCustomers();
        }
    }

    @FXML
    private void onActionCustomers(ActionEvent event) throws IOException {
        openWindowCustomers();
    }

    @FXML
    private void onKeyPressedProviders(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowProviders();
        }
    }

    @FXML
    private void onActionProviders(ActionEvent event) throws IOException {
        openWindowProviders();
    }

    @FXML
    private void onKeyPressedDirectory(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowDirectory();
        }
    }

    @FXML
    private void onActionDirectory(ActionEvent event) throws IOException {
        openWindowDirectory();
    }

    void setContent(AnchorPane windowinit,AnchorPane content) {
        this.windowinit=windowinit;
        this.content = content;
    }

    @FXML
    private void onKeyPressedArticulos(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.ENTER){
            openWindowArticulos();
        }
    }

    @FXML
    private void onActionArticulos(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

}
