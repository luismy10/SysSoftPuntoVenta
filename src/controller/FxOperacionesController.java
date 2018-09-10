package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FxOperacionesController implements Initializable {

    @FXML
    private HBox window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void openWindowCustomers() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_CLIENTE);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxClienteController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Clientes", window.getScene().getWindow());
        stage.setMaximized(true);
        stage.show();
        controller.fillCustomersTable("");
    }

    private void openWindowProviders() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PROVEEDORES);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxProveedoresController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Proveedores", window.getScene().getWindow());
        stage.setMaximized(true);
        stage.show();
//        controller.fillCustomersTable("");
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

}
