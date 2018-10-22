package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.DBUtil;
import model.ProveedorADO;
import model.ProveedorTB;

public class FxProveedorListaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<ProveedorTB> tvList;
    @FXML
    private TableColumn<ProveedorTB, Integer> tcId;
    @FXML
    private TableColumn<ProveedorTB, String> tcDocument;
    @FXML
    private TableColumn<ProveedorTB, String> tcRepresentative;

    private FxComprasController comprasController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);

        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocument.setCellValueFactory(cellData -> cellData.getValue().getNumeroDocumento());
        tcRepresentative.setCellValueFactory(cellData -> Bindings.concat(
                cellData.getValue().getRazonSocial().get() + "\n" + cellData.getValue().getNombreComercial().get()
        ));
    }

    public void fillCustomersTable(String value) {
        if (DBUtil.StateConnection()) {
            ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Task<List<ProveedorTB>> task = new Task<List<ProveedorTB>>() {
                @Override
                public ObservableList<ProveedorTB> call() {
                    return ProveedorADO.ListProveedor(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<ProveedorTB>) task.getValue());
            });
            exec.execute(task);

            if (!exec.isShutdown()) {
                exec.shutdown();
            }
        }

    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {

    }

    @FXML
    private void onActionToSearch(ActionEvent event) {

    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) {

    }

    @FXML
    private void onActionToRegister(ActionEvent event) {

    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 2) {
                comprasController.setInitComprasValue(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento().get(),
                        tvList.getSelectionModel().getSelectedItem().getRazonSocial().get());
                Tools.Dispose(window);
            }
        }
    }

    void setInitComptrasController(FxComprasController comprasController) {
        this.comprasController = comprasController;
    }

}
