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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.ClienteADO;
import model.ClienteTB;
import model.DBUtil;

public class FxClienteListaController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<ClienteTB> tvList;
    @FXML
    private TableColumn<ClienteTB, Long> tcId;
    @FXML
    private TableColumn<ClienteTB, String> tcDocumento;
    @FXML
    private TableColumn<ClienteTB, String> tcPersona;

    private FxVentaController ventaController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tools.DisposeWindow(window, KeyEvent.KEY_RELEASED);
        tcId.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
        tcDocumento.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getPersonaTB().getNumeroDocumento().get()));
        tcPersona.setCellValueFactory(cellData
                -> Bindings.concat(
                        cellData.getValue().getPersonaTB().getApellidoPaterno() + " ",
                        cellData.getValue().getPersonaTB().getApellidoMaterno() + " ",
                        cellData.getValue().getPersonaTB().getPrimerNombre() + " ",
                        cellData.getValue().getPersonaTB().getSegundoNombre()
                )
        );
    }

    public void fillCustomersTable(String value) {
        if (DBUtil.StateConnection()) {

            ExecutorService exec = Executors.newCachedThreadPool((runnable) -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t;
            });

            Task<List<ClienteTB>> task = new Task<List<ClienteTB>>() {
                @Override
                public ObservableList<ClienteTB> call() {
                    return ClienteADO.ListClienteVenta(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems((ObservableList<ClienteTB>) task.getValue());
            });

            exec.execute(task);

            if (!exec.isShutdown()) {
                exec.shutdown();
            }
        }

    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            tvList.requestFocus();
        }
    }

    @FXML
    private void onKeyReleasedToSearh(KeyEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 2) {
                Tools.Dispose(window);
            }
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillCustomersTable("");
    }

    void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            Tools.Dispose(window);
        }
    }

}
