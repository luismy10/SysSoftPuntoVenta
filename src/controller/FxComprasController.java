package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ArticuloTB;
import model.CompraADO;
import model.CompraTB;
import model.DetalleADO;
import model.DetalleTB;
import model.LoteTB;
import model.ProveedorADO;
import model.RepresentanteADO;
import model.RepresentanteTB;

public class FxComprasController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TextField txtProveedor;
    @FXML
    private ComboBox<RepresentanteTB> cbRepresentante;
    @FXML
    private ComboBox<DetalleTB> cbComprobante;
    @FXML
    private TextField cbNumeracion;
    @FXML
    private DatePicker tpFechaCompra;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, String> tcArticulo;
    @FXML
    private TableColumn<ArticuloTB, String> tcCantidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecio;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescuento;
    @FXML
    private TableColumn<ArticuloTB, String> tcImporte;
    @FXML
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblGravada;
    @FXML
    private Text lblIgv;
    @FXML
    private Text lblTotal;
    @FXML
    private Button btnArticulo;

    private AnchorPane content;

    private String idProveedor;

    private String idRepresentante;

    private double subTotal;

    private double descuento;

    private ObservableList<LoteTB> loteTBs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idProveedor = idRepresentante = "";
        loteTBs = FXCollections.observableArrayList();
        Tools.actualDate(Tools.getDate(), tpFechaCompra);
        DetalleADO.GetDetailIdName("2", "0009", "").forEach(e -> {
            cbComprobante.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbComprobante.getSelectionModel().select(0);
        cbRepresentante.setConverter(new javafx.util.StringConverter<RepresentanteTB>() {
            @Override
            public String toString(RepresentanteTB object) {
                return object.getInformacion();
            }

            @Override
            public RepresentanteTB fromString(String string) {
                return cbRepresentante.getItems().stream().filter(p->p.getInformacion().equals(string)).findFirst().orElse(null);
            }
        });
        initTable();
    }

    private void initTable() {
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave().get() + "\n" + cellData.getValue().getNombre().get()
        ));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCantidad().get(), 2)));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getPrecioCompra(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getDescuento().get(), 2)));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getImporte().get(), 2)));
    }

    private void InitializationTransparentBackground() {
        SysSoft.pane.setStyle("-fx-background-color: black");
        SysSoft.pane.setTranslateX(0);
        SysSoft.pane.setTranslateY(0);
        SysSoft.pane.setPrefWidth(Session.WIDTH_WINDOW);
        SysSoft.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        SysSoft.pane.setOpacity(0.7f);
        content.getChildren().add(SysSoft.pane);
    }

    private void onViewRegister() {
        if (txtProveedor.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compras", "Ingrese un proveedor, por favor.", false);
            txtProveedor.requestFocus();
        } else if (cbComprobante.getSelectionModel().getSelectedIndex() < 0) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compras", "Seleccione tipo de comprobante, por favor.", false);
            cbComprobante.requestFocus();
        } else if (cbNumeracion.getText().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compras", "Ingrese la numeración del comprobante, por favor.", false);
            cbNumeracion.requestFocus();
        } else if (tpFechaCompra.getValue() == null) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compras", "Ingrese la fecha de compra, por favor.", false);
            tpFechaCompra.requestFocus();
        } else if (tvList.getItems().isEmpty()) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compras", "Ingrese algún producto para realizar la compra, por favor.", false);
            btnArticulo.requestFocus();
        } else {
            CompraTB compraTB = new CompraTB();
            compraTB.setProveedor(idProveedor);
            compraTB.setRepresentante(idRepresentante);
            compraTB.setComprobante(cbComprobante.getSelectionModel().getSelectedItem().getIdDetalle().get());
            compraTB.setNumeracion(cbNumeracion.getText().trim());
            compraTB.setFechaRegistro(Timestamp.valueOf(Tools.getDatePicker(tpFechaCompra) + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
            compraTB.setSubTotal(Double.parseDouble(lblSubTotal.getText()));
            compraTB.setDescuento(Double.parseDouble(lblDescuento.getText()));
            compraTB.setGravada(Double.parseDouble(lblGravada.getText()));
            compraTB.setIgv(Double.parseDouble(lblIgv.getText()));
            compraTB.setTotal(Double.parseDouble(lblTotal.getText()));
            String result = CompraADO.CrudEntity(compraTB, tvList, loteTBs);
            if (result.equalsIgnoreCase("register")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Compras", "Registrado correctamente la compra.", false);
                idProveedor = idRepresentante = "";
                txtProveedor.clear();
                cbRepresentante.getItems().clear();
                cbNumeracion.clear();
                Tools.actualDate(Tools.getDate(), tpFechaCompra);
                tvList.getItems().clear();
                loteTBs.clear();
                initTable();
                lblSubTotal.setText("0.00");
                lblDescuento.setText("0.00");
                lblGravada.setText("0.00");
                lblIgv.setText("0.00");
                lblTotal.setText("0.00");
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Compras", result, false);

            }
        }
    }

    private void openWindowArticulos() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_ARTICULOLISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxArticuloListaController controller = fXMLLoader.getController();
        controller.setInitComptrasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
        controller.fillProvidersTable("");
    }

    private void onViewEdit() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            ObservableList<ArticuloTB> articuloTBs;
            articuloTBs = tvList.getSelectionModel().getSelectedItems();
            articuloTBs.forEach(e -> {
                try {
                    URL url = getClass().getResource(Tools.FX_FILE_ARTICULOCOMPRA);
                    FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
                    Parent parent = fXMLLoader.load(url.openStream());
                    //Controlller here
                    FxArticuloCompraController controller = fXMLLoader.getController();
                    controller.setInitCompraController(this);
                    //
                    Stage stage = FxWindow.StageLoaderModal(parent, "Editar artículo", window.getScene().getWindow());
                    stage.setResizable(false);
                    stage.show();
                    ArticuloTB articuloTB = new ArticuloTB();
                    articuloTB.setIdArticulo(e.getIdArticulo());
                    articuloTB.setClave(e.getClave().get());
                    articuloTB.setNombre(e.getNombre().get());
                    articuloTB.setCantidad(e.getCantidad().get());
                    articuloTB.setPrecioCompra(e.getPrecioCompra());
                    articuloTB.setPrecioCompraReal(e.getPrecioCompraReal());
                    articuloTB.setPrecioVenta(e.getPrecioVenta().get());
                    articuloTB.setDescuento(e.getDescuento().get());
                    articuloTB.setImporte(e.getImporte().get());
                    articuloTB.setUtilidad(e.getUtilidad().get());
                    articuloTB.setImpuesto(e.isImpuesto());
                    articuloTB.setLote(e.isLote());
                    controller.setLoadEdit(articuloTB, tvList.getSelectionModel().getSelectedIndex(), loteTBs);
                } catch (IOException ex) {
                    Logger.getLogger(FxComprasController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compras", "Seleccione un artículo para editarlo", false);

        }
    }

    private void onViewRemove() {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            short confirmation = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Compras", "¿Esta seguro de quitar el artículo?", true);
            if (confirmation == 1) {
                ObservableList<ArticuloTB> observableList, articuloTBs;
                observableList = tvList.getItems();
                articuloTBs = tvList.getSelectionModel().getSelectedItems();

                articuloTBs.forEach(e -> {
                    for (int i = 0; i < loteTBs.size(); i++) {
                        if (loteTBs.get(i).getIdArticulo().equals(e.getIdArticulo())) {
                            loteTBs.remove(i);
                            i--;
                        }
                    }
                    observableList.remove(e);
                });
                setCalculateTotals();

            }

        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compras", "Seleccione un artículo para removerlo", false);
        }
    }

    private void openWindowProvedores() throws IOException {
        InitializationTransparentBackground();
        URL url = getClass().getResource(Tools.FX_FILE_PROVEEDORLISTA);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxProveedorListaController controller = fXMLLoader.getController();
        controller.setInitComptrasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Proveedor", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
        controller.fillCustomersTable("");
    }

    @FXML
    private void onKeyPressedRegister(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onViewRegister();
        }
    }

    @FXML
    private void onActionRegister(ActionEvent event) {
        onViewRegister();
    }

    @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowArticulos();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowArticulos();
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewEdit();
        }
    }

    @FXML
    private void onActionEdit(ActionEvent event) {
        onViewEdit();
    }

    @FXML
    private void onKeyPressedRemover(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onViewRemove();
        }
    }

    @FXML
    private void onActionRemover(ActionEvent event) {
        onViewRemove();
    }

    @FXML
    private void onKeyPressedProviders(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowProvedores();
        }
    }

    @FXML
    private void onActionProviders(ActionEvent event) throws IOException {
        openWindowProvedores();
    }

    public void setInitComprasValue(String... value) {
        idProveedor = ProveedorADO.GetProveedorId(value[0]);
        txtProveedor.setText(value[1]);
        cbRepresentante.getItems().clear();
        RepresentanteADO.ListRepresentantes_By_Id(idProveedor, "").forEach(e -> {
            cbRepresentante.getItems().add(new RepresentanteTB(e.getNumeroDocumento(), e.getApellidos() + " "
                    + e.getNombres()));
        });

    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

    @FXML
    private void onKeyTypedNumeracion(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c != '-')
                && (c != '/') && (c != '+') && (c != '*') && (c != '(') && (c != ')') && (c != '|') && (c != '°')) {
            event.consume();
        }
    }

    @FXML
    private void onActionRepresentante(ActionEvent event) {
        if (cbRepresentante.getSelectionModel().getSelectedIndex() >= 0) {
            idRepresentante = RepresentanteADO.GetRepresentanteId(cbRepresentante.getSelectionModel().getSelectedItem().getNumeroDocumento());
        }
       
    }

    public TableView<ArticuloTB> getTvList() {
        return tvList;
    }

    public void setCalculateTotals() {

        double subTotalInterno, descuentoInterno;

        tvList.getItems().forEach(e -> subTotal += e.getSubTotal().get());
        lblSubTotal.setText(Tools.roundingValue(subTotal, 2));
        subTotalInterno = subTotal;
        subTotal = 0;

        tvList.getItems().forEach(e -> descuento += e.getDescuento().get());
        lblDescuento.setText(Tools.roundingValue(descuento, 2));
        descuentoInterno = descuento;
        descuento = 0;

        double total = subTotalInterno - descuentoInterno;
        lblTotal.setText(Tools.roundingValue(total, 2));

        double gravada = Tools.calculateValueNeto(Session.IMPUESTO, total);
        lblGravada.setText(Tools.roundingValue(gravada, 2));

        double impuesto = Tools.calculateTax(Session.IMPUESTO, gravada);
        lblIgv.setText(Tools.roundingValue(impuesto, 2));

    }

    public ObservableList<LoteTB> getLoteTBs() {
        return loteTBs;
    }

}
