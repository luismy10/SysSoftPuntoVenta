package controller;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

    private String idPersona;

    private String information;
    @FXML
    private Label lblDirectory;
    @FXML
    private Button btnDesglosar;
    @FXML
    private ComboBox<DetalleTB> cbDocumentTypeFactura;
    @FXML
    private TextField txtDocumentNumberFactura;
    @FXML
    private TextField txtBusinessName;
    @FXML
    private TextField txtTradename;
    @FXML
    private ComboBox<DetalleTB> cbEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        idPersona = "";
        if (DBUtil.StateConnection()) {
            DetalleADO.GetDetailIdName("1", "0003", "RUC").forEach(e -> {
                cbDocumentType.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
            DetalleADO.GetDetailId("0004").forEach(e -> {
                cbSex.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
            DetalleADO.GetDetailIdName("0", "0003", "RUC").forEach(e -> {
                cbDocumentTypeFactura.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
            DetalleADO.GetDetailIdName("2", "0001", "").forEach(e -> {
                cbEstado.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
        }

    }

    public void setValueAdd() {
        lblDirectory.setVisible(false);
        btnDirectory.setVisible(false);
    }

    public void setValueUpdate(String... value) {
        btnDesglosar.setVisible(false);
        btnRegister.setText("Actualizar");
        btnRegister.getStyleClass().add("buttonLightWarning");
        ArrayList<PersonaTB> arrayList = PersonaADO.GetIdListPersona(value[0]);

        if (!arrayList.isEmpty()) {
            PersonaTB list = arrayList.get(0);
            idPersona = list.getIdPersona().get();
            ObservableList<DetalleTB> lstype = cbDocumentType.getItems();
            for (int i = 0; i < lstype.size(); i++) {
                if (list.getTipoDocumento() == lstype.get(i).getIdDetalle().get()) {
                    cbDocumentType.getSelectionModel().select(i);
                    break;
                }
            }
            txtDocumentNumber.setText(list.getNumeroDocumento().get());
            txtLastName.setText(list.getApellidoPaterno().get());
            txtMotherLastName.setText(list.getApellidoMaterno());
            txtFirstName.setText(list.getPrimerNombre());
            txtSecondName.setText(list.getSegundoNombre());
            information = list.getApellidoPaterno().get() + " " + list.getApellidoMaterno() + " " + list.getPrimerNombre() + " " + list.getSegundoNombre();

            ObservableList<DetalleTB> lssex = cbSex.getItems();
            for (int i = 0; i < lssex.size(); i++) {
                if (list.getSexo() == lssex.get(i).getIdDetalle().get()) {
                    cbSex.getSelectionModel().select(i);
                    break;
                }
            }

            ObservableList<DetalleTB> lsest = cbEstado.getItems();
            for (int i = 0; i < lsest.size(); i++) {
                if (list.getEstado() == lsest.get(i).getIdDetalle().get()) {
                    cbEstado.getSelectionModel().select(i);
                    break;
                }
            }

            ObservableList<DetalleTB> lstypefacturacion = cbDocumentTypeFactura.getItems();
            for (int i = 0; i < lstypefacturacion.size(); i++) {
                if (list.getTipoDocumentoFacturacion() == lstypefacturacion.get(i).getIdDetalle().get()) {
                    cbDocumentTypeFactura.getSelectionModel().select(i);
                    break;
                }
            }
            txtDocumentNumberFactura.setText(list.getNumeroDocumentoFacturacion());
            txtBusinessName.setText(list.getRazonSocial());
            txtTradename.setText(list.getNombreComercial());
        }

    }

    private void onViewPerfil() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PERFIL);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxPerfilController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Perfil", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        controller.setLoadView(idPersona, information);
    }

    @FXML
    private void onKeyPressedToDirectory(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewPerfil();
        }
    }

    @FXML
    private void onActionToDirectory(ActionEvent event) throws IOException {
        onViewPerfil();
    }

    void aValidityProcess() throws ParseException {
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
        } else if (cbEstado.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "Seleccione el estado, por favor.", false);
            cbEstado.requestFocus();
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
                    personaTB.setSexo(cbSex.getSelectionModel().getSelectedIndex() >= 0
                            ? cbSex.getSelectionModel().getSelectedItem().getIdDetalle().get()
                            : 0);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    if (dpBirthdate.getValue() != null) {
                        personaTB.setFechaNacimiento(new java.sql.Date(dateFormat.parse(Tools.getDatePicker(dpBirthdate)).getTime()));
                    } else {
                        personaTB.setFechaNacimiento(null);
                    }
                    personaTB.setEstado(cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    personaTB.setUsuarioRegistro("76423388");
                    personaTB.setTipoDocumentoFacturacion(cbDocumentTypeFactura.getSelectionModel().getSelectedIndex() >= 0 ? cbDocumentTypeFactura.getSelectionModel().getSelectedItem().getIdDetalle().get()
                            : 0);
                    personaTB.setNumeroDocumentoFacturacion(txtDocumentNumberFactura.getText());
                    personaTB.setRazonSocial(txtBusinessName.getText().trim());
                    personaTB.setNombreComercial(txtTradename.getText().trim());
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
    private void onKeyPressedToRegister(KeyEvent event) throws ParseException {
        if (event.getCode() == KeyCode.ENTER) {
            aValidityProcess();
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) throws ParseException {
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

    @FXML
    private void onActionAddName(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/view/icon.png"));
        dialog.setTitle("Desglosar sus datos");
        dialog.setHeaderText("Ingrese los datos completos, de apellidos a nombres");
        dialog.setContentText("Por favor presione enter cuando aya ingresado los datos:\n");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(window.getScene().getWindow());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {

            String[] app = Tools.getDataPeople(result.get());
            if (app != null) {
                try {
                    txtLastName.setText(app[0]);
                    txtMotherLastName.setText(app[1]);
                    txtFirstName.setText(app[2]);
                    txtSecondName.setText(app[3] != null ? app[3] : "");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error :" + e);
                }

            }
        }

    }

}
