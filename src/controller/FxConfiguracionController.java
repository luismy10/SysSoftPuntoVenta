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

public class FxConfiguracionController implements Initializable {

    @FXML
    private HBox window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void openWindowTablasBasicas() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_DETALLE_MATENIMIENTO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxDetalleMantenimientoController controller = fXMLLoader.getController();        
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Mantenimiento de tablas básicas", window.getScene().getWindow());
        stage.setMaximized(true);
        stage.show();
        controller.initWindow();
    }
    
    private void openWindowMiEmpresa() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_MIEMPRESA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
//        FxDetalleMantenimientoController controller = fXMLLoader.getController();        
        //
        Stage stage = FxWindow.StageLoader(parent, "Mi Empresa", window.getScene().getWindow());
       
        stage.show();
//        controller.initWindow();
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
            openWindowMiEmpresa() ;
        }
    }

    @FXML
    private void onActionCompany(ActionEvent event) throws IOException {
        openWindowMiEmpresa() ;
    }

    @FXML
    private void onKeyPressedPrivileges(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            
        }
    }

    @FXML
    private void onActionPrivileges(ActionEvent event) {
    }


}
