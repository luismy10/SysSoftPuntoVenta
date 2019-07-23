package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.Utilidad;
import model.UtilidadADO;

public class FxVentasUtilidadesController implements Initializable {

    @FXML
    private Label lblLoad;
    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Utilidad> tvList;
    @FXML
    private TableColumn<Utilidad, Integer> tcId;
    @FXML
    private TableColumn<Utilidad, String> tcDescripcion;
    @FXML
    private TableColumn<Utilidad, String> tcCantidad;
    @FXML
    private TableColumn<Utilidad, String> tcCostoUnitario;
    @FXML
    private TableColumn<Utilidad, String> tcPrecioUnitario;
    @FXML
    private TableColumn<Utilidad, String> tcCostoTotal;
    @FXML
    private TableColumn<Utilidad, String> tcPrecioTotal;
    @FXML
    private TableColumn<Utilidad, String> tcUtilidad;
    @FXML
    private Text lblImporteC;
    @FXML
    private Text lblImporteCompra;
    @FXML
    private Text lblMonedaImporteV;
    @FXML
    private Text lblImporteVenta;
    @FXML
    private HBox hbAgregarImpuesto;
    @FXML
    private Text lblMonedaUtilidad;
    @FXML
    private Text lblUtilidad;

    private AnchorPane windowinit;

    private boolean validationSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcDescripcion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getClave() + "\n" + cellData.getValue().getNombreMarca()));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().isValorInventario()
                ? Tools.roundingValue(cellData.getValue().getCantidad(), 0) + " " + cellData.getValue().getUnidadCompra()
                : Tools.roundingValue(cellData.getValue().getCantidadGranel(), 2) + " " + cellData.getValue().getUnidadCompra()
        ));
        tcCostoUnitario.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getCostoVenta(), 2)));
        tcPrecioUnitario.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().isValorInventario()
                ? Tools.roundingValue(cellData.getValue().getPrecioVenta(), 2)
                : Tools.roundingValue(cellData.getValue().getPrecioVentaGranel(), 2)
        ));
        tcCostoTotal.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getCostoVentaTotal(), 2)));
        tcPrecioTotal.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getPrecioVentaTotal(), 2)));
        tcUtilidad.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getSimboloMoneda() + " " + Tools.roundingValue(cellData.getValue().getUtilidad(), 2)));

        tcId.prefWidthProperty().bind(tvList.widthProperty().multiply(0.06));
        tcDescripcion.prefWidthProperty().bind(tvList.widthProperty().multiply(0.20));
        tcCantidad.prefWidthProperty().bind(tvList.widthProperty().multiply(0.12));
        tcCostoUnitario.prefWidthProperty().bind(tvList.widthProperty().multiply(0.12));
        tcPrecioUnitario.prefWidthProperty().bind(tvList.widthProperty().multiply(0.12));
        tcCostoTotal.prefWidthProperty().bind(tvList.widthProperty().multiply(0.12));
        tcPrecioTotal.prefWidthProperty().bind(tvList.widthProperty().multiply(0.12));
        tcUtilidad.prefWidthProperty().bind(tvList.widthProperty().multiply(0.12));

        Tools.actualDate(Tools.getDate(), dtFechaInicial);
        Tools.actualDate(Tools.getDate(), dtFechaFinal);
    }

    public void loadInit() {
        if (dtFechaInicial != null && dtFechaFinal != null) {
            fillUtilidadTable((short) 1, Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal));
        }
    }

    public void fillUtilidadTable(short option, String fechaInicial, String fechaFinal) {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        Task<ObservableList<Utilidad>> task = new Task<ObservableList<Utilidad>>() {
            @Override
            public ObservableList<Utilidad> call() {
                return UtilidadADO.listUtilidadVenta(option, fechaInicial, fechaFinal, "");
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<Utilidad>) task.getValue());
            lblLoad.setVisible(false);
            validationSearch = false;
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
            validationSearch = false;
        });
        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
            validationSearch = true;
        });
        exec.execute(task);
        if (!exec.isShutdown()) {
            exec.shutdown();
        }
    }

    @FXML
    private void onActionFechaInicial(ActionEvent event) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillUtilidadTable((short) 1, Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal));
        }
    }

    @FXML
    private void onActionFechaFinal(ActionEvent event) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillUtilidadTable((short) 1, Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal));
        }
    }

    public void setContent(AnchorPane windowinit) {
        this.windowinit = windowinit;
    }

}
