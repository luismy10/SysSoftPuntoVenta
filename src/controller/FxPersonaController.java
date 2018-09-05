package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DetalleADO;
import model.DetalleTB;

public class FxPersonaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ComboBox<DetalleTB> cbDocumentType;
    @FXML
    private TextField txtDocumentNumber;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtMotherLastName;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtSecondName;
    @FXML
    private ComboBox<DetalleTB> cbSex;
    @FXML
    private DatePicker dpBirthdate;
    @FXML
    private Button btnDirectory;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
            DetalleADO.GetDetailIdName("0003").forEach(e -> {
                    cbDocumentType.getItems().add(new DetalleTB(e.getIdDetalle(),e.getNombre()));
            });
            DetalleADO.GetDetailIdName("0004").forEach(e -> {
                    cbSex.getItems().add(new DetalleTB(e.getIdDetalle(),e.getNombre()));
            });
            
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("La operación de selección de SQL ha fallado: " + e);
        }

    }
    
    private void onViewPerfil() throws IOException{
        URL url = getClass().getResource(Tools.FX_FILE_PERFIL);
            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
            Parent parent = fXMLLoader.load(url.openStream());
            //Controlller here
            //
            Stage stage = FxWindow.StageLoaderModal(parent, "Perfil", window.getScene().getWindow());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(true);
            stage.show();
    }

    @FXML
    private void onKeyPressedToDirectory(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToDirectory(ActionEvent event) throws IOException {
        onViewPerfil();
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToCancel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToCancel(ActionEvent event) {

    }

}
