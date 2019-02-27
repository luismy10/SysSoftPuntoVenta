package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ArticuloTB;
import model.CompraADO;
import model.CompraTB;
import model.ImpuestoADO;
import model.ImpuestoTB;
import model.LoteTB;
import model.MonedaADO;
import model.MonedaTB;
import model.ProveedorADO;
import model.RepresentanteADO;
import model.RepresentanteTB;
import model.TipoDocumentoADO;
import model.TipoDocumentoTB;

public class FxComprasController implements Initializable {

    @FXML
    private ScrollPane window;
    @FXML
    private TextField txtProveedor;
    @FXML
    private ComboBox<RepresentanteTB> cbRepresentante;
    @FXML
    private ComboBox<TipoDocumentoTB> cbComprobante;
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
    private TableColumn<ArticuloTB, String> tcCosto;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescuento;
    @FXML
    private TableColumn<ArticuloTB, String> tcImpuesto;
    @FXML
    private TableColumn<ArticuloTB, String> tcImporte;
    @FXML
    private Text lblSubTotal;
    @FXML
    private Text lblDescuento;
    @FXML
    private Text lblSubTotalNuevo;
    @FXML
    private Text lblTotal;
    @FXML
    private Text lblMonedaSubTotal;
    @FXML
    private Text lblMonedaDescuento;
    @FXML
    private Text lblMonedaSubTotalNuevo;
    @FXML
    private Text lblMonedaTotal;
    @FXML
    private Button btnArticulo;
    @FXML
    private ComboBox<MonedaTB> cbMoneda;
    @FXML
    private HBox hbAgregarImpuesto;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private TextField txtNotas;

    private AnchorPane content;

    private String idProveedor;

    private String idRepresentante;

    private double subImporte;

    private double descuento;

    private double subTotalImporte;

    private double totalImporte;

    private ArrayList<ImpuestoTB> arrayArticulos;

    private ObservableList<LoteTB> loteTBs;

    private String monedaSimbolo;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idProveedor = idRepresentante = "";
        monedaSimbolo = "M";
        loteTBs = FXCollections.observableArrayList();
        Tools.actualDate(Tools.getDate(), tpFechaCompra);

        arrayArticulos = new ArrayList<>();
        ImpuestoADO.GetTipoImpuestoCombBox().forEach(e -> {
            arrayArticulos.add(new ImpuestoTB(e.getIdImpuesto(), e.getNombre(), e.getValor(), e.getPredeterminado()));
        });

        cbComprobante.getItems().clear();
        TipoDocumentoADO.GetDocumentoCombBox().forEach(e -> {
            cbComprobante.getItems().add(new TipoDocumentoTB(e.getIdTipoDocumento(), e.getNombre(), e.isPredeterminado()));
        });
        if (!cbComprobante.getItems().isEmpty()) {
            for (int i = 0; i < cbComprobante.getItems().size(); i++) {
                if (cbComprobante.getItems().get(i).isPredeterminado() == true) {
                    cbComprobante.getSelectionModel().select(i);
                    Session.DEFAULT_COMPROBANTE = i;
                    break;
                }
            }
        }

        cbMoneda.getItems().clear();
        cbMoneda.getItems().add(new MonedaTB(0, "Seleccione una moneda", "", false));
        MonedaADO.GetMonedasCombBox().forEach(e -> {
            cbMoneda.getItems().add(new MonedaTB(e.getIdMoneda(), e.getNombre(), e.getSimbolo(), e.getPredeterminado()));
        });

        if (!cbMoneda.getItems().isEmpty()) {
            for (int i = 0; i < cbMoneda.getItems().size(); i++) {
                if (cbMoneda.getItems().get(i).getPredeterminado() == true) {
                    cbMoneda.getSelectionModel().select(i);
                    monedaSimbolo = cbMoneda.getItems().get(i).getSimbolo();
                    break;
                }
            }
        }
        
        lblMonedaSubTotal.setText(monedaSimbolo);
        lblMonedaDescuento.setText(monedaSimbolo);
        lblMonedaSubTotalNuevo.setText(monedaSimbolo);
        lblMonedaTotal.setText(monedaSimbolo);

