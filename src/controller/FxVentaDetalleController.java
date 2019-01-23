package controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.DBUtil;
import model.DetalleVentaTB;
import model.EmpleadoTB;
import model.VentaADO;

public class FxVentaDetalleController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private Label lblLoad;
    @FXML
    private Text lblFechaVenta;
    @FXML
    private Text lblComprobante;
    @FXML
    private TableView<DetalleVentaTB> tvList;
    @FXML
    private TableColumn<DetalleVentaTB, Integer> tcId;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcDescripcion;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcCantidad;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcImporte;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcMedida;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcPrecio;
    @FXML
    private TableColumn<DetalleVentaTB, String> tcDescuento;
    @FXML
    private Text lblTotal;
    @FXML
    private Text lblCliente;
    @FXML
    private Text lblEstado;
    @FXML
    private Text lblObservaciones;
    @FXML
    private Text lblVendedor;
    @FXML
    private Text lblSerie;

    private String idVenta;

    private double subTotal;

    private FxVentaRealizadasController ventaRealizadasController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcDescripcion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getNombreMarca()));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getCantidad(), 2)));
        tcMedida.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel"));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getPrecioUnitario(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getDescuento(), 2)));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getImporte(), 2)));
    }

    private void fillVentasDetalleTable(String value) throws SQLException {
        tvList.setItems(VentaADO.ListVentasDetalle(value));
        lblLoad.setVisible(false);
        tvList.getItems().forEach(e -> {
            subTotal += e.getImporte();
        });
        lblTotal.setText("S/. " + Tools.roundingValue(subTotal, 2));
    }

    public void setInitComponents(LocalDateTime fechaRegistro, String cliente, String comprobanteName, String serie, String numeracion, String estado, String observaciones, String total, String idVenta) {
        lblFechaVenta.setText(fechaRegistro.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        lblCliente.setText(cliente);
        lblComprobante.setText(comprobanteName);
        lblSerie.setText(serie + "-" + numeracion);
        lblEstado.setText(estado);
        lblObservaciones.setText(observaciones);
        this.idVenta = idVenta;
        DBUtil.dbConnect();
        if (DBUtil.getConnection() != null) {
            try {
                EmpleadoTB empleadoTB = VentaADO.CabeceraVenta(idVenta);
                if (empleadoTB != null) {
                    lblVendedor.setText(empleadoTB.getApellidos() + " " + empleadoTB.getNombres());
                }
                fillVentasDetalleTable(idVenta);
            } catch (SQLException ex) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.ERROR, "Detalle de venta", "No se pudo completar la petición, intente nuevamente.\n Error en: " + ex.getLocalizedMessage(), false);
            } finally {
                DBUtil.dbDisconnect();
            }
        }

    }

    @FXML
    private void onKeyPressedCancelar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

        }
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
        short validate = Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.CONFIRMATION, "Detalle de ventas", "¿Está seguro de cancelar la venta?", true);
        if (validate == 1) {
            String result = VentaADO.CancelTheSale(idVenta, tvList);
            if (result.equalsIgnoreCase("update")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", "Se ha cancelado con éxito", false);
                ventaRealizadasController.fillVentasTable("");
                Tools.Dispose(window);
            } else if (result.equalsIgnoreCase("scrambled")) {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Detalle de venta", "Ya está cancelada la venta!", false);
            } else {
                Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.INFORMATION, "Detalle de venta", result, false);

            }
        }
    }

    public void setInitVentasController(FxVentaRealizadasController ventaRealizadasController) {
        this.ventaRealizadasController = ventaRealizadasController;
    }

}
