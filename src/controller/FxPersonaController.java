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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DBUtil;
import model.DetalleADO;
import model.DetalleTB;
import model.PersonaADO;
import model.PersonaTB;

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

    private long idPersona;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idPersona = 0;
        if (DBUtil.StateConnection()) {
            try {
                Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
                DetalleADO.GetDetailIdName("0003").forEach(e -> {
                    cbDocumentType.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
                });
                DetalleADO.GetDetailIdName("0004").forEach(e -> {
                    cbSex.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
                });

            } catch (SQLException | ClassNotFoundException e) {
                System.out.println("La operación de selección de SQL ha fallado: " + e);
            }
        }

    }

    private void onViewPerfil() throws IOException {
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

    void aValidityProcess() {
        if (cbDocumentType.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "Seleccione el tipo de documento, por favor.", false);

            cbDocumentType.requestFocus();
        } else if (txtDocumentNumber.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "Ingrese el documento de identificación, por favor.", false);

            txtDocumentNumber.requestFocus();
        } else if (txtLastName.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "Ingrese el apellido paterno, por favor.", false);

            txtLastName.requestFocus();
        } else if (txtMotherLastName.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "Ingrese el apellido materno, por favor.", false);

            txtMotherLastName.requestFocus();
        } else if (txtFirstName.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "Ingrese el primero nombre, por favor.", false);

            txtFirstName.requestFocus();
        } else {
            if (DBUtil.StateConnection()) {
                short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Mantenimiento", "¿Esta seguro de continuar?", true);
                if (confirmation == 1) {
                    PersonaTB personaTB = new PersonaTB();
                    personaTB.setIdPersona(idPersona);
                    personaTB.setTipoDocumento(cbDocumentType.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    personaTB.setNumeroDocumento(txtDocumentNumber.getText().trim());
                    personaTB.setApellidoPaterno(txtLastName.getText().trim());
                    personaTB.setApellidoMaterno(txtMotherLastName.getText().trim());
                    personaTB.setPrimerNombre(txtFirstName.getText().trim());
                    personaTB.setSegundoNombre(txtSecondName.getText().trim());
                    String result = PersonaADO.CrudEntity(personaTB);
                    switch (result) {
                        case "registered":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Persona", "Registrado correctamente.", false);
                            break;
                        case "updated":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Persona", "Actualizado correctamente.", false);

                            break;
                        case "duplicate":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "No se puede haber 2 personas con el mismo documento de identidad.", false);
                            txtDocumentNumber.requestFocus();
                            break;
                        case "error":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "No se puedo completar la ejecución.", false);

                            break;
                        default:
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Persona", result, false);

                            break;
                    }
                }
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Persona", "No hay conexión al servidor.", false);

            }
        }
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            aValidityProcess();
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {
        aValidityProcess();
    }

    @FXML
    private void onKeyPressedToCancel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionToCancel(ActionEvent event) {
        Tools.Dispose(window);
    }

}
