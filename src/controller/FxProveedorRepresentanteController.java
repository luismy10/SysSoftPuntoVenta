package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.DetalleADO;
import model.DetalleTB;
import model.RepresentanteADO;
import model.RepresentanteTB;

public class FxProveedorRepresentanteController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ComboBox<DetalleTB> cbDocumentoRepresentante;
    @FXML
    private TextField txtNunDocumentoRepresentante;
    @FXML
    private TextField txtApellidosRepresentante;
    @FXML
    private TextField txtNombresRepresentante;
    @FXML
    private TextField txtTelefonoRepresentante;
    @FXML
    private TextField txtCelularRepresentante;
    @FXML
    private TextField txtEmailRepresentante;
    @FXML
    private TextField txtDireccionRepresentante;
    @FXML
    private Button btnProceso;

    private FxProveedorProcesoController proveedorProcesoController;

    private String idProveedor;

    private String idRepresentante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        DetalleADO.GetDetailIdName("2", "0003", "").forEach(e -> {
            cbDocumentoRepresentante.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        idProveedor = idRepresentante = "";
    }

    public void initRegisterDetalle(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void initUpdateDetalle(String documento) {
        btnProceso.setText("Actualizar");
        btnProceso.getStyleClass().add("buttonLightWarning");
        RepresentanteTB representanteTB = RepresentanteADO.Get_By_Documento_Representante(documento);
        if (representanteTB != null) {
            idRepresentante = representanteTB.getIdRepresentante();
            if (representanteTB.getTipoDocumento() != 0) {
                ObservableList<DetalleTB> lsest = cbDocumentoRepresentante.getItems();
                for (int i = 0; i < lsest.size(); i++) {
                    if (representanteTB.getTipoDocumento() == lsest.get(i).getIdDetalle().get()) {
                        cbDocumentoRepresentante.getSelectionModel().select(i);
                        break;
                    }
                }
            }
            txtNunDocumentoRepresentante.setText(representanteTB.getNumeroDocumento());
            txtApellidosRepresentante.setText(representanteTB.getApellidos());
            txtNombresRepresentante.setText(representanteTB.getNombres());
            txtTelefonoRepresentante.setText(representanteTB.getTelefono());
            txtCelularRepresentante.setText(representanteTB.getCelular());
            txtEmailRepresentante.setText(representanteTB.getEmail());
            txtDireccionRepresentante.setText(representanteTB.getDireccion());
        }
    }

    public void toRegisterRepresentative() throws IOException {
        if (cbDocumentoRepresentante.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Seleccione el tipo de documento", false);
            cbDocumentoRepresentante.requestFocus();
        } else if (txtNunDocumentoRepresentante.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Ingrese el número del documento", false);
            txtNunDocumentoRepresentante.requestFocus();
        } else if (txtApellidosRepresentante.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Ingrese los apellidos", false);
            txtApellidosRepresentante.requestFocus();
        } else if (txtNombresRepresentante.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Ingrese los nombres", false);
            txtNombresRepresentante.requestFocus();
        } else {
            RepresentanteTB representanteTB = new RepresentanteTB();
            representanteTB.setTipoDocumento(cbDocumentoRepresentante.getSelectionModel().getSelectedItem().getIdDetalle().get());
            representanteTB.setNumeroDocumento(txtNunDocumentoRepresentante.getText().trim());
            representanteTB.setApellidos(txtApellidosRepresentante.getText().toUpperCase().trim());
            representanteTB.setNombres(txtNombresRepresentante.getText().toUpperCase().trim());
            representanteTB.setTelefono(txtTelefonoRepresentante.getText().trim());
            representanteTB.setCelular(txtCelularRepresentante.getText().trim());
            representanteTB.setEmail(txtEmailRepresentante.getText().trim());
            representanteTB.setDireccion(txtDireccionRepresentante.getText().trim());
            representanteTB.setIdProveedor(idProveedor);
            if (!idProveedor.equalsIgnoreCase("")) {
                String result = RepresentanteADO.InsertRepresentante(representanteTB);
                switch (result) {
                    case "registered":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Agregado correctamente el representante.", false);
                        idProveedor = "";
                        cbDocumentoRepresentante.getSelectionModel().select(0);
                        txtNunDocumentoRepresentante.clear();
                        txtApellidosRepresentante.clear();
                        txtNombresRepresentante.clear();
                        txtTelefonoRepresentante.clear();
                        txtCelularRepresentante.clear();
                        txtEmailRepresentante.clear();
                        txtDireccionRepresentante.clear();
                        proveedorProcesoController.fillCustomersTable("");
                        break;
                    case "duplicate":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "No puede haber 2 representantes con los mismos datos.", false);
                        proveedorProcesoController.showViewRepresentante(txtNunDocumentoRepresentante.getText());
                        break;
                    case "error":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "No se puedo agregar el representante.", false);
                        break;
                    default:
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Representante", result, false);
                        break;
                }
            }

        }

    }

    public void toUpdateRepresentative() throws IOException {
        if (cbDocumentoRepresentante.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Seleccione el tipo de documento", false);
            cbDocumentoRepresentante.requestFocus();
        } else if (txtNunDocumentoRepresentante.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Ingrese el número del documento", false);
            txtNunDocumentoRepresentante.requestFocus();
        } else if (txtApellidosRepresentante.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Ingrese los apellidos", false);
            txtApellidosRepresentante.requestFocus();
        } else if (txtNombresRepresentante.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "Ingrese los nombres", false);
            txtNombresRepresentante.requestFocus();
        } else {
            RepresentanteTB representanteTB = new RepresentanteTB();
            representanteTB.setIdRepresentante(idRepresentante);
            representanteTB.setTipoDocumento(cbDocumentoRepresentante.getSelectionModel().getSelectedItem().getIdDetalle().get());
            representanteTB.setNumeroDocumento(txtNunDocumentoRepresentante.getText().trim());
            representanteTB.setApellidos(txtApellidosRepresentante.getText().toUpperCase().trim());
            representanteTB.setNombres(txtNombresRepresentante.getText().toUpperCase().trim());
            representanteTB.setTelefono(txtTelefonoRepresentante.getText().trim());
            representanteTB.setCelular(txtCelularRepresentante.getText().trim());
            representanteTB.setEmail(txtEmailRepresentante.getText().trim());
            representanteTB.setDireccion(txtDireccionRepresentante.getText().trim());
            if (!idRepresentante.equalsIgnoreCase("")) {
                String result = RepresentanteADO.UpdateRepresentante(representanteTB);
                switch (result) {
                    case "updated":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Actualizado correctamente el representante.", false);
                        idRepresentante = "";
                        cbDocumentoRepresentante.getSelectionModel().select(0);
                        txtNunDocumentoRepresentante.clear();
                        txtApellidosRepresentante.clear();
                        txtNombresRepresentante.clear();
                        txtTelefonoRepresentante.clear();
                        txtCelularRepresentante.clear();
                        txtEmailRepresentante.clear();
                        txtDireccionRepresentante.clear();
                        proveedorProcesoController.fillCustomersTable("");
                        break;
                    case "duplicate":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "No puede haber 2 representantes con los mismos datos.", false);
                        proveedorProcesoController.showViewRepresentante(txtNunDocumentoRepresentante.getText());
                        break;
                    case "error":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "No se puedo agregar el representante.", false);
                        break;
                    default:
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Representante", result, false);
                        break;
                }
            }

        }

    }

    @FXML
    private void onKeyPressedProceso(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            if (!idProveedor.equalsIgnoreCase("")) {
                toRegisterRepresentative();
            } else if (!idRepresentante.equalsIgnoreCase("")) {
                toUpdateRepresentative();
            }
        }
    }

    @FXML
    private void onActionProceso(ActionEvent event) throws IOException {
        if (!idProveedor.equalsIgnoreCase("")) {
            toRegisterRepresentative();
        } else if (!idRepresentante.equalsIgnoreCase("")) {
            toUpdateRepresentative();
        }
    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Tools.Dispose(window);
        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        Tools.Dispose(window);
    }

    public void setControllerProveedor(FxProveedorProcesoController proveedorProcesoController) {
        this.proveedorProcesoController = proveedorProcesoController;
    }

}
