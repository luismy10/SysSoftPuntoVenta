package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import model.ArticuloADO;
import model.ArticuloTB;

public class FxInventarioGeneralController implements Initializable {

    @FXML
    private Label lblLoad;
    @FXML
    private TableView<ArticuloTB> tvList;
    @FXML
    private TableColumn<ArticuloTB, Integer> tcNumero;
    @FXML
    private TableColumn<ArticuloTB, String> tcDescripcion;
    @FXML
    private TableColumn<ArticuloTB, String> tcCantidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcUnidad;
    @FXML
    private TableColumn<ArticuloTB, String> tcEstado;
    @FXML
    private TableColumn<ArticuloTB, String> tcCostoPromedio;
    @FXML
    private TableColumn<ArticuloTB, String> tcTotal;
    @FXML
    private Label lblValoTotal;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcNumero.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDescripcion.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave() + "\n" + cellData.getValue().getNombreMarca()
        ));
        tcCantidad.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCantidad(), 2)
        ));
        tcUnidad.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadCompraName()
        ));
        tcEstado.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getEstadoName().get()
        ));
        tcCostoPromedio.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCostoCompra(), 2)
        ));
        tcTotal.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getTotalImporte(), 2)
        ));

        tcNumero.prefWidthProperty().bind(tvList.widthProperty().multiply(0.05));
        tcDescripcion.prefWidthProperty().bind(tvList.widthProperty().multiply(0.28));
        tcCantidad.prefWidthProperty().bind(tvList.widthProperty().multiply(0.13));
        tcUnidad.prefWidthProperty().bind(tvList.widthProperty().multiply(0.13));
        tcEstado.prefWidthProperty().bind(tvList.widthProperty().multiply(0.13));
        tcCostoPromedio.prefWidthProperty().bind(tvList.widthProperty().multiply(0.13));
        tcTotal.prefWidthProperty().bind(tvList.widthProperty().multiply(0.13));
    }

    public void fillInventarioTable() {
        ExecutorService exec = Executors.newCachedThreadPool((Runnable runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        Task<ObservableList<ArticuloTB>> task = new Task<ObservableList<ArticuloTB>>() {
            @Override
            public ObservableList<ArticuloTB> call() {
                return ArticuloADO.ListInventario();
            }
        };

        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems(task.getValue());
            double total = 0;
            total = tvList.getItems().stream().map((l) -> l.getTotalImporte()).reduce(total, (accumulator, _item) -> accumulator + _item);
            lblValoTotal.setText(Session.MONEDA + Tools.roundingValue(total, 2));
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

    public void setContent(AnchorPane content) {
        this.content = content;
    }
}
