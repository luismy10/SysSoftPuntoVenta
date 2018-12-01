package controller;

import java.io.IOException;
import java.net.URL;
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
        tcDocumento.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getNumeroDocumento()));
        tcPersona.setCellValueFactory(cellData
                -> Bindings.concat(
                        cellData.getValue().getApellidos() + " ",
                        cellData.getValue().getNombres()
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

            Task<ObservableList<ClienteTB>> task = new Task<ObservableList<ClienteTB>>() {
                @Override
                public ObservableList<ClienteTB> call() {
                    return ClienteADO.ListClienteVenta(value);
                }
            };

            task.setOnSucceeded((WorkerStateEvent e) -> {
                tvList.setItems(task.getValue());
            });

            exec.execute(task);

            if (!exec.isShutdown()) {
                exec.shutdown();
            }
        }

    }
    
    private void openWindowAddCliente() throws IOException {
        URL url = getClass().getResource(Tools.FX_FILE_CLIENTE_PROCESO);
        FXMLLoader fXMLLoader = FxWindow.LoaderWindow(url);
        Parent parent = fXMLLoader.load(url.openStream());
        //Controlller here
        FxClienteProcesoController controller = fXMLLoader.getController();
        //
        Stage stage = FxWindow.StageLoaderModal(parent, "Agregar Cliente", window.getScene().getWindow());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        controller.setValueAdd();
    }

    @FXML
    private void onKeyPressedToSearh(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            tvList.requestFocus();
        }
    }

    @FXML
    private void onActionReload(ActionEvent event) {
        fillCustomersTable("");
    }

    @FXML
    private void onKeyReleasedToSearh(KeyEvent event) {
        fillCustomersTable(txtSearch.getText());
    }

    @FXML
    private void onMouseClickedList(MouseEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (event.getClickCount() == 2) {
                if (ventaController != null) {
                    ventaController.setClienteVenta(tvList.getSelectionModel().getSelectedItem().getIdCliente(),
                            tvList.getSelectionModel().getSelectedItem().getApellidos() + " " + tvList.getSelectionModel().getSelectedItem().getNombres());
                    Tools.Dispose(window);
                }

            }
        }
    }

    @FXML
    private void onKeyPressedList(KeyEvent event) {
        if (tvList.getSelectionModel().getSelectedIndex() >= 0) {
            if (ventaController != null) {
                ventaController.setClienteVenta(tvList.getSelectionModel().getSelectedItem().getIdCliente(),
                        tvList.getSelectionModel().getSelectedItem().getApellidos() + " " + tvList.getSelectionModel().getSelectedItem().getNombres());
                Tools.Dispose(window);
            }
        }
    }
    
     @FXML
    private void onKeyPressedAdd(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.ENTER){
            openWindowAddCliente();
        }
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        openWindowAddCliente();
    }

    public void setInitVentasController(FxVentaController ventaController) {
        this.ventaController = ventaController;
    }

   

}