        cbRepresentante.setConverter(new javafx.util.StringConverter<RepresentanteTB>() {
            @Override
            public String toString(RepresentanteTB object) {
                return object.getInformacion();
            }

            @Override
            public RepresentanteTB fromString(String string) {
                return cbRepresentante.getItems().stream().filter(p -> p.getInformacion().equals(string)).findFirst().orElse(null);
            }
        });

        initTable();

    }

    private void initTable() {
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave() + "\n" + cellData.getValue().getNombreMarca()
        ));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCantidad(), 2)));
        tcCosto.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getPrecioCompraReal(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getDescuento(), 0) + "%"
        ));
        tcImpuesto.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getImpuestoArticuloName()));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getTotalImporte(), 2)));
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

    private void InitializationTransparentBackground() {
        Session.pane.setStyle("-fx-background-color: black");
        Session.pane.setTranslateX(0);
        Session.pane.setTranslateY(0);
        Session.pane.setPrefWidth(Session.WIDTH_WINDOW);
        Session.pane.setPrefHeight(Session.HEIGHT_WINDOW);
        Session.pane.setOpacity(0.7f);
        content.getChildren().add(Session.pane);
    }

    private void onViewRegister() throws IOException {
        if (txtProveedor.getText().isEmpty() && idProveedor.equalsIgnoreCase("")) {
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
//            InitializationTransparentBackground();
//            URL url = getClass().getResource(Tools.FX_FILE_COMPRASPROCESO);
//            FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
//            Parent parent = fXMLLoader.load(url.openStream());
////            Controlller here
//            FxCompraProcesoController controller = fXMLLoader.getController();
//            controller.setInitComprasController(this);
//            
//            Stage stage = FxWindow.StageLoaderModal(parent, "Pago de la compra", window.getScene().getWindow());
//            stage.setResizable(false);
//            stage.sizeToScene();
//            stage.setOnHiding((WindowEvent)-> content.getChildren().remove(Session.pane));
//            stage.show();
            
            CompraTB compraTB = new CompraTB();
            compraTB.setProveedor(idProveedor);
            compraTB.setRepresentante(idRepresentante);
            compraTB.setComprobante(cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento());
            compraTB.setNumeracion(cbNumeracion.getText().trim());
            compraTB.setTipoMoneda(cbMoneda.getSelectionModel().getSelectedIndex() >= 1
                    ? cbMoneda.getSelectionModel().getSelectedItem().getIdMoneda() : 0);
            compraTB.setFechaRegistro(Timestamp.valueOf(Tools.getDatePicker(tpFechaCompra) + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
            compraTB.setSubTotal(Double.parseDouble(lblSubTotal.getText()));
            compraTB.setDescuento(Double.parseDouble(lblDescuento.getText()));
            compraTB.setTotal(Double.parseDouble(lblTotal.getText()));
            compraTB.setObservaciones(txtObservaciones.getText().isEmpty()? "" : txtObservaciones.getText());
            compraTB.setNotas(txtNotas.getText().isEmpty()? "" : txtNotas.getText());
            String result = CompraADO.CrudCompra(compraTB, tvList, loteTBs);
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
                lblSubTotalNuevo.setText("0.00");
                lblTotal.setText("0.00");
                txtObservaciones.clear();
                txtNotas.clear();
                hbAgregarImpuesto.getChildren().clear();
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
        controller.setInitComprasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Artículo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
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
                    articuloTB.setClave(e.getClave());
                    articuloTB.setNombreMarca(e.getNombreMarca());
                    articuloTB.setCantidad(e.getCantidad());  
                    articuloTB.setPrecioCompra(e.getPrecioCompra());
                    articuloTB.setPrecioCompraReal(e.getPrecioCompraReal());
                    
                    articuloTB.setPrecioVentaNombre(e.getPrecioVentaNombre());
                    articuloTB.setPrecioVenta(e.getPrecioVenta());
                    articuloTB.setMargen(e.getMargen());
                    articuloTB.setUtilidad(e.getUtilidad());
                    
                    articuloTB.setPrecioVentaNombre2(e.getPrecioVentaNombre2());
                    articuloTB.setPrecioVenta2(e.getPrecioVenta2());
                    articuloTB.setMargen2(e.getMargen2());
                    articuloTB.setUtilidad2(e.getUtilidad2());
                    
                    articuloTB.setPrecioVentaNombre3(e.getPrecioVentaNombre3());
                    articuloTB.setPrecioVenta3(e.getPrecioVenta3());
                    articuloTB.setMargen3(e.getMargen3());
                    articuloTB.setUtilidad3(e.getUtilidad3());                   
                    
                    articuloTB.setDescuento(e.getDescuento());
                    articuloTB.setTotalImporte(e.getTotalImporte());
                    articuloTB.setImpuestoArticulo(e.getImpuestoArticulo());
                    articuloTB.setLote(e.isLote());
                    articuloTB.setUnidadVenta(e.getUnidadVenta());
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
                calculateTotals();
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
        controller.setInitComprasController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Seleccione un Proveedor", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(Session.pane);
        });
        stage.show();
        controller.fillCustomersTable("");
    }

    @FXML
    private void onKeyPressedRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            onViewRegister();
        }
    }

    @FXML
    private void onActionRegister(ActionEvent event) throws IOException {
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

    public void calculateTotals() {

        tvList.getItems().forEach(e -> subImporte += e.getSubImporte());
        lblSubTotal.setText(Tools.roundingValue(subImporte, 2));
        subImporte = 0;

        tvList.getItems().forEach(e -> descuento += e.getDescuentoSumado());
        lblDescuento.setText((Tools.roundingValue(descuento * (-1), 2)));
        descuento = 0;

        tvList.getItems().forEach(e -> subTotalImporte += e.getTotalImporte());
        lblSubTotalNuevo.setText(Tools.roundingValue(subTotalImporte, 2));
        subTotalImporte = 0;

        hbAgregarImpuesto.getChildren().clear();
        boolean addElement = false;
        double sumaElement = 0;
        for (int k = 0; k < arrayArticulos.size(); k++) {
            for (int i = 0; i < tvList.getItems().size(); i++) {
                if (arrayArticulos.get(k).getIdImpuesto() == tvList.getItems().get(i).getImpuestoArticulo()) {
                    addElement = true;
                    sumaElement += tvList.getItems().get(i).getImpuestoSumado();
                }
            }
            if (addElement) {
                addElementImpuesto(arrayArticulos.get(k).getIdImpuesto() + "", arrayArticulos.get(k).getNombre(), monedaSimbolo, Tools.roundingValue(sumaElement, 2));
                addElement = false;
                sumaElement = 0;
            }
        }

        tvList.getItems().forEach(e -> totalImporte += e.getTotalImporte());
        lblTotal.setText(Tools.roundingValue(totalImporte, 2));
        totalImporte = 0;
    }

    private void addElementImpuesto(String id, String titulo, String moneda, String total) {
        Label label = new Label(titulo);
        label.setStyle("-fx-text-fill:#1a2226;");
        label.getStyleClass().add("labelRoboto14");

        Text text = new Text(moneda);
        text.getStyleClass().add("labelRobotoMedium16");
        Text text1 = new Text(total);
        text1.getStyleClass().add("labelRobotoMedium16");

        HBox hBox = new HBox(text, text1);
        hBox.setSpacing(5);
        VBox vBox = new VBox(label, hBox);
        vBox.setStyle("-fx-spacing: 0.8333333333333334em;");
        vBox.setId(id);

        hbAgregarImpuesto.getChildren().add(vBox);
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (event.getClickCount() == 2) {
            onViewEdit();
        }
    }

    @FXML
    private void onActionMoneda(ActionEvent event) {
        if (cbMoneda.getSelectionModel().getSelectedIndex() >= 1) {
            monedaSimbolo = cbMoneda.getSelectionModel().getSelectedItem().getSimbolo();
            lblMonedaSubTotal.setText(monedaSimbolo);
            lblMonedaSubTotalNuevo.setText(monedaSimbolo);
            lblMonedaDescuento.setText(monedaSimbolo);
            lblMonedaTotal.setText(monedaSimbolo);
            calculateTotals();
        }
    }

    public TableView<ArticuloTB> getTvList() {
        return tvList;
    }

    public ObservableList<LoteTB> getLoteTBs() {
        return loteTBs;
    }

    public HBox getHbAgregarImpuesto() {
        return hbAgregarImpuesto;
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }



}
