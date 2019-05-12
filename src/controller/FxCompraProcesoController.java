package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    private RadioButton rbContado;
    @FXML
    private RadioButton rbCredito;
    @FXML
    private TextField txtProveedor;
    @FXML
    private ComboBox<PlazosTB> cbPlazos;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtMonto;
    @FXML
    private TextField txtObservacion;
    @FXML
    private Label lblDeudaPendiente;

    private FxCompraController compraController;

    private CompraTB compraTB = null;

    private TableView<ArticuloTB> tvList;

    private ObservableList<LoteTB> loteTBs;

    private PagoProveedoresTB pagoProveedoresTB;

    private String simboloMoneda;
    
    private double deudaPendiente;
    
    private int dias;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup group = new ToggleGroup();
        rbContado.setToggleGroup(group);
        rbCredito.setToggleGroup(group);
        Tools.actualDate(Tools.getDate(), dpFecha);
        setInitializePlazos();
        pagoProveedoresTB = new PagoProveedoresTB();
    }

    public void setInitializePlazos() {
        cbPlazos.getItems().clear();
        PlazosADO.GetTipoPlazoCombBox().forEach(e -> {
            this.cbPlazos.getItems().add(new PlazosTB(e.getIdPlazos(), e.getNombre(), e.getDias(), e.getEstado(), e.getPredeterminado()));
        });
        cbPlazos.getSelectionModel().select(0);
        dias = cbPlazos.getSelectionModel().getSelectedItem().getDias();
        System.out.println("valor "+dias);
    }

    public void setLoadProcess(CompraTB compraTB, TableView<ArticuloTB> tvList, ObservableList<LoteTB> loteTBs, String total, String proveedor, String simboloMoneda) {
        this.compraTB = compraTB;
        this.tvList = tvList;
        this.loteTBs = loteTBs;
        lblTotal.setText(simboloMoneda + " " + total);
        txtProveedor.setText(proveedor);
        this.simboloMoneda = simboloMoneda;
        txtEfectivo.setText(total);
        this.lblDeudaPendiente.setText(simboloMoneda + " 0000.00");
    }

    private void executeCrud(int tipoCompra,int estadoCompra) {
        compraTB.setTipo(tipoCompra);
        compraTB.setEstado(estadoCompra);

        if (rbCredito.isSelected()) {
            if (!this.txtMonto.getText().equalsIgnoreCase("")) {

                pagoProveedoresTB.setPlazos(cbPlazos.getSelectionModel().getSelectedItem().getNombre());
                pagoProveedoresTB.setDias(dias);
                pagoProveedoresTB.setFechaActual(Timestamp.valueOf(Tools.getDatePicker(dpFecha) + " " + Tools.getDateHour().toLocalDateTime().toLocalTime()));
                pagoProveedoresTB.setObservacion(this.txtObservacion.getText().equals("") ? "ninguna".toUpperCase() : this.txtObservacion.getText().toUpperCase());
                pagoProveedoresTB.setEstado("activo".toUpperCase());
                pagoProveedoresTB.setIdProveedor(compraTB.getProveedor());
                pagoProveedoresTB.setIdEmpleado(Session.USER_ID);

                String result = CompraADO.CrudCompra(compraTB, tvList, loteTBs, pagoProveedoresTB);
                if (result.equalsIgnoreCase("register")) {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Compra", "Se registró correctamente la compra.", false);
                    Tools.Dispose(window);
                    compraController.clearComponents();
                } else {
                    Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Compra", result, false);
                }

            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Ingrese un valor numerico no negativo.", false);
                this.txtMonto.requestFocus();
            }

        } else {
            String result = CompraADO.CrudCompra(compraTB, tvList, loteTBs, pagoProveedoresTB);
            if (result.equalsIgnoreCase("register")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Compra", "Se registró correctamente la compra.", false);
                Tools.Dispose(window);
                compraController.clearComponents();
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Compra", result, false);
            }
        }

    }

    private void onEventProcess() {
        if (rbContado.isSelected()) {
            if (!Tools.isNumeric(txtEfectivo.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo efectivo.", false);
                txtEfectivo.requestFocus();
            } else {
                executeCrud(1, 1);
            }
        } else if (rbCredito.isSelected()) {
            if (!Tools.isNumeric(txtMonto.getText())) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo monto inicial.", false);
                txtMonto.requestFocus();
            } else if (dpFecha.getValue() == null) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Complete el campo fecha de vencimiento.", false);
                dpFecha.requestFocus();
            } else {
                executeCrud(2, 2);
            }
        }

    }

    public void openWindowAddPlazo() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PLAZOS);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxPlazosController controller = fXMLLoader.getController();
        controller.setInitCompraVentaProcesoController(this, null, "compra");
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
        hbPagoContado.setDisable(false);
    }

    @FXML
    private void onActionRbCredito(ActionEvent event) {
        hbPagoContado.setDisable(true);
        vbPagoCredito.setDisable(false);
        this.txtMonto.requestFocus();
    }
    
    @FXML
    private void OnMouseClickedPlazos(MouseEvent event) throws IOException {
        openWindowAddPlazo();
    }

    @FXML
    private void OnActionPlazos(ActionEvent event) {
        onEventPagoProveedores();
    }
    
    
    @FXML
    private void onKeyReleasedMonto(KeyEvent event) {
        onEventPagoProveedores();
    }

    @FXML
    private void onKeyTypedMonto(KeyEvent event) {
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9') && (c != '\b') && (c != '.')) {
            event.consume();
        }
        if (c == '.' && txtMonto.getText().contains(".")) {
            event.consume();
        }
    }

    private void onEventPagoProveedores() {
        if (Tools.isNumeric(txtMonto.getText())) {
            if (Double.parseDouble(txtMonto.getText()) >= 0) {

                pagoProveedoresTB.setMontoTotal(compraTB.getTotal().get());
                pagoProveedoresTB.setMontoActual(Double.parseDouble(txtMonto.getText()));
                pagoProveedoresTB.setCuotaActual(1);
                
                deudaPendiente = compraTB.getTotal().get() - Double.parseDouble(txtMonto.getText());
                this.lblDeudaPendiente.setText(simboloMoneda+" "+Tools.roundingValue(deudaPendiente, 2));
                
            }
        }
        dias = cbPlazos.getSelectionModel().getSelectedItem().getDias();              
        
    }

    public void setInitCompraController(FxCompraController compraController) {
        this.compraController = compraController;
    }

}