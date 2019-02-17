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
    private TableColumn<ArticuloTB, Integer> tcId;
    @FXML
    private TableColumn<ArticuloTB, String> tcArticulo;
    @FXML
    private TableColumn<ArticuloTB, String> tcCosto;
    @FXML
    private TableColumn<ArticuloTB, String> tcPrecioVenta;
    @FXML
    private TableColumn<ArticuloTB, String> tcExistencia;
    @FXML
    private TableColumn<ArticuloTB, String> tcMedida;
    @FXML
    private TableColumn<ArticuloTB, String> tcInvMinimo;
    @FXML
    private TableColumn<ArticuloTB, String> tcInvMaximo;

    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcArticulo.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getClave() + "\n" + cellData.getValue().getNombreMarca()
        ));
        tcCosto.setCellValueFactory(cellData -> Bindings.concat("S/. "+Tools.roundingValue(cellData.getValue().getPrecioCompra(), 2)));
        tcPrecioVenta.setCellValueFactory(cellData -> Bindings.concat("S/. "+Tools.roundingValue(cellData.getValue().getPrecioVenta(), 2)));
        tcExistencia.setCellValueFactory(cellData -> Bindings.concat(
                Tools.roundingValue(cellData.getValue().getCantidad(), 2)
        ));
        tcMedida.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getUnidadVenta() == 1 ? "Por Unidad/Pza" : "A Granel(Kg, Lt, Etc)"
        ));
        tcInvMinimo.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getStockMinimo(), 2)));
        tcInvMaximo.setCellValueFactory(cellData -> Bindings.concat(Tools.roundingValue(cellData.getValue().getStockMaximo(), 2)));
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
            tvList.setItems((ObservableList<ArticuloTB>) task.getValue());
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
