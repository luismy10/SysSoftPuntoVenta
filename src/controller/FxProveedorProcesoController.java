package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CiudadADO;
import model.CiudadTB;
import model.DBUtil;
import model.DetalleADO;
import model.DetalleTB;
import model.DistritoADO;
import model.DistritoTB;
import model.PaisADO;
import model.PaisTB;
import model.PersonaADO;
import model.ProveedorADO;
import model.ProveedorTB;
import model.ProvinciaADO;
import model.ProvinciaTB;
import model.RepresentanteADO;
import model.RepresentanteTB;

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
    private TextField txtBusinessName;
    @FXML
    private TextField txtTradename;
    @FXML
    private ComboBox<CiudadTB> cbCiudad;
    @FXML
    private ComboBox<ProvinciaTB> cbProvincia;
    @FXML
    private ComboBox<DistritoTB> cbDistrito;
    @FXML
    private TextField txtDocumentNumberFactura;
    @FXML
    private ComboBox<DetalleTB> cbAmbito;
    @FXML
    private ComboBox<DetalleTB> cbEstado;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<RepresentanteTB> tvList;
    @FXML
    private TableColumn<RepresentanteTB, Integer> tcId;
    @FXML
    private TableColumn<RepresentanteTB, String> tcDocument;
    @FXML
    private TableColumn<RepresentanteTB, String> tcNames;
    @FXML
    private TableColumn<RepresentanteTB, String> tcLastName;
    @FXML
    private TableColumn<RepresentanteTB, String> tcContacto;
    @FXML
    private Tab tbRepresentantes;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCelular;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPaginaWeb;
    @FXML
    private TextField txtDireccion;
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

    private String idProveedor;

    private String idRepresentante;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        idProveedor = idRepresentante = "";
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
        DetalleADO.GetDetailIdName("2", "0003", "").forEach(e -> {
            cbDocumentoRepresentante.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocument.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroDocumento()));
        tcLastName.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getApellidos()
        ));
        tcNames.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNombres()
        ));
        tcContacto.setCellValueFactory(cellData
                -> Bindings.concat(
                        !Tools.isText(cellData.getValue().getTelefono())
                        ? "TEL: " + cellData.getValue().getTelefono() + "\n" + "CEL: " + cellData.getValue().getCelular()
                        : "CEL: " + cellData.getValue().getCelular()
                )
        );

    }

    public void fillCustomersTable(String value) {
        if (!idProveedor.equalsIgnoreCase("")) {
            ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Task<List<RepresentanteTB>> task = new Task<List<RepresentanteTB>>() {
                @Override
                public ObservableList<RepresentanteTB> call() {
                    return RepresentanteADO.ListRepresentantes_By_Id(idProveedor, value);
                }
            };
            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<RepresentanteTB>) task.getValue());
            });
            exec.execute(task);
            if (!exec.isShutdown()) {
                exec.shutdown();
            }
        }

    }

    public void setValueAdd(String... value) {
        lblDirectory.setVisible(false);
        btnDirectory.setVisible(false);
        tbRepresentantes.setDisable(true);
        cbDocumentTypeFactura.requestFocus();
    }

    public void setValueUpdate(String... value) {
        btnRegister.setText("Actualizar");
        btnRegister.getStyleClass().add("buttonLightWarning");
        cbDocumentTypeFactura.requestFocus();
        txtDocumentNumberFactura.setText(value[0]);
        ProveedorTB proveedorTB = ProveedorADO.GetIdLisProveedor(value[0]);
        if (proveedorTB != null) {
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

            if (proveedorTB.getPais() != null || !proveedorTB.getPais().equals("")) {
                ObservableList<PaisTB> lspais = cbPais.getItems();
                for (int i = 0; i < lspais.size(); i++) {
                    if (proveedorTB.getPais().equals(lspais.get(i).getPaisCodigo())) {
                        cbPais.getSelectionModel().select(i);
                        CiudadADO.ListCiudad(cbPais.getSelectionModel().getSelectedItem().getPaisCodigo()).forEach(e -> {
                            cbCiudad.getItems().add(new CiudadTB(e.getIdCiudad(), e.getCiudadDistrito()));
                        });
                        break;
                    }
                }
            }

            if (proveedorTB.getCiudad() != 0) {
                ObservableList<CiudadTB> lsciudad = cbCiudad.getItems();
                for (int i = 0; i < lsciudad.size(); i++) {
                    if (proveedorTB.getCiudad() == lsciudad.get(i).getIdCiudad()) {
                        cbCiudad.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            if (proveedorTB.getProvincia() != 0) {
                ObservableList<ProvinciaTB> lsprovin = cbProvincia.getItems();
                for (int i = 0; i < lsprovin.size(); i++) {
                    if (proveedorTB.getProvincia() == lsprovin.get(i).getIdProvincia()) {
                        cbProvincia.getSelectionModel().select(i);
                        DistritoADO.ListDistrito(cbProvincia.getSelectionModel().getSelectedItem().getIdProvincia()).forEach(e -> {
                            cbDistrito.getItems().add(new DistritoTB(e.getIdDistrito(), e.getDistrito()));
                        });
                        break;
                    }
                }
            }

            if (proveedorTB.getDistrito() != 0) {
                ObservableList<DistritoTB> lsdistrito = cbDistrito.getItems();
                for (int i = 0; i < lsdistrito.size(); i++) {
                    if (proveedorTB.getDistrito() == lsdistrito.get(i).getIdDistrito()) {
                        cbDistrito.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            if (proveedorTB.getAmbito() != 0) {
                ObservableList<DetalleTB> lstamb = cbAmbito.getItems();
                for (int i = 0; i < lstamb.size(); i++) {
                    if (proveedorTB.getAmbito() == lstamb.get(i).getIdDetalle().get()) {
                        cbAmbito.getSelectionModel().select(i);
                        break;
                    }
                }
            }

            ObservableList<DetalleTB> lstest = cbEstado.getItems();
            for (int i = 0; i < lstest.size(); i++) {
                if (proveedorTB.getEstado() == lstest.get(i).getIdDetalle().get()) {
                    cbEstado.getSelectionModel().select(i);
                    break;
                }
            }

            txtTelefono.setText(proveedorTB.getTelefono());
            txtCelular.setText(proveedorTB.getCelular());
            txtEmail.setText(proveedorTB.getEmail());
            txtPaginaWeb.setText(proveedorTB.getPaginaWeb());
            txtDireccion.setText(proveedorTB.getDireccion());

        }
    }

    private void toRegisterRepresentative() throws IOException {
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
            representanteTB.setIdProveedor(idProveedor);
            if (!idProveedor.equalsIgnoreCase("")) {
                String result = RepresentanteADO.InsertRepresentante(representanteTB);
                switch (result) {
                    case "registered":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Agregado correctamente el representante.", false);
                        cbDocumentoRepresentante.getSelectionModel().select(0);
                        txtNunDocumentoRepresentante.clear();
                        txtApellidosRepresentante.clear();
                        txtNombresRepresentante.clear();
                        txtTelefonoRepresentante.clear();
                        txtCelularRepresentante.clear();
                        txtEmailRepresentante.clear();
                        txtDireccionRepresentante.clear();
                        fillCustomersTable("");
                        break;
                    case "duplicate":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Representante", "No puede haber 2 representantes con los mismos datos.", false);
                        showViewRepresentante(txtNunDocumentoRepresentante.getText());                        
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

    private void toCrudProvider() {
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
                            ? cbCiudad.getSelectionModel().getSelectedItem().getIdCiudad()
                            : 0);
                    proveedorTB.setProvincia(cbProvincia.getSelectionModel().getSelectedIndex() >= 0
                            ? cbProvincia.getSelectionModel().getSelectedItem().getIdProvincia() : 0);
                    proveedorTB.setDistrito(cbDistrito.getSelectionModel().getSelectedIndex() >= 0
                            ? cbDistrito.getSelectionModel().getSelectedItem().getIdDistrito() : 0);

                    proveedorTB.setAmbito(cbAmbito.getSelectionModel().getSelectedIndex() >= 0
                            ? cbAmbito.getSelectionModel().getSelectedItem().getIdDetalle().get()
                            : 0);
                    proveedorTB.setEstado(cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
                    proveedorTB.setTelefono(txtTelefono.getText().trim());
                    proveedorTB.setCelular(txtCelular.getText().trim());
                    proveedorTB.setEmail(txtEmail.getText().trim());
                    proveedorTB.setPaginaWeb(txtPaginaWeb.getText().trim());
                    proveedorTB.setDireccion(txtDireccion.getText().trim());
                    proveedorTB.setUsuarioRegistro("76423388");
                    String result = ProveedorADO.CrudEntity(proveedorTB);
                    switch (result) {
                        case "registered":
                            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Registrado correctamente.", false);
                            Tools.Dispose(window);
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

    private void onViewPerfil(String id, String value) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PERFIL);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxPerfilController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Perfil", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        controller.setLoadView(id, value);
    }

    private void showViewRepresentante(String value) throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_REPRESENTANTE);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxRepresentanteController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Representantens", window.getScene().getWindow());
        stage.setResizable(false);
        stage.show();
        if(!value.equalsIgnoreCase(""))controller.getTxtSearch().setText(value);
        controller.fillCustomersTable(value);
    }

    private void removerRepresentante() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Proveedor", "¿Esta seguro de continuar?", true);
            if (confirmation == 1) {
                String idPersona = PersonaADO.GetPersonasId(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento());
                String result = RepresentanteADO.DeleteRepresentante(idProveedor, idPersona);
                switch (result) {
                    case "eliminado":
                        Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Proveedor", "Eliminado correctamente.", false);
                        fillCustomersTable("");
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
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Proveedor", "Debe seleccionar un representante para eliminarlo.", false);
            tvList.requestFocus();
        }

    }

    @FXML
    private void onActionToRegister(ActionEvent event) {
        toCrudProvider();
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            toCrudProvider();
        }
    }

    @FXML
    private void onKeyPressedToDirectory(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewPerfil(idProveedor, txtBusinessName.getText());
        }
    }

    @FXML
    private void onActionToDirectory(ActionEvent event) throws IOException {
        onViewPerfil(idProveedor, txtBusinessName.getText());
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
            CiudadADO.ListCiudad(cbPais.getSelectionModel().getSelectedItem().getPaisCodigo()).forEach(e -> {
                cbCiudad.getItems().add(new CiudadTB(e.getIdCiudad(), e.getCiudadDistrito()));
            });
        }
    }

    @FXML
    private void onActionCiudad(ActionEvent event) {
        if (cbCiudad.getSelectionModel().getSelectedIndex() >= 0) {
            cbProvincia.getItems().clear();
            ProvinciaADO.ListProvincia(cbCiudad.getSelectionModel().getSelectedItem().getIdCiudad()).forEach(e -> {
                cbProvincia.getItems().add(new ProvinciaTB(e.getIdProvincia(), e.getProvincia()));
            });
        }
    }

    @FXML
    private void onActionProvincia(ActionEvent event) {
        if (cbProvincia.getSelectionModel().getSelectedIndex() >= 0) {
            cbDistrito.getItems().clear();
            DistritoADO.ListDistrito(cbProvincia.getSelectionModel().getSelectedItem().getIdProvincia()).forEach(e -> {
                cbDistrito.getItems().add(new DistritoTB(e.getIdDistrito(), e.getDistrito()));
            });
        }
    }

    @FXML
    private void onKeyPressedSeach(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            tvList.requestFocus();
        }
    }

    @FXML
    private void onKeyReleasedSeach(KeyEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onActionToRepresentanteRegister(ActionEvent event) throws IOException {
        toRegisterRepresentative();
    }

    @FXML
    private void onKeyPressedToRepresentanteRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            toRegisterRepresentative();
        }
    }

    @FXML
    private void onKeyPressedToRepresentanteEdit(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionToRepresentanteEdit(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToRepresentanteRemover(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            removerRepresentante();
        }
    }

    @FXML
    private void onActionToRepresentanteRemover(ActionEvent event) {
        removerRepresentante();
    }

    @FXML
    private void onKeyPressedLista(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            showViewRepresentante("");
        }
    }

    @FXML
    private void onActionLista(ActionEvent event) throws IOException {
        showViewRepresentante("");
    }

}
