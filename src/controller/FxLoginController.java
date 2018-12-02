package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.EmpleadoADO;
import model.EmpleadoTB;

public class FxLoginController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void onActionContactenos(ActionEvent event) {
        
    }

    @FXML
    private void onActionEntrar(ActionEvent event) throws IOException {
        if (Tools.isText(txtUsuario.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Iniciar Sesión", "Ingrese su usuario", false);
            txtUsuario.requestFocus();
        } else if (Tools.isText(txtClave.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Iniciar Sesión", "Ingrese su contraseña", false);
            txtClave.requestFocus();
        } else {
            EmpleadoTB empleadoTB = EmpleadoADO.GetValidateUser(txtUsuario.getText().trim(), txtClave.getText().trim());
            if (empleadoTB != null) {
                Session.ROL = empleadoTB.getRol();
                Tools.Dispose(window);
                URL url = getClass().getResource(Tools.FX_FILE_INICIO);
                FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
                Parent parent = fXMLLoader.load(url.openStream());
                //Controlller here
                FxInicioController controller = fXMLLoader.getController();
                //
                Stage stage = new Stage();
                Scene scene = new Scene(parent);
                stage.getIcons().add(new Image(Tools.FX_LOGO));
                stage.setScene(scene);
                stage.setTitle("Sys Soft");
                stage.centerOnScreen();
                stage.setMaximized(true);
                stage.show();
                stage.requestFocus();                
                controller.initInicioController();
                controller.initWindowSize();
                Session.USER_ID = empleadoTB.getIdEmpleado();
                Session.USER_NAME = (empleadoTB.getApellidos().substring(0, 1).toUpperCase()+empleadoTB.getApellidos().substring(1).toLowerCase()) 
                        + " " + empleadoTB.getNombres().substring(0, 1).toUpperCase()+empleadoTB.getNombres().substring(1).toLowerCase();
                controller.initUserSession((
                        empleadoTB.getPuestoName().substring(0, 1).toUpperCase()+empleadoTB.getPuestoName().substring(1).toLowerCase()));

                Session.WIDTH_WINDOW = scene.getWidth();
                Session.HEIGHT_WINDOW = scene.getHeight();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Iniciar Sesión", "Datos incorrectos", false);

            }
        }

    }

    public void initComponents() {
        txtUsuario.requestFocus();
    }

}
