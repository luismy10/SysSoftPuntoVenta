package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.CiudadADO;
import model.CiudadTB;
import model.DBUtil;
import model.DetalleADO;
import model.DetalleTB;
import model.PaisADO;
import model.PaisTB;
import model.ProveedorADO;
import model.ProveedorTB;

public class FxProveedorProcesoController implements Initializable {

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
    private ComboBox<PaisTB> cbPais;
    @FXML
    private Label lblDirectory;
    @FXML
    private Button btnDirectory;
    @FXML
    private ComboBox<DetalleTB> cbDocumentTypeFactura;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtBusinessName;
    @FXML
    private TextField txtTradename;
    @FXML
    private ComboBox<CiudadTB> cbCiudad;
    @FXML
    private TextField txtDocumentNumberFactura;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtSecondName;
    @FXML
    private ComboBox<DetalleTB> cbEstado;

    private long idProveedor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        idProveedor = 0;
        if (DBUtil.StateConnection()) {
            DetalleADO.GetDetailIdName("2", "0003", "").forEach(e -> {
                cbDocumentTypeFactura.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
            DetalleADO.GetDetailIdName("1", "0003", "RUC").forEach(e -> {
                cbDocumentType.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
            PaisADO.ListPais().forEach(e -> {
                cbPais.getItems().add(new PaisTB(e.getPaisCodigo(), e.getPaisNombre()));
            });
            DetalleADO.GetDetailIdName("2", "0001", "").forEach(e -> {
                cbEstado.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });

        }
    }

    public void setValueAdd(String... value) {
        lblDirectory.setVisible(false);
        btnDirectory.setVisible(false);
    }

    public void setValueUpdate(String... value) {
        btnRegister.setText("Actualizar");
        btnRegister.getStyleClass().add("buttonFourth");
    }

    private void aValidityProcess() {
        if (cbDocumentTypeFactura.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Seleccione el tipo de documento, por favor.", false);

            cbDocumentTypeFactura.requestFocus();
        } else if (txtDocumentNumberFactura.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Ingrese el número del documento, por favor.", false);

            txtDocumentNumberFactura.requestFocus();
        } else if (txtBusinessName.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Ingrese la razón social o datos correspondientes, por favor.", false);

            txtBusinessName.requestFocus();
        } else if (cbEstado.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Seleccione el estado, por favor.", false);

            cbEstado.requestFocus();
        } else {
            if (DBUtil.StateConnection()) {
                short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Mi Empresa", "¿Esta seguro de continuar?", true);
                if (confirmation == 1) {
                    ProveedorTB proveedorTB = new ProveedorTB();
                    proveedorTB.setIdProveedor(idProveedor);
                    proveedorTB.setTipoDocumento(cbDocumentTypeFactura.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    proveedorTB.setNumeroDocumento(txtDocumentNumberFactura.getText().trim());
                    proveedorTB.setRazonSocial(txtBusinessName.getText().trim());
                    proveedorTB.setNombreComercial(txtTradename.getText().trim());
                    proveedorTB.setPais(cbPais.getSelectionModel().getSelectedIndex() >= 0
                            ? cbPais.getSelectionModel().getSelectedItem().getPaisCodigo()
                            : "");
                    proveedorTB.setCiudad(cbCiudad.getSelectionModel().getSelectedIndex() >= 0
                            ? cbCiudad.getSelectionModel().getSelectedItem().getCiudadID()
                            : 0);
                    proveedorTB.setEstado(cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    proveedorTB.setUsuarioRegistro("76423388");
                    String result = ProveedorADO.CrudEntity(proveedorTB);
                    switch (result) {
                        case "registered":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Registrado correctamente.", false);
                            break;
                        case "updated":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Actualizado correctamente.", false);
                            break;
                        case "duplicate":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Persona", "No se puede haber 2 personas con el mismo documento.", false);
                            txtDocumentNumberFactura.requestFocus();
                            break;
                        case "error":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "No se puedo completar la ejecución.", false);
                            break;
                        default:
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Proveedor", result, false);
                            break;
                    }
                }
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Proveedor", "No hay conexión al servidor.", false);
            }
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) {
        aValidityProcess();
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            aValidityProcess();
        }
    }

    @FXML
    private void onKeyPressedToDirectory(KeyEvent event) {
    }

    @FXML
    private void onActionToDirectory(ActionEvent event) {
    }

    @FXML
    private void onKeyPressedToCancel(KeyEvent event) {
    }

    @FXML
    private void onActionToCancel(ActionEvent event) {
    }

    @FXML
    private void onActionPais(ActionEvent event) {
        if (cbPais.getSelectionModel().getSelectedIndex() >= 0) {
            cbCiudad.getItems().clear();
            CiudadADO.ListPais(cbPais.getSelectionModel().getSelectedItem().getPaisCodigo()).forEach(e -> {
                cbCiudad.getItems().add(new CiudadTB(e.getCiudadID(), e.getCiudadDistrito()));
            });
        }
    }

}
