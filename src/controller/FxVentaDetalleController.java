package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.DetalleVentaTB;
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

    private FxVentaRealizadasController ventaRealizadasController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcDescripcion.setCellValueFactory(cellData -> cellData.getValue().getArticuloTB().getNombreMarca());
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getCantidad(), 2)));
        tcMedida.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getArticuloTB().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel"));
        tcPrecio.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getPrecioUnitario(), 2)));
        tcDescuento.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getDescuento(), 2)));
        tcImporte.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getImporte(), 2)));
    }

    public void fillVentasDetalleTable(String value) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        Task<ObservableList<DetalleVentaTB>> task = new Task<ObservableList<DetalleVentaTB>>() {
            @Override
            public ObservableList<DetalleVentaTB> call() {
                return VentaADO.ListVentasDetalle(value);
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
            lblLoad.setVisible(false);
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }
    }

    public void setInitComponents(LocalDateTime fechaRegistro, String cliente, String comprobanteName, String serie, String numeracion, String estado, String observaciones, String total,String idVenta) {
        lblFechaVenta.setText(fechaRegistro.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        lblCliente.setText(cliente);
        lblComprobante.setText(comprobanteName);
        lblSerie.setText(serie + "-" + numeracion);
        lblEstado.setText(estado);
        lblObservaciones.setText(observaciones);
        lblTotal.setText(total);
        fillVentasDetalleTable(idVenta);
    }

    public void setInitVentasController(FxVentaRealizadasController ventaRealizadasController) {
        this.ventaRealizadasController = ventaRealizadasController;
    }

}
