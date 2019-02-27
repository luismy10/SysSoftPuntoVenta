package controller;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

    private FxCompraController compraController;

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

    private void openWindowAddProveedor() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_PROVEEDOREPROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxProveedorProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Proveedor", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        controller.setValueAdd();
    }

    private void openWindowReload() {
        fillCustomersTable("");
    }

    @FXML
    private void onKeyReleasedToSearch(KeyEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            tvList.requestFocus();
        }
    }

    @FXML
    private void onKeyPressedToRegister(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowAddProveedor();
        }
    }

    @FXML
    private void onActionToRegister(ActionEvent event) throws IOException {
        openWindowAddProveedor();
    }

    @FXML
    private void onKeyPressedToReload(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            openWindowReload();
        }
    }

    @FXML
    private void onActionToReload(ActionEvent event) {
        openWindowReload();
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 2) {
                compraController.setInitComprasValue(tvList.getSelectionModel().getSelectedItem().getNumeroDocumento().get(),
                        tvList.getSelectionModel().getSelectedItem().getRazonSocial().get());
                Tools.Dispose(window);
            }
        }
    }

    void setInitCompraController(FxCompraController comprasController) {
        this.compraController = comprasController;
    }

}
