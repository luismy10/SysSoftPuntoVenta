package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.CajaADO;
import model.EmpleadoADO;
import model.EmpleadoTB;

public class FxLoginController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Label lblError;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void onActionContactenos(ActionEvent event) {

    }

    @FXML
    private void onActionEntrar(ActionEvent event) throws IOException, SQLException {
        if (Tools.isText(txtUsuario.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Iniciar Sesión", "Ingrese su usuario", false);
            txtUsuario.requestFocus();
        } else if (Tools.isText(txtClave.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Iniciar Sesión", "Ingrese su contraseña", false);
            txtClave.requestFocus();
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            ExecutorCompletionService<EmpleadoTB> completionService = new ExecutorCompletionService<>(executor);
            completionService.submit(EmpleadoADO.GetValidateUser(txtUsuario.getText().trim(), txtClave.getText().trim()));
            try {
                Future<EmpleadoTB> batchCount = completionService.take();
                if (batchCount != null) {
                    EmpleadoTB empleadoTB = batchCount.get();
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
                        Session.USER_NAME = (empleadoTB.getApellidos().substring(0, 1).toUpperCase() + empleadoTB.getApellidos().substring(1).toLowerCase())
                                + " " + empleadoTB.getNombres().substring(0, 1).toUpperCase() + empleadoTB.getNombres().substring(1).toLowerCase();
                        Session.USER_NAME_PUESTO = empleadoTB.getPuestoName();

                        controller.initUserSession((empleadoTB.getPuestoName().substring(0, 1).toUpperCase() + empleadoTB.getPuestoName().substring(1).toLowerCase()));

                        Session.WIDTH_WINDOW = scene.getWidth();
                        Session.HEIGHT_WINDOW = scene.getHeight();

                        if (!Session.USER_NAME_PUESTO.equalsIgnoreCase("administrador")) {
                            if (!estadoCaja().equalsIgnoreCase("activo")) {
                                //System.out.println(estadoCaja());
                                this.openWindowCaja(stage);
                            } else {
                                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Iniciar Sesión", "Ya existe una caja aperturada para el usuario actual", false);
                            }
                        }
                    } else {
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Iniciar Sesión", "Datos incorrectos", false);
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                lblError.setText("Error: No se pudo conectar al servidor revise su conexión.");
            } finally {
                executor.shutdown();
            }

        }
    }

    public void initComponents() {
        txtUsuario.requestFocus();
    }

    private void openWindowCaja(Window window) throws IOException {

        URL url = getClass().getResource(Tools.FX_FILE_APERTURACAJA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxAperturaCajaController controller = fXMLLoader.getController();
        controller.setInitCajaController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Caja", window);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        controller.initComponents();
    }

    private String estadoCaja() {
        return CajaADO.consultarEstadoCaja("activo".toUpperCase(), Session.USER_ID);
    }

}
