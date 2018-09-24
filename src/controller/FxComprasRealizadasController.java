package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.VBox;
import model.CompraADO;
import model.CompraTB;

public class FxComprasRealizadasController implements Initializable {

    @FXML
    private VBox window;

    private AnchorPane content;
    @FXML
    private DatePicker dtFechaInicial;
    @FXML
    private DatePicker dtFechaFinal;
    @FXML
    private Label lblLoad;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<CompraTB> tvList;
    @FXML
    private TableColumn<CompraTB, Integer> tcId;
    @FXML
    private TableColumn<CompraTB, LocalDate> tcFechaCompra;
    @FXML
    private TableColumn<CompraTB, String> tcProveedor;
    @FXML
    private TableColumn<CompraTB, Double> tcTotal;

    private Executor exec;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcFechaCompra.setCellValueFactory(cellData -> cellData.getValue().getFechaCompra());
        tcProveedor.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getProveedorTB().getNumeroDocumento().get() + "\n" + cellData.getValue().getProveedorTB().getRazonSocial().get()
        ));
        tcTotal.setCellValueFactory(cellData -> cellData.getValue().getTotal().asObject());
    }

    public void fillPurchasesTable(String value) {

        Task<List<CompraTB>> task = new Task<List<CompraTB>>() {
            @Override
            public ObservableList<CompraTB> call() {
                return CompraADO.ListCompras(value);
            }
        };
        task.setOnSucceeded((WorkerStateEvent e) -> {
            tvList.setItems((ObservableList<CompraTB>) task.getValue());
            lblLoad.setVisible(false);
        });
        task.setOnFailed((WorkerStateEvent event) -> {
            lblLoad.setVisible(false);
        });

        task.setOnScheduled((WorkerStateEvent event) -> {
            lblLoad.setVisible(true);
        });
        exec.execute(task);

    }

    @FXML
    private void onActionView(ActionEvent event) {
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillPurchasesTable("");
    }

    @FXML
    private void onActionSearch(ActionEvent event) {
    }

    void setContent(AnchorPane content) {
        this.content = content;
    }

}
