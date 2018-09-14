package controller;

import java.net.URL;
import java.util.ArrayList;
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
import model.CiudadADO;
import model.CiudadTB;
import model.DBUtil;
import model.DetalleADO;
import model.DetalleTB;
import model.EmpresaADO;
import model.EmpresaTB;
import model.PaisADO;
import model.PaisTB;

public class FxMiEmpresaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCelular;
    @FXML
    private TextField txtPaginasWeb;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtDomicilio;
    @FXML
    private ComboBox<DetalleTB> cbTipoDocumento;
    @FXML
    private TextField txtNumeroDocumento;
    @FXML
    private TextField txtRazonSocial;
    @FXML
    private TextField txtNombreComercial;
    @FXML
    private ComboBox<PaisTB> cbPais;
    @FXML
    private ComboBox<CiudadTB> cbCiudad;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnCancel;

    private boolean validate;

    private int idEmpresa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (DBUtil.StateConnection()) {
            PaisADO.ListPais().forEach(e -> {
                cbPais.getItems().add(new PaisTB(e.getPaisCodigo(), e.getPaisNombre()));
            });
            DetalleADO.GetDetailIdName("0", "0003", "RUC").forEach(e -> {
                cbTipoDocumento.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
            });
            ArrayList<EmpresaTB> list = EmpresaADO.GetEmpresa();
            if (!list.isEmpty()) {
                validate = true;
                idEmpresa = list.get(0).getIdEmpresa();
                txtNombre.setText(list.get(0).getNombre());
                txtTelefono.setText(list.get(0).getTelefono());
                txtCelular.setText(list.get(0).getCelular());
                txtPaginasWeb.setText(list.get(0).getPaginaWeb());
                txtEmail.setText(list.get(0).getEmail());
                txtDomicilio.setText(list.get(0).getDomicilio());
                
                ObservableList<DetalleTB> lsdoc = cbTipoDocumento.getItems();
                if(list.get(0).getTipoDocumento() != 0){
                    for (int i = 0; i < lsdoc.size(); i++) {
                        if (list.get(0).getTipoDocumento() == lsdoc.get(i).getIdDetalle().get()) {
                            cbTipoDocumento.getSelectionModel().select(i);
                            break;
                        }
                    }
                }
                
                txtNumeroDocumento.setText(list.get(0).getNumeroDocumento());
                txtRazonSocial.setText(list.get(0).getRazonSocial());
                txtNombreComercial.setText(list.get(0).getNombreComercial());
                

                ObservableList<PaisTB> lspais = cbPais.getItems();
                if (list.get(0).getPais() != null) {
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
                if (list.get(0).getCiudad() != 0) {
                    for (int i = 0; i < lsciudad.size(); i++) {
                        if (list.get(0).getCiudad() == lsciudad.get(i).getCiudadID()) {
                            cbCiudad.getSelectionModel().select(i);
                            break;
                        }
                    }
                }
            } else {
                validate = false;
            }

        }
    }

    private void aValidityProcess() {
        if (txtNombre.getText().equalsIgnoreCase("")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Mi Empresa", "Ingrese el nombre de la empresa, por favor.", false);
            txtNombre.requestFocus();
        } else {
            if (DBUtil.StateConnection()) {
                short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Mi Empresa", "¿Esta seguro de continuar?", true);
                if (confirmation == 1) {
                    EmpresaTB empresaTB = new EmpresaTB();
                    empresaTB.setIdEmpresa(validate == true ? idEmpresa : 0);
                    empresaTB.setNombre(txtNombre.getText().trim());
                    empresaTB.setTelefono(txtTelefono.getText().trim());
                    empresaTB.setCelular(txtCelular.getText().trim());
                    empresaTB.setPaginaWeb(txtPaginasWeb.getText().trim());
                    empresaTB.setEmail(txtEmail.getText().trim());
                    empresaTB.setDomicilio(txtDomicilio.getText().trim());
                    empresaTB.setTipoDocumento(cbTipoDocumento.getSelectionModel().getSelectedIndex() >= 0
                            ? cbTipoDocumento.getSelectionModel().getSelectedItem().getIdDetalle().get()
                            : 0);
                    empresaTB.setNumeroDocumento(txtNumeroDocumento.getText().trim());
                    empresaTB.setRazonSocial(txtRazonSocial.getText().trim());
                    empresaTB.setNombreComercial(txtNombreComercial.getText().trim());
                    empresaTB.setPais(cbPais.getSelectionModel().getSelectedIndex() >= 0
                            ? cbPais.getSelectionModel().getSelectedItem().getPaisCodigo()
                            : null);
                    empresaTB.setCiudad(cbCiudad.getSelectionModel().getSelectedIndex() >= 0
                            ? cbCiudad.getSelectionModel().getSelectedItem().getCiudadID()
                            : 0);
                    String result = EmpresaADO.CrudEntity(empresaTB);
                    switch (result) {
                        case "registered":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Mi Empresa", "Registrado correctamente.", false);
                            Tools.Dispose(window);
                            break;
                        case "updated":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Mi Empresa", "Actualizado correctamente.", false);

                            break;
                        case "error":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Mi Empresa", "No se puedo completar la ejecución.", false);
                            break;
                        default:
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Mi Empresa", result, false);
                            break;
                    }
                }
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Mi Empresa", "No hay conexión al servidor.", false);

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
