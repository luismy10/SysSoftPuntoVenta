package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ArticuloTB;
import model.CompraADO;
import model.CompraTB;
import model.DetalleADO;
import model.DetalleTB;
import model.LoteTB;
import model.PagoProveedoresTB;
import model.PlazosADO;
import model.PlazosTB;

public class FxCompraProcesoController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Text lblTotal;
    @FXML
    private TextField txtEfectivo;
    @FXML
    private HBox hbPagoContado;
    @FXML
    private VBox vbPagoCredito;
    @FXML
    private VBox vbOtroPago;
    @FXML
    private RadioButton rbContado;
    @FXML
    private RadioButton rbCredito;
    @FXML
    private RadioButton rbOtroPago;
    @FXML
    private TextField txtProveedor;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox<PlazosTB> cbPlazos;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtMonto;
    @FXML
    private TextField txtCuotas;

    private FxCompraController compraController;

    private CompraTB compraTB = null;

    private TableView<ArticuloTB> tvList;

    private ObservableList<LoteTB> loteTBs;
    
    private int diasPlazo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup group = new ToggleGroup();
        rbContado.setToggleGroup(group);
        rbCredito.setToggleGroup(group);
        rbOtroPago.setToggleGroup(group);
        System.out.println(hbPagoContado.isDisable());
        System.out.println(vbPagoCredito.isDisable());
        System.out.println(vbOtroPago.isDisable());
        Tools.actualDate(Tools.getDate(), dpFecha);
        setInitializePlazos();   
    }
    
    public void setInitializePlazos(){
        cbPlazos.getItems().clear();
        PlazosADO.GetTipoPlazoCombBox().forEach(e -> {
            this.cbPlazos.getItems().add(new PlazosTB(e.getIdPlazos(), e.getNombre(), e.getDias(), e.getEstado(), e.getPredeterminado()));
        });
        cbPlazos.getSelectionModel().select(0);
        diasPlazo = cbPlazos.getSelectionModel().getSelectedItem().getDias();
        
    }

    public void setLoadProcess(CompraTB compraTB, TableView<ArticuloTB> tvList, ObservableList<LoteTB> loteTBs, String total, String proveedor) {
        this.compraTB = compraTB;
        this.tvList = tvList;
        this.loteTBs = loteTBs;
        lblTotal.setText(total);
        txtProveedor.setText(proveedor);
    }

    private void executeCrud(String estadoCompra, String tipoCompra) {
        
        double deuda_pendiente = 0;
        double valor_cuota = 0;
        
        PagoProveedoresTB pagoProveedoresTB = new PagoProveedoresTB(); 
        pagoProveedoresTB.setMontoTotal(compraTB.getTotal().get());
        pagoProveedoresTB.setMontoActual(Double.parseDouble(txtMonto.getText()));
        pagoProveedoresTB.setCuotaTotal(Integer.parseInt(txtCuotas.getText()));
        pagoProveedoresTB.setCuotaActual(Integer.parseInt(txtMonto.getText())> 0 ? 1:0);
        
        deuda_pendiente = compraTB.getTotal().get() - Double.parseDouble(txtMonto.getText());
        valor_cuota = deuda_pendiente / Integer.parseInt(txtCuotas.getText());
        
        pagoProveedoresTB.setPlazos(cbPlazos.getSelectionModel().getSelectedItem().getNombre());
        pagoProveedoresTB.setValorCuota(valor_cuota);
        
        pagoProveedoresTB.setFechaInicial(Timestamp.valueOf(Tools.getDatePicker(dpFecha) + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
        pagoProveedoresTB.setFechaActual(Timestamp.valueOf(Tools.getDatePicker(dpFecha) + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
        
        LocalDate fecha_final = LocalDate.parse(Tools.getDatePicker(dpFecha)).plusDays(diasPlazo * Integer.parseInt(txtCuotas.getText()));
        
        pagoProveedoresTB.setFechaFinal(Timestamp.valueOf(fecha_final + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
        pagoProveedoresTB.setObservacion("ninguno".toUpperCase());
        pagoProveedoresTB.setEstado("activo".toUpperCase());
        pagoProveedoresTB.setIdProveedor(compraTB.getProveedor());
        
        compraTB.setEstadoCompra(estadoCompra);
        compraTB.setTipoCompra(tipoCompra);
        
        String result = CompraADO.CrudCompra(compraTB, tvList, loteTBs, pagoProveedoresTB);
        if (result.equalsIgnoreCase("register")) {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Compra", "Se registró correctamente la compra.", false);
            Tools.Dispose(window);
            compraController.clearComponents();
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Compra", result, false);
        }
    }

    private void onEventProcess() {
        
        if (rbContado.isSelected()) {
            if (!Tools.isNumeric(txtEfectivo.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo efectivo.", false);
                txtEfectivo.requestFocus();
            } else {
                executeCrud("PAGADO", "CONTADO");
            }
        } else if (rbCredito.isSelected()) {
            if (!Tools.isNumeric(txtMonto.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo crédito.", false);
                txtMonto.requestFocus();
            } else if (!Tools.isNumeric(txtCuotas.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo días de crédito.", false);
                txtCuotas.requestFocus();
            } else if (dpFecha.getValue() == null) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo fecha de vencimiento.", false);
                dpFecha.requestFocus();
            } else {
                executeCrud("PENDIENTE", "CREDITO");
            }
        } else if (rbOtroPago.isSelected()) {
            if (txtDescripcion.getText().isEmpty()) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo descripción.", false);
                txtDescripcion.requestFocus();
            } else {
                executeCrud("PENDIENTE", "CREDITO");
            }
        }

    }
    
    private void openWindowAddPlazo() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PLAZOS);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxPlazosController controller = fXMLLoader.getController();
        controller.setInitCompraProcesoController(this);
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agegar nuevo plazo", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    private void onKeyTypedEfectivo(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtEfectivo.getText().contains(".") || c == '-' && txtEfectivo.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyPressedAceptar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onEventProcess();
        }
    }

    @FXML
    private void onActionAceptar(ActionEvent event) {
        onEventProcess();
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

    @FXML
    private void onActionRbContado(ActionEvent event) {
        vbPagoCredito.setDisable(true);
        vbOtroPago.setDisable(true);
        hbPagoContado.setDisable(false);
    }

    @FXML
    private void onActionRbCredito(ActionEvent event) {
        hbPagoContado.setDisable(true);
        vbOtroPago.setDisable(true);
        vbPagoCredito.setDisable(false);
    }

    @FXML
    private void onActionRbOtroPago(ActionEvent event) {
        hbPagoContado.setDisable(true);
        vbPagoCredito.setDisable(true);
        vbOtroPago.setDisable(false);
    }

    public void setInitCompraController(FxCompraController compraController) {
        this.compraController = compraController;
    }

    @FXML
    private void OnActionPlazos(ActionEvent event) {
        diasPlazo = cbPlazos.getSelectionModel().getSelectedItem().getDias();
    }

    @FXML
    private void onKeyTypedMonto(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.') && (c != '-')) {
            event.consume();
        }
        if (c == '.' && txtMonto.getText().contains(".") || c == '-' && txtMonto.getText().contains("-")) {
            event.consume();
        }
    }

    @FXML
    private void onKeyTypedCuotas(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b')) {
            event.consume();
        }
    }

    @FXML
    private void OnMouseClickedPlazos(MouseEvent event) throws IOException {
        this.openWindowAddPlazo();
    }

}
