package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
import model.PersonaADO;
import model.PersonaTB;
import model.ProveedorADO;

public class FxComprasController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private TextField txtProveedor;
    @FXML
    private ComboBox<PersonaTB> cbRepresentante;
    @FXML
    private ComboBox<DetalleTB> cbComprobante;
    @FXML
    private TextField cbNumeracion;
    @FXML
    private DatePicker tpFechaCompra;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, Integer> tcId;
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

    private AnchorPane content;

    private String idProveedor;

    private String idRepresentante;

    private double subTotal;

    private double descuento;

    private double igv;

    private double total;

    private int count;
    
    private double sumarcompra;
       
    private double sumardescuento;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idProveedor = idRepresentante = "";
        count = 1;
        Tools.actualDate(Tools.getDate(), tpFechaCompra);
        DetalleADO.GetDetailIdName("2", "0009", "").forEach(e -> {
            cbComprobante.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbComprobante.getSelectionModel().select(0);
        initTable();
    }

    private void initTable() {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
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
                Tools.roundingValue(cellData.getValue().getTotal().get(), 2)));
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
        } else {
            CompraTB compraTB = new CompraTB();
            compraTB.setProveedor(idProveedor);
            compraTB.setRepresentante(idRepresentante);
            compraTB.setComprobante(cbComprobante.getSelectionModel().getSelectedItem().getIdDetalle().get());
            compraTB.setNumeracion(cbNumeracion.getText().trim());
            compraTB.setFechaRegistro(Timestamp.valueOf(Tools.getDatePicker(tpFechaCompra) + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
            compraTB.setSubTotal(subTotal);
            compraTB.setDescuento(descuento);
            compraTB.setIgv(igv);
            compraTB.setTotal(total);
            String result = CompraADO.CrudEntity(compraTB);
            if (result.equalsIgnoreCase("register")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Compras", "Registrado correctamente la compra.", false);
                idProveedor = idRepresentante = "";
                txtProveedor.clear();
                cbRepresentante.getItems().clear();
                cbNumeracion.clear();
                Tools.actualDate(Tools.getDate(), tpFechaCompra);
                lblSubTotal.setText("S/. 0.00");
                lblDescuento.setText("S/. 0.00");
                lblIgv.setText("S/. 0.00");
                lblTotal.setText("S/. 0.00");
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Compras", result, false);

            }
        }
    }

    private void onViewAdd() throws IOException {
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
        stage.setOnHiding((WindowEvent WindowEvent) -> {
            content.getChildren().remove(SysSoft.pane);
        });
        stage.show();
        controller.fillProvidersTable("");
    }

    private void onViewEdit() {

    }

    private void onViewRemove() {

    }

    private void onViewProviders() throws IOException {
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
            onViewAdd();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        onViewAdd();
    }

    @FXML
    private void onKeyPressedEdit(KeyEvent event) {
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
            onViewProviders();
        }
    }

    @FXML
    private void onActionProviders(ActionEvent event) throws IOException {
        onViewProviders();
    }

    public void setInitComprasValue(String... value) {
        idProveedor = ProveedorADO.GetProveedorId(value[0]);
        txtProveedor.setText(value[1]);
        cbRepresentante.getItems().clear();
        PersonaADO.ListRepresentantes(idProveedor, "").forEach(e -> {
            cbRepresentante.getItems().add(new PersonaTB(e.getNumeroDocumento().get(), (e.getApellidoPaterno().get() + " "
                    + e.getApellidoMaterno() + " "
                    + e.getPrimerNombre() + " "
                    + e.getSegundoNombre())));
        });
    }

    public void setContent(AnchorPane content) {
        this.content = content;
    }

    @FXML
    private void onKeyTypedNumeracion(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
            event.consume();
        }
    }

    @FXML
    private void onActionRepresentante(ActionEvent event) {
        if (cbRepresentante.getSelectionModel().getSelectedIndex() >= 0) {
            idRepresentante = PersonaADO.GetPersonasId(cbRepresentante.getSelectionModel().getSelectedItem().getNumeroDocumento().get());
        }
    }

    public TableView<ArticuloTB> getTvList() {
        return tvList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCalculateTotals() {
        tvList.getItems().forEach(tran -> sumarcompra += tran.getTotal().get());
        double temtotal = Double.parseDouble(Tools.roundingValue(sumarcompra, 2));           
        
        tvList.getItems().forEach(tran -> sumardescuento += tran.getDescuento().get());
        lblDescuento.setText("-"+Tools.roundingValue(sumardescuento, 2));
        
        double descuentoTotal = temtotal - sumardescuento; 
        lblSubTotal.setText(Tools.roundingValue(temtotal, 2));
        
        double subtotal = Tools.calculateValueNeto(18,descuentoTotal);
        lblGravada.setText(Tools.roundingValue(subtotal, 2));
        
        double impuesto = Tools.calculateTax(18,subtotal);
        lblIgv.setText(Tools.roundingValue(impuesto, 2));    
        
        lblTotal.setText("S/. "+Tools.roundingValue(descuentoTotal, 2));   
        
        
        sumarcompra=sumardescuento=0;
    }

}
