package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private ComboBox<DetalleTB> cbAmbito;
    @FXML
    private ComboBox<DetalleTB> cbEstado;

    private String idProveedor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_PRESSED);
        idProveedor = "";
        if (DBUtil.StateConnection()) {
            DetalleADO.GetDetailIdName("2", "0003", "").forEach(e -> {
                cbDocumentTypeFactura.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
            PaisADO.ListPais().forEach(e -> {
                cbPais.getItems().add(new PaisTB(e.getPaisCodigo(), e.getPaisNombre()));
            });
            DetalleADO.GetDetailIdName("2", "0005", "").forEach(e -> {
                cbAmbito.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
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
        txtDocumentNumberFactura.setText(value[0]);
        ArrayList<ProveedorTB> list = ProveedorADO.GetIdPersona(value[0]);
        if (!list.isEmpty()) {
            ProveedorTB proveedorTB = list.get(0);
            idProveedor = proveedorTB.getIdProveedor().get();
            ObservableList<DetalleTB> lstypefa = cbDocumentTypeFactura.getItems();
            for (int i = 0; i < lstypefa.size(); i++) {
                if (proveedorTB.getTipoDocumento() == lstypefa.get(i).getIdDetalle().get()) {
                    cbDocumentTypeFactura.getSelectionModel().select(i);
                    break;
                }
            }
            txtBusinessName.setText(proveedorTB.getRazonSocial().get());
            txtTradename.setText(proveedorTB.getNombreComercial().get());

            ObservableList<PaisTB> lspais = cbPais.getItems();
            if (proveedorTB.getPais() != null || !proveedorTB.getPais().equals("")) {
                for (int i = 0; i < lspais.size(); i++) {
                    if (list.get(0).getPais().equals(lspais.get(i).getPaisCodigo())) {
                        cbPais.getSelectionModel().select(i);
                        CiudadADO.ListPais(cbPais.getSelectionModel().getSelectedItem().getPaisCodigo()).forEach(e -> {
                            cbCiudad.getItems().add(new CiudadTB(e.getCiudadID(), e.getCiudadDistrito()));
                        });
                        break;
                    }
                }
            }

            ObservableList<CiudadTB> lsciudad = cbCiudad.getItems();
            if (proveedorTB.getCiudad() != 0) {
                for (int i = 0; i < lsciudad.size(); i++) {
                    if (proveedorTB.getCiudad() == lsciudad.get(i).getCiudadID()) {
                        cbCiudad.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            ObservableList<DetalleTB> lstamb = cbAmbito.getItems();
            if (proveedorTB.getAmbito() != 0) {
                for (int i = 0; i < lstamb.size(); i++) {
                    if (proveedorTB.getAmbito() == lstamb.get(i).getIdDetalle().get()) {
                        cbAmbito.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            ObservableList<DetalleTB> lstyest = cbEstado.getItems();
            for (int i = 0; i < lstyest.size(); i++) {
                if (proveedorTB.getEstado() == lstyest.get(i).getIdDetalle().get()) {
                    cbEstado.getSelectionModel().select(i);
                    break;
                }
            }
        }
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
                    proveedorTB.setAmbito(cbAmbito.getSelectionModel().getSelectedIndex() >= 0
                            ? cbAmbito.getSelectionModel().getSelectedItem().getIdDetalle().get()
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
        controller.setLoadView(idProveedor, txtBusinessName.getText());
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
    private void onKeyPressedToDirectory(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewPerfil();
        }
    }

    @FXML
    private void onActionToDirectory(ActionEvent event) throws IOException {
        onViewPerfil();
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
    private void onActionPais(ActionEvent event) {
        if (cbPais.getSelectionModel().getSelectedIndex() >= 0) {
            cbCiudad.getItems().clear();
            CiudadADO.ListPais(cbPais.getSelectionModel().getSelectedItem().getPaisCodigo()).forEach(e -> {
                cbCiudad.getItems().add(new CiudadTB(e.getCiudadID(), e.getCiudadDistrito()));
            });
        }
    }

    private void onViewPersonas() throws IOException {
        
        URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOLISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
//        FxPerfilController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccionar Proveedor", window.getScene().getWindow());
        stage.setResizable(false);
       
        stage.show();
//        controller.setLoadView(idProveedor, txtBusinessName.getText());
    }

    @FXML
    private void onActionToRepresentanteRegister(ActionEvent event) throws IOException {
        onViewPersonas();
    }

    @FXML
    private void onKeyPressedToRepresentanteRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewPersonas();
        }
    }

}
