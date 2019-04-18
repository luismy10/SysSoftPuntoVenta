package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CajaADO;

public class FxCajaCerrarCajaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtEfectivo;

    private int idActual;

    private AnchorPane content;
    
    private double calculado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void eventAceptar() throws IOException {
        if (!Tools.isNumeric(txtEfectivo.getText())) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Corte de caja", "Ingrese el monto actual de caja.", false);
            txtEfectivo.requestFocus();
        } else {
            short option = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Corte de caja", "¿Está seguro de cerrar su turno?", true);
            if (option == 1) {
                String result = CajaADO.CerarAperturaCaja(idActual, LocalDateTime.now(), false, Double.parseDouble(txtEfectivo.getText()), calculado);
                if (result.equalsIgnoreCase("completed")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Corte de caja", "Se cerro correctamente la caja.", false);
                    Tools.Dispose(content);
                    URL urllogin = getClass().getResource(Tools.FX_FILE_LOGIN);
                    FXMLLoader fXMLLoaderLogin = FxWindow.LoaderWindow(urllogin);
                    Parent parent = fXMLLoaderLogin.load(urllogin.openStream());
                    Scene scene = new Scene(parent);
                    Stage primaryStage = new Stage();
                    primaryStage.getIcons().add(new Image(Tools.FX_LOGO));
                    primaryStage.setScene(scene);
                    primaryStage.initStyle(StageStyle.DECORATED);
                    primaryStage.setTitle("Sys Soft");
                    primaryStage.centerOnScreen();
                    primaryStage.setMaximized(true);
                    primaryStage.show();
                    primaryStage.requestFocus();
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Corte de caja", result, false);
                }
            }
        }

    }

    @FXML
    private void onActionAceptar(ActionEvent event) throws IOException {
        eventAceptar();
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            eventAceptar(); 
        }
    }

    @FXML
    private void onKeyTypedEfectivo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtEfectivo.getText().contains(".")) {
            event.consume();
        }
    }

    public void setInitCajaController(int idActual, double calculado,AnchorPane content) {
        this.idActual = idActual;
        this.calculado=calculado;
        this.content = content;
    }

}
