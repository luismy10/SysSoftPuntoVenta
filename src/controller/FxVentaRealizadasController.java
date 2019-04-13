package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
import javafx.scene.layout.VBox;
import model.DetalleADO;
import model.DetalleTB;
import model.TipoDocumentoADO;
import model.TipoDocumentoTB;
import model.VentaADO;
import model.VentaTB;

public class FxVentaRealizadasController implements Initializable {

    @FXML
    private VBox window;
    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private Label lblLoad;
    @FXML
    private TableView<VentaTB> tvList;
    @FXML
    private TableColumn<VentaTB, Integer> tcId;
    @FXML
    private TableColumn<VentaTB, String> tcFechaVenta;
    @FXML
    private TableColumn<VentaTB, String> tcCliente;
    @FXML
    private TableColumn<VentaTB, String> tcTipo;
    @FXML
    private TableColumn<VentaTB, String> tcEstado;
    @FXML
    private TableColumn<VentaTB, String> tcSerie;
    @FXML
    private TableColumn<VentaTB, String> tcTotal;
    @FXML
    private TableColumn<VentaTB, String> tcObservacion;
    @FXML
    private ComboBox<DetalleTB> cbEstado;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<TipoDocumentoTB> cbComprobante;

    private AnchorPane windowinit;

    private AnchorPane vbContent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        tcFechaVenta.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getFechaVenta().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))+"\n"+
                cellData.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("hh:mm:ss a"))
        ));
        tcCliente.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getCliente()));
        tcTipo.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getTipoName()));
        tcEstado.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getEstadoName()));
        tcSerie.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getComprobanteName() + "\n" + cellData.getValue().getSerie() + "-" + cellData.getValue().getNumeracion()));
        tcTotal.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getMonedaName() + " " + Tools.roundingValue(cellData.getValue().getTotal(), 2)));
        tcObservacion.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getObservaciones()));

        cbEstado.getItems().add(new DetalleTB(new SimpleIntegerProperty(0), new SimpleStringProperty("TODOS")));
        DetalleADO.GetDetailIdName("2", "0009", "").forEach(e -> {
            cbEstado.getItems().add(new DetalleTB(e.getIdDetalle(), e.getNombre()));
        });
        cbEstado.getSelectionModel().select(0);

        cbComprobante.getItems().add(new TipoDocumentoTB(0, "TODOS"));
        TipoDocumentoADO.GetDocumentoCombBox().forEach(e -> {
            cbComprobante.getItems().add(new TipoDocumentoTB(e.getIdTipoDocumento(), e.getNombre()));
        });
        cbComprobante.getSelectionModel().select(0);

        Tools.actualDate(Tools.getDate(), dtFechaInicial);
        Tools.actualDate(Tools.getDate(), dtFechaFinal);

    }

    public void loadInit() {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillVentasTable((short) 0, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),
                    cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                    cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
        }
    }

    private void fillVentasTable(short opcion, String value, String fechaInicial, String fechaFinal, int comprobante, int estado) {
        ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        Task<ObservableList<VentaTB>> task = new Task<ObservableList<VentaTB>>() {
            @Override
            public ObservableList<VentaTB> call() {
                return VentaADO.ListVentas(opcion, value, fechaInicial, fechaFinal, comprobante, estado);
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

    private void openWindowDetalleVenta() throws IOException {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource(Tools.FX_FILE_VENTADETALLE));
            ScrollPane node = fXMLLoader.load();
            //Controlller here
            FxVentaDetalleController controller = fXMLLoader.getController();
            controller.setInitVentasController(this, windowinit, vbContent);
            controller.setInitComponents(tvList.getSelectionModel().getSelectedItem().getIdVenta());
            //
            vbContent.getChildren().clear();
            AnchorPane.setLeftAnchor(node, 0d);
            AnchorPane.setTopAnchor(node, 0d);
            AnchorPane.setRightAnchor(node, 0d);
            AnchorPane.setBottomAnchor(node, 0d);
            vbContent.getChildren().add(node);
        } else {
            Tools.AlertMessage(window.getScene().getWindow(), Alert.AlertType.WARNING, "Compra", "Debe seleccionar una compra de la lista", false);
        }
    }

    @FXML
    private void onKeyPressedSearch(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!tvList.getItems().isEmpty()) {
                tvList.requestFocus();
                tvList.getSelectionModel().select(0);
            }
        }
    }

    @FXML
    private void onKeyRelasedSearch(KeyEvent event) {
        fillVentasTable((short) 1, txtSearch.getText().trim(), "", "", 0, 0);
    }

    @FXML
    private void onActionFechaInicial(ActionEvent event) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillVentasTable((short) 0, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),
                    cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                    cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
        }
    }

    @FXML
    private void onActionFechaFinal(ActionEvent event) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillVentasTable((short) 0, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),
                    cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                    cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
        }
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowDetalleVenta();
        }
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            openWindowDetalleVenta();
        }
    }

    @FXML
    private void onKeyPressedMostar(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowDetalleVenta();
        }
    }

    @FXML
    private void onActionMostrar(ActionEvent event) throws IOException {
        openWindowDetalleVenta();
    }

    @FXML
    private void onKeyPressedRecargar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            cbEstado.getSelectionModel().select(0);
            Tools.actualDate(Tools.getDate(), dtFechaInicial);
            Tools.actualDate(Tools.getDate(), dtFechaFinal);
            if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
                fillVentasTable((short) 0, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),
                        cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                        cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
            }
        }
    }

    @FXML
    private void onActionRecargar(ActionEvent event) {
        cbEstado.getSelectionModel().select(0);
        Tools.actualDate(Tools.getDate(), dtFechaInicial);
        Tools.actualDate(Tools.getDate(), dtFechaFinal);
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillVentasTable((short) 0, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),
                    cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                    cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
        }
    }

    @FXML
    private void onActionComprobante(ActionEvent event) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillVentasTable((short) 0, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),
                    cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                    cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
        }
    }

    @FXML
    private void onActionEstado(ActionEvent event) {
        if (dtFechaInicial.getValue() != null && dtFechaFinal.getValue() != null) {
            fillVentasTable((short) 0, "", Tools.getDatePicker(dtFechaInicial), Tools.getDatePicker(dtFechaFinal),
                    cbComprobante.getSelectionModel().getSelectedItem().getIdTipoDocumento(),
                    cbEstado.getSelectionModel().getSelectedItem().getIdDetalle().get());
        }
    }

    public VBox getWindow() {
        return window;
    }

    public void setContent(AnchorPane windowinit, AnchorPane vbContent) {
        this.windowinit = windowinit;
        this.vbContent = vbContent;
    }

}
